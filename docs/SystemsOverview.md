# 2.1 Systems Overview

The Systems Overview presents the architecture of the Furry Friends application through four complementary perspectives: User View, Logical View, Process View, and Physical View. Each perspective highlights a different aspect of the system, ensuring that both technical and user requirements are addressed consistently.

- Logical View: Defines the structural organization of the system into modular components, showing how major services such as user management, scheduling, and pet health tracking interact.
- User View: Illustrates how external actors—such as pet owners, veterinarians, shelter staff—interact with the system to achieve their goals.
- Process View: Captures the dynamic runtime behavior of the system, emphasizing workflows such as appointment scheduling, vaccination reminders, and emergency vet dispatch.
- Physical View: Maps the software components to the deployment environment, including mobile devices, cloud servers, and databases, and explains how the system scales to support performance and reliability.

Together, these views provide a comprehensive understanding of the Furry Friends application’s architecture. They enable validation of requirements, evaluation of usability, and assessment of scalability and maintainability prior to implementation. As Bass, Clements, and Kazman (2021) emphasize, viewing architecture from multiple perspectives improves traceability between requirements and design decisions, while also ensuring alignment with stakeholder expectations.

---

## 2.1.1 Software Systems Architecture – Logical View

The logical view organizes the Furry Friends system into major functional components, each responsible for a distinct area of application logic. This structure illustrates how the system’s main features are realized, how responsibilities are distributed, and how components interact through well-defined interfaces. Each component is implemented by one or more classes and communicates with others through explicit relationships or service interfaces.

### User & Access Control

- Features/Functions:
  - Handles user authentication, registration, password management, and role-based access for pet owners, shelter staff, and veterinarians.
- Implemented by:
  - `controller.Controller` (user management methods)
  - `gui.GUI_SceneController` (user interaction and login/registration screens)
  - `models.User` model class
- Interfaces & Relationships:
  - Provides identity verification and user context to Scheduling & Notifications and Pet Health Management.
  - Collaborates with Scheduling & Notifications to personalize alerts and reminders.

### Pet Health Management

- Features/Functions:
  - Manages pet data (breed, age, medical history, dietary needs, behavioral notes, health journals).
  - Supports profile creation, editing, retrieval, and health milestone tracking.
- Implemented by:
  - `controller.Controller` (pet and journal management)
  - `models.Pets`, `models.JournalEntry` model classes
- Interfaces & Relationships:
  - Provides pet data to the (future) AI Recommendation Engine.
  - Aggregates health journal entries for each pet.
  - Requires user context from User & Access Control for secure access.

### Scheduling & Notifications

- Features/Functions:
  - Enables appointment booking, cancellations, and viewing available slots.
  - Sends reminders for veterinary and grooming services, medication, and scheduled care tasks.
- Implemented by:
  - `controller.Controller` (appointment and slot management)
  - `gui.GUI_SceneController` (appointment UI)
  - `models.Calendar` model class
- Interfaces & Relationships:
  - Requires user identity from User & Access Control.
  - Collaborates with Pet Health Management for pet-specific scheduling.
  - Provides scheduling data to `services.ReminderService` (future).
  - Works with User & Access Control for delivery preferences.

### Veterinarian Services

- Features/Functions:
  - Allows licensed professionals to view pet medical histories, add treatments, and manage appointment slots.
- Implemented by:
  - `controller.Controller` (facility and appointment management)
  - `models.Vet_Hospital` model class
- Interfaces & Relationships:
  - Collaborates with Scheduling & Notifications and Pet Health Management.
  - Requires pet data from Pet Health Management.

### Shelter Staff Management

- Features/Functions:
  - Enables shelter staff to manage pet profiles, coordinate appointments, and oversee adoption processes.
  - Supports updating pet availability, reviewing adoption applications, and managing meet-and-greet sessions.
- Implemented by:
  - `controller.Controller` (facility and pet management)
  - `models.Shelter` model class
- Interfaces & Relationships:
  - Provides administrative access to Pet Health Management.
  - Collaborates with User & Access Control to verify adopter identity and track application status.
  - Requires scheduling support from Scheduling & Notifications.

### AI Recommendation Engine (Future Expansion)

- Features/Functions:
  - Analyzes pet data to generate personalized care suggestions.
- Implemented by:
  - Planned for future development
- Interfaces & Relationships:
  - Depends on Pet Health Management for input data.
  - Provides recommendations for User & Access Control for display.

Figure 1: Logical View (component responsibilities and relationships).

---

## 2.1.2 Software Systems Architecture – User View

The User View focuses on the primary actors who interact with Furry Friends application and their corresponding goals. This perspective ensures that system requirements are tied directly to stakeholders’ needs and user activities. According to Garlan and Shaw (1994), a user-centered representation of system behavior clarifies how software supports real-world tasks and provides a foundation for validating design decisions.

- Pet Owner: Creates and manages pet profiles, schedules veterinary appointments, records journal entries, uploads photos, and receives reminders for ongoing care.
- Veterinarian: Accesses shared medical records and health journals for consultation and treatment management.
- Shelter Staff: Supports adoption workflows, animal intake, and care coordination.
- System Administrator: Maintains accounts, access privileges, and overall system stability.

Figure 2: User View use case diagram for Furry Friends Application.

---

## 2.1.3 Software Systems Architecture – Process View

The process view of software architecture represents the dynamic runtime behavior of a system, focusing on how architectural components interact to complete workflows (Otero, 2012). Within the Pet Health Management System, this view is captured through a combination of UML activity, sequence, and collaboration diagrams. Each diagram highlights a different perspective of control flow and system collaboration: activity diagrams illustrate step-by-step workflows, sequence diagrams emphasize time-ordered message exchanges, and collaboration diagrams reveal object-level communication links. Together, these figures provide a comprehensive understanding of how the system supports pet health management processes in practice.

### Intake and Veterinary Check (Activity Diagram)
- Staff initiates intake, completes the form, assigns a temporary profile.
- System schedules an initial veterinary check.
- Decision: If unstable -> immediate medical attention; else -> standard vet check.
- Findings recorded, notifications sent, activity logged in both cases.

Purpose: Ensure intake follows a consistent, safe protocol while maintaining accurate records.

Figure 3: Process View activity diagram — shelter intake and veterinary check workflow.

### Vaccination Reminder Workflow (Sequence Diagram)
- Scheduling component queries pet health data for upcoming vaccination deadlines.
- Notification component formats and sends reminders to the operations dashboard.

Purpose: Automate reminders for vaccinations and treatments to ensure timely care.

Figure 4: Process View sequence diagram — vaccination reminder workflow.

### Appointment Scheduling (Collaboration Diagram)
- Scheduling checks availability service for open slots.
- Profile DB attaches medical history.
- Staff assignment confirmed.
- Appointment details returned to requester.

Purpose: Support efficient coordination between shelter operations and veterinary services.

Figure 5: Process View collaboration diagram — appointment scheduling.

### Routine vs Emergency Scheduling (Sequence Diagram)
1. Routine appointment: Mobile app -> API gateway -> authentication -> scheduling -> confirmation -> notification.
2. Emergency dispatch: API gateway -> emergency authorization -> dispatch service -> ETA -> notification.

Purpose: Model both routine and urgent appointment workflows, highlighting sequential runtime behavior.

Figure 6: Process View sequence diagram — appointment scheduling and emergency vet dispatch.

Together, Figures 3–6 provide a complete picture of runtime behavior. The activity diagram outlines intake, the sequence diagrams capture message flows for reminders and scheduling (including emergencies), and the collaboration diagram shows object-level interactions.

### Registration (User + Process View bridge)
- Actors (Vet Hospital, Shelter) complete registration.
- System creates `users` and `facilities` entries and can provision initial `facility_slots`.
- Bridges user onboarding (User View) with operational readiness (Process View) for scheduling and reminders.

---

## 2.1.4 Software Systems Architecture – Physical View (Deployment)

This view maps software components to the runtime environment and notes scalability & reliability considerations.

- Mobile/Web Client: JavaFX desktop client (prototype); future React Native or Flutter for mobile.
- API Layer: REST API gateway (future) to broker requests from clients.
- Application Services: JVM services (Controller, Scheduling, Reminder, Dispatch) running on app servers or containers.
- Data Layer: SQLite locally for prototype; PostgreSQL/MySQL in cloud for production.
- Storage: Object store for images (prototype bundles under `src/main/resources/gui/images`).

Non-functional considerations:
- Scalability: Horizontal scale of stateless services behind a load balancer; database read replicas.
- Reliability: Health checks, circuit breakers, retry policies; message queues for reminders/dispatch.
- Security: Role-based access control; hashed passwords (`libraries.BCrypt`); secure transport.

---

## Code & Demo Pointers

- JavaFX app: `FurryFriendsApp` and `src/main/resources/gui/Scene.fxml`
- Controller & Models: `controller.Controller`, `models.*`
- Demo services: `services.ReminderService`, `services.EmergencyDispatchService`
- CLI demo: `demo.ProcessViewDemoRunner` — run with `mvn exec:java`

These components map to the views above and can be used during a presentation to narrate Figures 3–6.

---

## 2.1.5 Software Engineering Design Constraints

Design constraints are mandatory rules that guide system design. They arise from performance targets, platform/runtime limitations, security and privacy standards, accessibility expectations, regulatory compliance, and storage limitations (Otero, 2012). The following constraints influence the Furry Friends implementation:

1) Performance & Responsiveness
- Constraint: Core operations (login, facility lookup, listing open slots, booking) should be sub-second on typical datasets.
- Enforcements:
  - Database indexes on hot paths: `users(loginId)`, `users(role)`, `facilities(adminAgent)`, `facilities(type)`, `facility_slots(facilityId, booked)`, `appointments(ownerUsername)`, `appointments(facilityId)`, `pets(ownerUsername)`.
  - Lightweight in-memory flows for the demo mode (CLI) while retaining realistic persistence schema.

2) Platform/Runtime
- Constraint: Java 17, JavaFX runtime (21.x) with Maven build; SQLite for local persistence.
- Enforcements:
  - Mavenized project with JavaFX dependencies and plugin.
  - JavaFX FXML resource paths use classpath URLs and are copied via Maven resources.
  - SQLite JDBC with `PRAGMA foreign_keys = ON` for referential integrity.

3) Security & Privacy
- Constraint: Credentials must not be stored in plaintext; role-based UI access to administrative functions.
- Enforcements:
  - Passwords hashed with `libraries.BCrypt` (OpenBSD-style bcrypt) at registration and verified on login.
  - Role gating in UI: Vet/Shelter-only actions (slot management, facility admin); Owner-only booking views.
  - Minimal PII footprint in demo; production guidance to use TLS and secret management.

4) Accessibility & Usability
- Constraint: Clear, readable UI with visible controls and simple flows for the demo.
- Enforcements:
  - Non-transparent stage, high-contrast text, straightforward buttons for each role.
  - Readable dialogs for results (slots, appointments), avoiding console-only feedback.

5) Regulatory & Compliance (Forward-Looking)
- Constraint: Potential HIPAA/CCPA considerations for health and user data.
- Enforcements (guidance):
  - Separation of concerns (Controller for DB, GUI for presentation), audit-friendly schema (appointments, journal entries).
  - Future: audit logs, consent/retention policies, encryption at-rest.

6) Storage & Data Management
- Constraint: Local dev uses SQLite; must scale to a server RDBMS later (PostgreSQL/MySQL).
- Enforcements:
  - SQL schema portable to mainstream RDBMS; avoid engine-specific features.
  - Facility slots/appointments normalized for clear migration paths.

References: Otero, E. (2012). Software Architecture Design: UML and Patterns for System Design.

### How we validated constraints (step-by-step)
1. Defined the Process View workflows (Figures 1–4) and implemented a CLI runner to exercise them deterministically.
2. Added role-gated UI controls in JavaFX so stakeholders can visualize permissible actions per role.
3. Hardened persistence paths: enabled SQLite foreign keys and created targeted indexes for frequent lookups and joins.
4. Verified security posture basics: bcrypt for passwords; UI hides admin controls for non-admin roles.
5. Performed a clean build and runs via Maven (`exec:java` for CLI; `javafx:run` for GUI) to ensure portability and repeatability.

### References & Further Reading
- Appointment Scheduling & Emergency Dispatch (UML Sequence): See `docs/AppointmentScheduling_EmergencyDispatch.md`.
