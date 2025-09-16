# Appointment Scheduling & Emergency Dispatch

This sequence diagram models two related workflows: routine appointment scheduling and urgent emergency dispatch. It highlights the sequential runtime behavior across the mobile app, API gateway, auth, scheduling, dispatch, and notification components.

## Mermaid Sequence Diagram

```mermaid
sequenceDiagram
    autonumber
    actor Owner as Mobile App (Owner)
    participant API as API Gateway
    participant Auth as Auth Service
    participant Sched as Scheduling Service
    participant Emer as Emergency Authorization
    participant Dispatch as Vet Dispatch Service
    participant Notif as Notification Service

    rect rgb(230, 248, 255)
    Note over Owner,Notif: 1) Routine Appointment Scheduling
    Owner->>API: Submit appointment request (facility, slot, reason)
    API->>Auth: Validate token/session
    Auth-->>API: Auth OK
    API->>Sched: Create appointment (facilityId, slotId, description)
    Sched-->>API: Appointment ID, status=CONFIRMED
    API->>Notif: Notify confirmation (owner)
    Notif-->>Owner: Confirmation (details/ICS)
    end

    rect rgb(255, 241, 230)
    Note over Owner,Notif: 2) Emergency Dispatch
    Owner->>API: Emergency request (symptoms, location)
    API->>Auth: Validate token/session
    Auth-->>API: Auth OK
    API->>Emer: Evaluate urgency (triage)
    Emer-->>API: Urgent=true, priority=HIGH
    API->>Dispatch: Dispatch nearest on-call vet (ETA request)
    Dispatch-->>API: ETA 12 min, crew ID
    API->>Notif: Push ETA to owner
    Notif-->>Owner: Vet dispatched (ETA, map link)
    end
```

## Purpose
- Model both routine and urgent appointment workflows, highlighting sequential runtime behavior.
- Clarify separation of concerns: gateway, authN, scheduling, emergency triage, dispatch, and notifications.

## Notes
- In the current prototype:
  - Routine appointments are created with `appointments` and `facility_slots` tables; the GUI flow prompts for a slot and reason.
  - Emergency dispatch is simulated in the CLI (e.g., `EmergencyDispatchService`) and can be surfaced via UI later.
- Production guidance:
  - API gateway can be an ingress + reverse proxy; auth via JWT.
  - Notifications may send push + email + ICS calendar files.
  - Dispatch integrates with mapping/traffic for accurate ETA.
