# BHASHASETU AI (भाषासेतु) — FUNCTIONAL SPECIFICATION DOCUMENT (FSD)
**SIH Problem Statement:** SIH26042 | **Domain:** Mother-Tongue-Based Multilingual Education (MTB-MLE)  
**Target Region:** Jharkhand Primary Schools (Grades 1–5; Santhali, Ho, Mundari)  
**Document Version:** 3.0.0-PROD | **Status:** Approved Functional Specification  
**Classification:** Living Executable Functional Requirements Document

---

## 1. Functional Scope & Domain Catalog

The functional capabilities of BhashaSetu AI are categorized into 30 discrete domains:

| Domain ID | Domain Title | Key Actor | Core Functional Purpose | Priority |
|---|---|---|---|---|
| **F01** | Identity & Authentication | All Users | Secure login, session management, Argon2id auth, role discovery | **P0** |
| **F02** | Role-Based Access Control | All Users | Enforce least-privilege RBAC permissions per endpoint | **P0** |
| **F03** | Tenant / School Scoping | Admin / Teacher | Enforce strict school-level and district-level data scoping | **P0** |
| **F04** | Curriculum Management | Admin / Teacher | Manage state-prescribed JCERT/NCERT curriculum hierarchy | **P0** |
| **F05** | Learning Outcomes (LO) | Teacher / Admin | Link all content to official NIPUN Bharat learning outcome codes | **P0** |
| **F06** | Lesson Studio | Teacher | Multi-step curriculum scaffolding, prompt input, and generation | **P0** |
| **F07** | Language Identification | System / Teacher | Automatic detection of spoken/typed Hindi and tribal dialects | **P0** |
| **F08** | Curriculum Translation | System | Grounded translation into Santhali, Ho, and Mundari | **P0** |
| **F09** | Transliteration Engine | System / Teacher | Native script rendering (Ol Chiki, Warang Chiti) + phonetic guides | **P0** |
| **F10** | Pedagogical Adaptation | System | Injects localized cultural analogies and simplifies grade-level text | **P0** |
| **F11** | Hybrid Multilingual RAG | System | Retrieval of textbook chunks via BM25 + BGE-M3 + DiskANN | **P0** |
| **F12** | Live Voice-to-Voice | Teacher / Student | Sub-3s streaming classroom dialogue translation | **P0** |
| **F13** | Speech Synthesis (TTS) | System / Teacher | Natural, intelligible tribal language audio synthesis | **P0** |
| **F14** | Bilingual Worksheets | Teacher / Student | Automated generation of printable dual-language PDF exercises | **P0** |
| **F15** | Visual Flashcards | Teacher / Student | Multimodal visual cards with audio pronunciation triggers | **P0** |
| **F16** | Teacher Review & Approval | Teacher / Reviewer | HITL review interface with COMET quality scoring | **P0** |
| **F17** | Content Publishing | Teacher / Admin | Immutable versioning and packaging of approved lessons | **P0** |
| **F18** | Student Learning Delivery | Student / Teacher | Offline classroom presentation of lessons and audio | **P0** |
| **F19** | Formative Assessment | Student / Teacher | On-device interactive quizzes with deterministic scoring | **P0** |
| **F20** | Progress & FLN Tracking | Teacher / Admin | Append-only tracking of student competency achievements | **P0** |
| **F21** | Offline Content Packs | Teacher / Operator | Bundled zip packages of verified lessons and audio | **P0** |
| **F22** | Local Offline Database | Mobile Device | SQLite (Room/Drift) local-first storage of all records | **P0** |
| **F23** | Durable Outbox Sync | System | Resilient, low-bandwidth push/pull sync engine with UUID idempotency | **P0** |
| **F24** | Conflict Handling | System | Deterministic conflict resolution policies across entities | **P0** |
| **F25** | Device Management | Admin / Operator | Track tablet serials, storage health, battery, and sync status | **P1** |
| **F26** | Administrative Analytics | District Admin | School-level FLN attainment and sync telemetry dashboards | **P0** |
| **F27** | Audit & Provenance | Security Admin | Cryptographic audit trail of all AI generations and approvals | **P0** |
| **F28** | Notification Dispatcher | Teacher / Admin | In-app alerts for sync status, new content, and review tasks | **P1** |
| **F29** | AI Quality Monitoring | AI Engineer | Real-time monitoring of COMET scores and terminology compliance | **P0** |
| **F30** | Operational Health | DevOps / SRE | Health checks for database, Redis queues, and AI inference | **P0** |

---

## 2. Exhaustive Functional Specifications

### 2.1 F01: Identity & Authentication
- **Requirement ID:** `FSD-AUTH-001`
- **Actor:** Teacher / Admin / Native Reviewer
- **Goal:** Authenticate securely into the platform and obtain authorized tenant context.
- **Trigger:** User launches app or enters web login URL.
- **Preconditions:** Valid user record exists in PostgreSQL / SQLite.
- **Process:**
  1. User enters email/identifier and password.
  2. Gateway hashes password with Argon2id and verifies against database record.
  3. Gateway issues a signed JWT access token ($15\text{m}$ TTL) and sets a secure, HTTP-only refresh cookie.
  4. Client stores token securely (Android EncryptedSharedPreferences / Secure Storage) and loads tenant workspace.
- **Business Rules:**
  - Failed login attempts are throttled via Redis token bucket ($5\text{ attempts per 5 minutes}$).
  - Generic error message displayed on failure ("Invalid email or password").
- **Acceptance Criteria:**
  ```gherkin
  Given a registered teacher with email "ramesh@jcert.in" and valid password
  When the teacher submits the login form
  Then a JWT token containing school_id and role=TEACHER is returned
  And the teacher is redirected to the Lesson Studio dashboard.
  ```

---

### 2.2 F06 & F10: Lesson Studio & Pedagogical Adaptation
- **Requirement ID:** `FSD-LESSON-001`
- **Actor:** Primary School Teacher
- **Goal:** Create a culturally adapted, curriculum-grounded lesson in a tribal language from a Hindi prompt.
- **Trigger:** Teacher clicks "New Lesson" in Lesson Studio.
- **Inputs:**
  - Grade: `Grade 2`
  - Subject: `EVS (Environmental Studies)`
  - Learning Outcome: `LO-EVS-G2-03 (Identify local flora & leaf shapes)`
  - Hindi Prompt: *"पेड़ और पत्तियों के प्रकार और उनके कार्य"*
  - Target Language: `Santhali (sat_Olck)`
- **Process:**
  1. System executes metadata-filtered hybrid retrieval against JCERT textbook chunks.
  2. Context is injected into the LLM prompt with local analogy directives (Sarhul festival, Sal trees).
  3. LLM generates Santhali text, Ol Chiki script rendering, phonetic Devanagari transliteration, and student activities.
  4. System computes COMETKiwi quality score and verifies JCERT glossary constraints.
  5. System generates high-fidelity audio via Kokoro/Bhashini TTS and presents the result in the Review Panel.
- **State Transition:** `DRAFT` $\to$ `GENERATING` $\to$ `REVIEW_REQUIRED`.
- **Offline Behavior:** If offline, the teacher can select from pre-indexed on-device curriculum templates and local glossaries.
- **Acceptance Criteria:**
  ```gherkin
  Given the teacher selects Grade 2 EVS and Santhali language
  When the teacher enters the Hindi prompt "पेड़ों के प्रकार"
  Then the system generates Santhali text in Ol Chiki script
  And includes a cultural reference to the Sarhul festival
  And computes a COMET quality score >= 0.80
  And renders phonetic Devanagari transliteration.
  ```

---

### 2.3 F12: Real-Time Voice-to-Voice Classroom Translation
- **Requirement ID:** `FSD-VOICE-001`
- **Actor:** Primary Teacher & Tribal Student
- **Goal:** Enable live, sub-3-second bidirectional spoken dialogue in the classroom.
- **Trigger:** Teacher presses and holds the microphone button.
- **Process:**
  1. Silero VAD detects speech activity and streams audio frames.
  2. Streaming ASR (Whisper / Bhashini) transcribes Hindi speech.
  3. FastAPI engine translates text to target tribal language with glossary constraint enforcement.
  4. TTS engine synthesizes audio chunks in parallel.
  5. Mobile ExoPlayer begins audio playback in $\le 3000\text{ ms}$ total elapsed time.
- **UI States:** `Listening` $\to$ `Processing` $\to$ `Speaking` $\to$ `Idle`.
- **Failure Recovery:** If network drops during voice call, system falls back to on-device cached bilingual phrasebook.
- **Acceptance Criteria:**
  ```gherkin
  Given the teacher is in live voice mode set to Hindi -> Ho
  When the teacher speaks "बच्चों, अपनी किताब खोलो" (Children, open your books)
  Then the system synthesizes Ho audio within 3000 ms
  And displays phonetic transliteration on the tablet screen.
  ```

---

### 2.4 F18 & F19: Offline Student Practice & Formative Assessment
- **Requirement ID:** `FSD-ASSESS-001`
- **Actor:** Tribal Student & Teacher
- **Goal:** Deliver interactive formative quizzes and record scores 100% offline.
- **Trigger:** Student starts an interactive quiz on the classroom tablet.
- **Process:**
  1. Tablet loads quiz from local SQLite database (zero network requests).
  2. Student answers multiple-choice, matching, or visual questions.
  3. Immediate feedback provided via green/amber visual indicators and spoken tribal audio explanations.
  4. Attempt record (`attempt_id`, `score`, `timestamp`) written atomically to local `assessments` and `outbox` tables.
- **State Transition:** `STARTED` $\to$ `IN_PROGRESS` $\to$ `SAVED_LOCAL` $\to$ `QUEUED_OUTBOX`.
- **Acceptance Criteria:**
  ```gherkin
  Given the Android tablet is in Airplane Mode
  When the student completes a 5-question quiz on Santhali vocabulary
  Then the score is immediately displayed to the student
  And the attempt is stored in local SQLite with sync_status=PENDING
  And zero network error toasts or dialogs appear.
  ```

---

### 2.5 F23 & F24: Durable Outbox Synchronization & Conflict Engine
- **Requirement ID:** `FSD-SYNC-001`
- **Actor:** Mobile Background Sync Worker & Backend Gateway
- **Goal:** Replicate offline student attempts and teacher approvals to the cloud backend with zero data loss or duplication.
- **Trigger:** Connectivity restoration, app launch, or manual sync swipe.
- **Process:**
  1. Worker acquires local sync lock and collects pending records from `outbox` table.
  2. Compresses batch using Gzip and issues `POST /api/v1/sync/push` with unique `operation_id` headers.
  3. Backend applies idempotent transaction, verifies tenant boundary, and returns success receipt.
  4. Worker issues `GET /api/v1/sync/pull?cursor=...` to fetch server deltas.
  5. Local database updates records, resolves conflicts via deterministic policies, and advances cursor.
- **Conflict Rules:**
  - Published lessons: `IMMUTABLE` (creates Version $N+1$).
  - Student attempts: `APPEND_ONLY` (all attempts preserved).
  - Teacher corrections: `TEACHER_AUTHORITATIVE` (overrides automated AI draft).
- **Acceptance Criteria:**
  ```gherkin
  Given 10 pending assessment attempts in the tablet's local outbox
  When network connectivity is restored
  Then the sync worker uploads all 10 records in a single batch
  And the server acknowledges the transaction with status 200 OK
  And the tablet marks all 10 records as ACK_SYNCED with zero duplicate rows.
  ```

---

## 3. Screen State Contracts (Frontend & Mobile)

Every user interface screen in BhashaSetu AI must implement five explicit states:

| State | Visual & Behavioral Contract |
|---|---|
| **1. LOADING** | Skeleton loaders matching exact component layout; zero layout shift (CLS $< 0.05$). Non-blocking spinners for background audio generation. |
| **2. EMPTY** | Helpful visual empty state with clear Call to Action (e.g., *"No lessons created yet. Click 'New Lesson' to start scaffolding"*). |
| **3. ERROR** | Human-readable bilingual error banner with explicit recovery action (e.g., *"Audio playback failed. Tap to retry"*). Technical error code logged internally. |
| **4. OFFLINE** | Persistent subtle badge indicating *"Offline Mode (Working Locally)"*. All primary actions (create, edit, teach, quiz) remain 100% functional. |
| **5. SUCCESS** | Rendered interactive UI with smooth transitions, accessible keyboard navigation, and high-contrast color scheme ($> 4.5:1$ contrast ratio). |

---

## 4. Error Code Catalog

| Error Code | Category | User-Facing Message | System Recovery Action |
|---|---|---|---|
| `ERR_AUTH_EXPIRED` | Auth | *"Session expired. Please log in again."* | Redirect to login with redirect URL preserved. |
| `ERR_RAG_NO_EVIDENCE` | AI | *"Curriculum context not found for this topic. Using foundational glossary."* | Fall back to JCERT glossary keywords; flag for review. |
| `ERR_QE_SCORE_LOW` | AI | *"AI translation quality is below threshold. Manual teacher review required."* | Transition lesson to `REVIEW_REQUIRED`; disable auto-publish. |
| `ERR_VOICE_TIMEOUT` | Voice | *"Voice service timed out. Switching to offline phrasebook."* | Fall back to pre-rendered local audio cache. |
| `ERR_SYNC_NETWORK_DROP` | Sync | *"Sync paused due to poor network. Changes saved safely offline."* | Re-queue outbox items with exponential backoff and jitter. |
| `ERR_SYNC_CONFLICT` | Sync | *"New version available from another device. Created a local draft."* | Fork conflicting changes into versioned draft; never overwrite. |
