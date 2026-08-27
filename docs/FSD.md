# BHASHASETU AI — FUNCTIONAL SPECIFICATION DOCUMENT (FSD)
**SIH Problem Statement:** SIH26042 | **Domain:** Mother-Tongue-Based Multilingual Education (MTB-MLE)  
**Document Version:** 2.0.0-PROD | **Status:** Approved Functional Spec

---

## 1. Functional Requirements by Domain

### F06: Lesson Studio & Curriculum Scaffolding
- **FSD-LESSON-001 (Curriculum Selection):** The teacher selects Grade (1–5), Subject (Language/Math/EVS/Heritage), and Learning Objective from preloaded JCERT curriculum nodes.
- **FSD-LESSON-002 (Prompt & Language Selection):** The teacher enters a concept in Hindi (text or speech) and selects the target language (Santhali, Ho, Mundari).
- **FSD-LESSON-003 (Generation & Adaptation):** System retrieves curriculum context, simplifies language for grade-level, injects local cultural analogies (Sarhul, Karam, Sohrai), and generates native script + transliteration.

### F12: Real-Time Bidirectional Voice Translation
- **FSD-VOICE-001 (Live Classroom Dialogue):** Teacher speaks Hindi sentence. VAD captures audio, streaming ASR transcribes text, RAG retrieves vocabulary grounding, MT translates to target language, and TTS synthesizes audio in <= 3.0s total latency.
- **FSD-VOICE-002 (Transliteration Assistance):** UI displays phonetic Devanagari and Latin script to assist the non-tribal teacher in reading aloud.

### F18 & F19: Offline Student Practice & Assessment
- **FSD-ASSESS-001 (Offline Interactive Quiz):** Students practice formative questions offline. Immediate feedback (visual green/amber badges + tribal audio explanation) is provided.
- **FSD-ASSESS-002 (Append-Only Local Storage):** Student score, attempt timestamp, and question ID are stored locally in Room SQLite database.

### F23 & F24: Durable Synchronization & Conflict Handling
- **FSD-SYNC-001 (Outbox Synchronization):** On network resumption, pending assessments and approved lessons sync via idempotent `operation_id` HTTP POST.
- **FSD-SYNC-002 (Conflict Policy):** Published lessons are immutable; student attempts are append-only; teacher corrections override automated drafts.

---

## 2. Acceptance Criteria (Given / When / Then)

```gherkin
Scenario: Offline Lesson Delivery and Formative Assessment
  Given the teacher has downloaded the Grade 2 Santhali Science content package
  And the Android tablet is completely disconnected from the Internet (Airplane Mode)
  When the teacher selects the lesson "पेड़ और पत्तियाँ" (Trees and Leaves)
  Then the app renders the lesson in Ol Chiki script with Devanagari transliteration
  And plays the preloaded Santhali audio pronunciation without network error
  When the student completes the 5-question interactive quiz
  Then the student attempt is saved to local SQLite with status PENDING_OUTBOX
  When network connectivity is restored
  Then the background sync service pushes the attempt to Cloud Firestore
  And updates the sync status to SYNCED with zero data loss.
```
