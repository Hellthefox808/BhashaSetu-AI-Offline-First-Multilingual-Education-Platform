# BHASHASETU AI — FUNCTIONAL TRACEABILITY LEDGER (FTL)
**SIH Problem Statement:** SIH26042 | **Domain:** Mother-Tongue-Based Multilingual Education (MTB-MLE)  
**Document Version:** 2.0.0-PROD | **Status:** Verified Traceability Chain

---

## 1. Master Traceability Matrix

| FTL-ID | PRD-ID | TAD-ID | SAD-ID | FSD-ID | Code Module | Verification Method | Status |
|---|---|---|---|---|---|---|---|
| **FTL-01** | REQ-01 | TAD-AI-01 | SAD-AI-TR | FSD-LESSON-002 | `app/src/main/java/com/example/data/remote/GeminiApiService.kt` | Unit + Translation Test | **VERIFIED** |
| **FTL-02** | REQ-02 | TAD-PED-01 | SAD-PED-01 | FSD-LESSON-003 | `app/src/main/java/com/example/domain/model/Models.kt` | Pedagogical Rule Test | **VERIFIED** |
| **FTL-03** | REQ-04 | TAD-TTS-01 | SAD-AI-TTS | FSD-VOICE-001 | `app/src/main/java/com/example/ui/util/TtsManager.kt` | Audio Playback Test | **VERIFIED** |
| **FTL-04** | REQ-05 | TAD-VOICE-01 | SAD-VOICE-01 | FSD-VOICE-001 | `app/src/main/java/com/example/ui/screens/VoiceTranslateScreen.kt` | E2E Latency Benchmark | **VERIFIED** |
| **FTL-05** | REQ-08 | TAD-RAG-01 | SAD-RAG-01 | FSD-LESSON-001 | `app/src/main/java/com/example/domain/rag/LocalRagEmbeddingEngine.kt` | Recall@K Benchmark | **VERIFIED** |
| **FTL-06** | REQ-09 | TAD-GOV-01 | SAD-GOV-01 | FSD-LESSON-003 | `app/src/main/java/com/example/ui/screens/LessonStudioScreen.kt` | HITL Workflow Test | **VERIFIED** |
| **FTL-07** | REQ-10 | TAD-EDGE-01 | SAD-MOB-01 | FSD-ASSESS-001 | `app/src/main/java/com/example/data/local/AppDatabase.kt` | Offline Fault Injection | **VERIFIED** |
| **FTL-08** | REQ-11 | TAD-SYNC-01 | SAD-SYNC-01 | FSD-SYNC-001 | `app/src/main/java/com/example/data/remote/FirebaseService.kt` | Idempotency Test | **VERIFIED** |
| **FTL-09** | REQ-15 | TAD-QE-01 | SAD-AI-QE | FSD-LESSON-003 | `app/src/main/java/com/example/domain/model/Models.kt` | COMET Score Validation | **VERIFIED** |
| **FTL-10** | REQ-16 | TAD-SEC-01 | SAD-SEC-01 | FSD-AUTH-001 | `app/src/main/java/com/example/ui/components/UserProfileSheet.kt` | RBAC & RLS Security Test | **VERIFIED** |

---

## 2. SIH 2026 Live Demo Traceability Chain

```
[1. Teacher Speaks Hindi] (FTL-04 / FSD-VOICE-001)
            ↓
[2. Local RAG Grounding] (FTL-05 / FSD-LESSON-001)
            ↓
[3. Pedagogical Adaptation] (FTL-02 / FSD-LESSON-003)
            ↓
[4. Teacher HITL Approval] (FTL-06 / FSD-LESSON-003)
            ↓
[5. Offline Delivery in Airplane Mode] (FTL-07 / FSD-ASSESS-001)
            ↓
[6. Formative Student Quiz Submission] (FTL-07 / FSD-ASSESS-001)
            ↓
[7. Network Reconnection & Outbox Sync] (FTL-08 / FSD-SYNC-001)
            ↓
[8. Live Dashboard Verification] (FTL-10 / FSD-AUTH-001)
```
