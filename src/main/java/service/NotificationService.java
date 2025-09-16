package service;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/**
 * NotificationService schedules and dispatches reminder notifications to users.
 * It runs a lightweight ScheduledExecutor and invokes registered listeners when
 * it's time to notify. UI layers can register a listener and route messages to
 * toast/snackbar or other visual elements.
 */
public class NotificationService implements AutoCloseable {
    public interface Listener {
        void onNotify(String userLoginId, String message, String type);
    }

    private final ScheduledExecutorService scheduler;
    private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();
    private final Map<String, CopyOnWriteArrayList<ScheduledFuture<?>>> tasksByUser = new ConcurrentHashMap<>();
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public NotificationService() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "notification-service");
            t.setDaemon(true);
            return t;
        });
    }

    public void addListener(Listener l) {
        if (l != null) listeners.add(l);
    }

    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    /**
     * Schedule a reminder for the specified user at a given time.
     * If the computed delay is negative, a small delay (5s) is used instead.
     *
     * @param userLoginId user login id
     * @param when local date-time when the reminder should fire
     * @param zone zone for the local date-time
     * @param message message to deliver
     * @param type optional type (info/success/error)
     */
    public void scheduleReminder(String userLoginId, LocalDateTime when, ZoneId zone, String message, String type) {
        Objects.requireNonNull(userLoginId, "userLoginId");
        Objects.requireNonNull(when, "when");
        if (closed.get()) return;
        Instant target = when.atZone(zone == null ? ZoneId.systemDefault() : zone).toInstant();
        long delayMs = Duration.between(Instant.now(), target).toMillis();
        if (delayMs < 0) delayMs = 5000; // fire soon if target already passed
        scheduleAfter(userLoginId, delayMs, message, type);
    }

    /**
     * Convenience: schedule after a fixed delay in milliseconds.
     */
    public void scheduleAfter(String userLoginId, long delayMs, String message, String type) {
        if (closed.get()) return;
        ScheduledFuture<?> f = scheduler.schedule(() -> notifyListeners(userLoginId, message, type), Math.max(0, delayMs), TimeUnit.MILLISECONDS);
        tasksByUser.computeIfAbsent(userLoginId, k -> new CopyOnWriteArrayList<>()).add(f);
    }

    /**
     * Cancel all scheduled tasks for a user (e.g., on logout) without interrupting running tasks.
     */
    public void cancelAllForUser(String userLoginId) {
        List<ScheduledFuture<?>> futures = tasksByUser.remove(userLoginId);
        if (futures != null) futures.forEach(f -> f.cancel(false));
    }

    private void notifyListeners(String userLoginId, String message, String type) {
        for (Listener l : listeners) {
            try { l.onNotify(userLoginId, message, type == null ? "info" : type); } catch (Throwable ignore) {}
        }
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            scheduler.shutdownNow();
            tasksByUser.clear();
            listeners.clear();
        }
    }
}
