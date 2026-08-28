# BHASHASETU AI (भाषासेतु) — SOFTWARE ARCHITECTURE DOCUMENT (SAD)
**SIH Problem Statement:** SIH26042 | **Domain:** Mother-Tongue-Based Multilingual Education (MTB-MLE)  
**Document Version:** 3.0.0-PROD | **Status:** Approved Software Architecture  
**Classification:** Implementation-Ready Software Engineering Blueprint

---

## 1. Monorepo Structure & Internal Module Organization

```text
/bhashasetu
├── apps/
│   ├── web-frontend/                   # Next.js 16.3 + React 19.2 (App Router)
│   │   ├── app/                        # Route handlers & React Server Components
│   │   │   ├── (auth)/login/
│   │   │   ├── (dashboard)/layout.tsx
│   │   │   ├── (dashboard)/lessons/
│   │   │   └── (dashboard)/admin/
│   │   ├── features/                   # Domain-bounded feature modules
│   │   │   ├── lesson-studio/          # Multi-step creation, RAG review, Ol Chiki canvas
│   │   │   │   ├── components/
│   │   │   │   ├── hooks/
│   │   │   │   ├── api/
│   │   │   │   └── stores/
│   │   │   ├── voice-dialogue/         # Real-time microphone & streaming audio
│   │   │   └── analytics/              # FLN progress charts & sync health maps
│   │   └── components/ui/              # Radix UI + Tailwind CSS v4 primitives
│   └── mobile/                         # Android Native (Kotlin Compose) / Flutter 3.x
│       ├── data/local/                 # Room / SQLite Entities, DAOs, AppDatabase
│       ├── data/remote/                # Retrofit / Dio API Clients & Outbox Sync Worker
│       ├── domain/model/               # Domain Models (Lesson, Assessment, SyncOperation)
│       ├── domain/rag/                 # On-device Cosine & BM25 Matcher
│       └── ui/screens/                 # LessonStudio, VoiceTranslate, StudentPractice
├── services/
│   ├── web-backend/                    # NestJS 11 + Node.js 22 LTS Domain Gateway
│   │   ├── src/
│   │   │   ├── auth/                   # Identity, Argon2id, JWT, RBAC Guards
│   │   │   ├── curriculum/             # JCERT/NCERT hierarchy, Learning Outcomes
│   │   │   ├── lessons/                # Lesson State Machine, HITL Review, Versioning
│   │   │   │   ├── domain/             # Entities, Value Objects, Domain Policies
│   │   │   │   ├── application/        # Commands, Queries, Use-Case Handlers
│   │   │   │   ├── infrastructure/     # TypeORM / Prisma Repositories, RLS Interceptors
│   │   │   │   └── presentation/       # REST Controllers & OpenAPI Annotations
│   │   │   ├── sync/                   # Outbox Reconciliation, Delta Generator, Conflict Engine
│   │   │   └── ai-orchestration/       # gRPC / HTTP Dispatcher to AI Platform
│   └── ai-platform/                    # FastAPI + Python 3.12 AI Microservice
│       ├── rag/                        # Hybrid Ingestion, BGE-M3 Embeddings, RRF, Cross-Encoder
│       ├── translation/                # NLLB-200, Bhashini Adapter, Gemini 3.1/3.5 Pro
│       ├── voice/                      # Silero VAD, Whisper ASR, Kokoro/Piper TTS
│       ├── quality/                    # COMETKiwi Reference-Free QE, XCOMET MQM Error Spans
│       └── pedagogy/                   # Cultural analogy injector, Grade simplifier
├── packages/
│   ├── contracts/                      # OpenAPI 3.1 Schemas, Generated TypeScript & Dart Types
│   └── ui-kit/                         # Shared Design Tokens & Visual Primitives
├── infra/                              # Docker Compose, PostgreSQL 18, Redis 7.4, Nginx
└── docs/                               # PRD, TAD, SAD, FSD, FTL, Architecture Blueprints
```

---

## 2. Core Domain Model & Entity Definitions

### 2.1 Entity Relationship Diagram
```text
┌─────────────────┐       1..* ┌─────────────────┐       1..* ┌─────────────────┐
│     School      ├───────────►│      User       ├───────────►│     Device      │
│  (Tenant Root)  │            │ (Teacher/Admin) │            │ (Tablet Serial) │
└────────┬────────┘            └────────┬────────┘            └────────┬────────┘
         │ 1                            │ 1                            │ 1
         │                              │                              │
         ▼ *                            ▼ *                            ▼ *
┌─────────────────┐            ┌─────────────────┐            ┌─────────────────┐
│   Curriculum    │            │     Lesson      │            │  SyncOperation  │
│  (Grade/FLN/LO) │            │ (State Machine) │            │  (Outbox UUID)  │
└────────┬────────┘            └────────┬────────┘            └─────────────────┘
         │ 1                            │ 1
         │                              │
         ▼ *                            ▼ *
┌─────────────────┐            ┌─────────────────┐
│ CurriculumChunk │            │  LessonVersion  │
│ (Vector+BM25)   │            │ (Immutable N+1) │
└─────────────────┘            └────────┬────────┘
                                        │ 1
                                        │
                                        ▼ 1..*
                               ┌─────────────────┐
                               │   Assessment    │
                               │(Attempts Logged)│
                               └─────────────────┘
```

### 2.2 Core Entity Definitions
- **School (Tenant Root):** `school_id`, `name`, `district`, `block`, `state_code` (`JH`), `created_at`.
- **User (Educator / Reviewer / Admin):** `user_id`, `school_id`, `email`, `role` (`TEACHER`, `NATIVE_REVIEWER`, `SCHOOL_ADMIN`, `DISTRICT_ADMIN`), `password_hash`, `languages_spoken`.
- **Lesson:** `lesson_id`, `school_id`, `creator_id`, `grade` ($1\text{--}5$), `subject`, `learning_outcome_code`, `status` (`DRAFT`, `GENERATING`, `REVIEW_REQUIRED`, `APPROVED`, `PUBLISHED`, `ARCHIVED`), `current_version_no`.
- **LessonVersion:** `version_id`, `lesson_id`, `version_no`, `hindi_prompt`, `target_language_code`, `native_script_content`, `transliteration`, `cultural_analogy_notes`, `audio_asset_url`, `comet_quality_score`, `created_at`.
- **AssessmentAttempt (Append-Only):** `attempt_id`, `student_id`, `lesson_id`, `version_no`, `score`, `max_score`, `answers_json`, `device_id`, `timestamp`.
- **SyncOperation:** `operation_id`, `device_id`, `entity_type`, `operation`, `payload_json`, `sequence_no`, `status` (`PENDING`, `IN_FLIGHT`, `ACKNOWLEDGED`, `CONFLICT`), `retry_count`.

---

## 3. Domain State Machines

### 3.1 Lesson Lifecycle State Machine
```text
                  [Teacher enters Hindi Prompt]
                                │
                                ▼
                             [DRAFT]
                                │
                                ▼ [Trigger AI Pipeline]
                          [GENERATING]
                                │
                                ▼ [RAG, MT, TTS & QE Completed]
                       [REVIEW_REQUIRED]
                                │
          ┌─────────────────────┴─────────────────────┐
          │                                           │
          ▼ [Teacher Approves]                        ▼ [Teacher Rejects / Edits]
      [APPROVED]                                  [REJECTED]
          │                                           │
          ▼ [Publish Action]                          ▼ [Feedback Loop]
     [PUBLISHED]                                 [DRAFT (Edit)]
  (Immutable Version N+1)
  (Enqueued for Sync Bundle)
```

### 3.2 Offline Sync Outbox State Machine
```text
[Local Classroom Action (Quiz/Lesson)]
                  │
                  ▼
            [LOCAL_SAVED]
                  │
                  ▼ (Atomic SQLite Transaction)
           [QUEUED_OUTBOX]
                  │
                  ▼ [Network Connectivity Detected]
              [SENDING] (Idempotent POST with UUID)
                  │
     ┌────────────┴────────────┐
     ▼                         ▼
[ACK_SYNCED]            [CONFLICT / RETRY]
     │                         │
     ▼                         ▼
(Advance Local Cursor)   (Exponential Backoff & Merge)
```

---

## 4. AI Orchestration & Multi-Signal Quality Pipeline

### 4.1 Orchestration Workflow (FastAPI Engine)
```text
POST /api/v1/ai/generate-lesson
       │
       ▼
1. Metadata & Query Extraction (Pydantic v2 validation)
       │
       ▼
2. Hybrid Retrieval Execution (PostgreSQL pgvector/DiskANN + Lexical BM25)
       │
       ▼
3. Reciprocal Rank Fusion & Cross-Encoder Reranking
       │
       ▼
4. Context Formatting (<curriculum_evidence>...</curriculum_evidence>)
       │
       ▼
5. LLM Pedagogical Generation (Gemini 3.1 Pro / NLLB-200 with Local Analogies)
       │
       ▼
6. Phonetic Transliteration Generation (Ol Chiki / Devanagari / Latin)
       │
       ▼
7. Text-to-Speech Audio Synthesis (Kokoro / Piper TTS Engine)
       │
       ▼
8. Multi-Signal Quality Estimation Gate (COMETKiwi + Glossary Check)
       │
       ▼
Response Envelope: { lesson_payload, audio_url, quality_report, provenance_metadata }
```

### 4.2 Quality Evaluation Logic
```python
def evaluate_translation_quality(
    source_hindi: str,
    target_tribal: str,
    glossary_terms: list[str],
    evidence_text: str
) -> QualityReport:
    # 1. Terminology Compliance Check
    missing_terms = [t for t in glossary_terms if t not in target_tribal]
    term_score = 1.0 - (len(missing_terms) / max(len(glossary_terms), 1))
    
    # 2. Reference-Free COMETKiwi Score
    comet_score = comet_model.predict(source=source_hindi, target=target_tribal)
    
    # 3. Grounding Confidence (Overlap with JCERT textbook evidence)
    grounding_score = calculate_ngram_overlap(target_tribal, evidence_text)
    
    composite_score = (comet_score * 0.5) + (term_score * 0.3) + (grounding_score * 0.2)
    
    if composite_score >= 0.85 and not missing_terms:
        status = "HIGH_CONFIDENCE"
    elif composite_score >= 0.70:
        status = "MEDIUM_CONFIDENCE" # Requires Teacher HITL Sign-off
    else:
        status = "LOW_CONFIDENCE"    # Blocks Publication; Triggers Regeneration
        
    return QualityReport(
        composite_score=composite_score,
        comet_score=comet_score,
        status=status,
        warnings=missing_terms
    )
```

---

## 5. Web Frontend Software Architecture (`apps/web-frontend/`)

### 5.1 State Management Hierarchy
| State Category | Management Solution | Intended Use Case |
|---|---|---|
| **Server State** | **TanStack Query v5** | Caching, invalidating, and fetching curriculum, lessons, and analytics. |
| **Transient UI State** | **Zustand** | Modals, active audio player state, drawer navigation, and filter bars. |
| **Form State** | **React Hook Form + Zod** | Lesson prompt creation, worksheet question editor, validation schemas. |
| **Stream State** | **Native WebSockets / SSE** | Real-time voice translation visualizer and streaming generation tokens. |

### 5.2 Component Seams & Contract Integration
- Frontend components strictly consume TypeScript models generated by `@bhashasetu/contracts`.
- Mock Service Worker (MSW v2) intercepts all `/api/v1/*` HTTP calls during local development, enabling 100% offline front-end engineering.

---

## 6. Mobile Software Architecture (`apps/mobile/` & `app/`)

### 6.1 Clean Architecture Layering
```text
┌────────────────────────────────────────────────────────┐
│ UI Layer (Jetpack Compose / Flutter Widgets)           │
│ - LessonStudioScreen, VoiceTranslateScreen, QuizScreen │
└───────────────────────────┬────────────────────────────┘
                            │ (Observes UI State Flow)
┌───────────────────────────▼────────────────────────────┐
│ Domain Layer (Use Cases & Business Logic)              │
│ - DeliverLessonUseCase, RecordAttemptUseCase           │
│ - LocalRagEmbeddingEngine, OutboxSyncUseCase           │
└───────────────────────────┬────────────────────────────┘
                            │ (Calls Repository Interfaces)
┌───────────────────────────▼────────────────────────────┐
│ Data Layer (Local SQLite & Remote API Sync)            │
│ - AppDatabase (Room / Drift), OutboxDao, LessonDao     │
│ - SyncWorker (WorkManager), Retrofit/Dio Remote Client │
└────────────────────────────────────────────────────────┘
```

---

## 7. Testing Architecture & Verification Seams

```text
               ▲
              / \
             /   \
            / E2E \       Playwright Web Flows + Android Espresso Offline Tests
           /-------\
          /  Integ  \     NestJS Supertest API + FastAPI AI Pipeline Tests
         /-----------\
        /  Contract   \   Pact / OpenAPI 3.1 DTO Schema Validation
       /---------------\
      /   Unit Tests    \ Jest / Vitest / JUnit (Use cases, RLS, Sync Logic)
     /-------------------\
```

---

## 8. Definition of Done (DoD) for Software Modules
1. **Separation of Concerns:** Business logic strictly contained in Use-Case orchestrators.
2. **Type Safety:** 100% strict TypeScript / Kotlin / Dart with zero `any` types.
3. **Database Integrity:** Transactions used for all multi-table mutations; RLS applied.
4. **Offline Resilience:** All mobile reads/writes succeed locally with zero network calls.
5. **Traceability:** Module ID documented in FTL matrix with passing automated test suite.
