# BHASHASETU AI — SOFTWARE ARCHITECTURE DOCUMENT (SAD)
**SIH Problem Statement:** SIH26042 | **Domain:** Mother-Tongue-Based Multilingual Education (MTB-MLE)  
**Document Version:** 2.0.0-PROD | **Status:** Approved Software Design

---

## 1. Monorepo Structure & Module Boundaries

```
/bhashasetu
├── apps/
│   ├── web-frontend/           # Next.js 16.3 + React 19.2 (App Router)
│   │   ├── app/                # Routes & Server Component Pages
│   │   ├── features/           # Feature Modules (lesson-studio, review, analytics)
│   │   └── components/         # UI Primitives (Radix UI + Tailwind CSS v4)
│   └── mobile/                 # Android Native (Kotlin Jetpack Compose) / Flutter 3.x
│       ├── data/local/         # Room / SQLite DAOs & Entities
│       ├── domain/rag/         # On-device Cosine & BM25 Matcher
│       └── ui/screens/         # LessonStudio, VoiceTranslate, StudentPractice
├── services/
│   ├── web-backend/            # NestJS 11 Domain Core & API Gateway
│   │   ├── auth/               # Identity, RBAC, Sessions
│   │   ├── curriculum/         # JCERT / NCERT Content Hierarchy
│   │   ├── lessons/            # Lesson Lifecycle State Machine
│   │   └── sync/               # Outbox Reconciliation & Conflict Engine
│   └── ai-platform/            # FastAPI + Python 3.12 AI Services
│       ├── rag/                # Hybrid Ingestion, BGE-M3 Embeddings, RRF
│       ├── translation/        # NLLB, Gemini 3.1/3.5, Bhashini Adapters
│       ├── voice/              # Silero VAD, Whisper ASR, Kokoro/Piper TTS
│       └── quality/            # COMETKiwi, XCOMET, MQM Error Span Tagger
└── packages/
    ├── contracts/              # OpenAPI 3.1 Schemas, Generated TypeScript & Dart Types
    └── ui-kit/                 # Design Tokens, Accessible Visual Primitives
```

---

## 2. Core Domain State Machines

### 2.1 Lesson Lifecycle State Machine
```
[DRAFT] ──(Teacher inputs prompt)──► [GENERATING] ──(RAG & MT Complete)──► [REVIEW_REQUIRED]
                                                                                  │
     ┌────────────────────────────────────────────────────────────────────────────┴────────┐
     ▼                                                                                     ▼
[APPROVED] ──(Publish Action)──► [PUBLISHED]                                          [REJECTED]
    │                                │                                                     │
    ▼                                ▼                                                     ▼
(Ready for Offline Sync)   (Immutable Version N+1)                                  (Feedback Loop)
```

### 2.2 Offline Outbox Sync State Machine
```
[LOCAL_SAVED] ──► [QUEUED_OUTBOX] ──(Network Detected)──► [SENDING]
                                                              │
                                     ┌────────────────────────┴────────────────────────┐
                                     ▼                                                 ▼
                              [ACK_SYNCED]                                     [CONFLICT / RETRY]
                                   │                                                   │
                                   ▼                                                   ▼
                          (Cursor Advanced)                                    (Backoff & Merge)
```
