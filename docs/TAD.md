# BHASHASETU AI (भाषासेतु) — TECHNICAL ARCHITECTURE DOCUMENT (TAD)
**SIH Problem Statement:** SIH26042 | **Domain:** Mother-Tongue-Based Multilingual Education (MTB-MLE)  
**Target Deployment:** Web Platform (Desktop/Admin) + Android Field Edge App + AI/ML Microservice  
**Target Hardware Constraints:** Android 9+, ARM64, $\sim 2\text{ GB}$ RAM, Intermittent 2G/Offline  
**Document Version:** 3.0.0-PROD | **Status:** Approved System Architecture  
**Classification:** Production-Oriented Hackathon-Feasible Architecture Control Document

---

## 1. System Context & High-Level Architecture Blueprint

```text
                                   BHASHASETU ECOSYSTEM
                                            │
         ┌──────────────────────────────────┴──────────────────────────────────┐
         │                                                                     │
    WEB PLATFORM                                                         MOBILE CLIENT
  (Next.js 16.3 + React 19.2)                                      (Android Kotlin Compose / Flutter 3.x)
  [Teacher Studio, Admin, Linguist Review]                         [Offline Classroom, Voice, Quizzes]
         │                                                                     │
         │ (REST / SSE / WebSocket)                                            │ (Sync Outbox / Idempotent REST)
         ▼                                                                     ▼
  ┌───────────────────────────────────────────────────────────────────────────────┐
  │                           API GATEWAY & DOMAIN CORE                           │
  │                         (NestJS 11 + Node.js 22 LTS)                          │
  │  - Auth (Argon2id, JWT, OAuth)                - Tenant / School RLS Scoping   │
  │  - Curriculum & Lesson State Machines         - Outbox Sync Reconciliation    │
  │  - OpenAPI 3.1 Contract Engine                - OpenTelemetry Instrumentation │
  └──────────────────────┬────────────────────────────────┬───────────────────────┘
                         │                                │
         ┌───────────────┴────────────────┐               │ (gRPC / HTTP/2)
         ▼                                ▼               ▼
  ┌────────────────────────┐   ┌────────────────────┐   ┌───────────────────────────┐
  │  PRIMARY DATABASE      │   │ CACHE & QUEUES     │   │ AI / ML INFERENCE ENGINE  │
  │  PostgreSQL 18         │   │ Redis 7.4 / BullMQ │   │ FastAPI + Python 3.12     │
  │  - pgvector + DiskANN  │   │ - Async Packaging  │   │ - BGE-M3 / LaBSE RAG      │
  │  - RLS Multi-Tenancy   │   │ - AI Task Queue    │   │ - Whisper / Bhashini ASR  │
  │  - Immutable Audits    │   │ - Rate Limiting    │   │ - NLLB / Gemini MT        │
  └────────────────────────┘   └────────────────────┘   │ - XCOMET / MQM Quality    │
                                                        └───────────────────────────┘
```

---

## 2. Architecture Principles (P01–P18)

- **P01 — Domain-First Architecture:** Business rules reside strictly within isolated use-case orchestrators and domain policies; HTTP controllers are pure transport adapters.
- **P02 — Contract-First Integration:** OpenAPI 3.1 serves as the absolute single source of truth, generating TypeScript and Dart DTOs to eliminate type drift.
- **P03 — Strict Frontend/Backend Separation:** The frontend never connects directly to databases, holds provider credentials, or makes autonomous authorization decisions.
- **P04 — Local-First Field Operation:** Mobile writes to local SQLite (Room/Drift) immediately; network access is an asynchronous opportunistic synchronization layer.
- **P05 — Durable Outbox Synchronization:** All outbound actions are queued with UUID idempotency keys and sequence numbers to guarantee zero data duplication across flaky 2G/3G links.
- **P06 — Human-in-the-Loop Governance:** AI-generated educational content cannot be published without teacher review and explicit approval.
- **P07 — Hardware-Aware Dynamic Execution:** The system inspects client RAM, battery, thermal state, and network status to route tasks intelligently (Edge $\leftrightarrow$ LAN $\leftrightarrow$ Cloud).
- **P08 — Provider Abstraction:** Abstract `LanguageProvider` interfaces decouple domain code from specific vendors (e.g., Bhashini, Gemini, Hugging Face).
- **P09 — Language Capability Matrix:** Never assume equal AI maturity across all tribal languages; capabilities are discovered via a capability registry.
- **P10 — Hybrid RAG Grounding:** Combine lexical BM25 with dense multilingual embeddings (BGE-M3) and reciprocal rank fusion to prevent hallucinations.
- **P11 — Defense-in-Depth Security:** Treat all RAG retrieved chunks as untrusted user input; isolate LLM prompt boundaries with strict XML tagging.
- **P12 — Observable Operations:** End-to-end distributed tracing via OpenTelemetry across every API request, RAG query, and sync transaction.
- **P13 — Deterministic Assessment:** Formative student evaluations must be deterministically scored on-device with zero server latency.
- **P14 — Measured Performance:** Latency and memory targets are strictly validated on target hardware ($\sim 2\text{GB}$ Android tablets) before declaring compliance.
- **P15 — Progressive Complexity:** Start with a robust modular monolith; extract independent microservices only when justified by scaling bottlenecks.
- **P16 — Immutable Publishing:** Published curriculum versions are strictly append-only and cryptographically signed.
- **P17 — Zero Silent Data Loss:** Conflicting sync operations branch into versioned drafts rather than suffering destructive overwrites.
- **P18 — Evidence Before Claims:** Architectural readiness requires verifiable test reports, logs, and live benchmarks.

---

## 3. Architecture Tiers

### Tier 1 — Experience (Presentation)
- **Web Frontend (`apps/web-frontend/`):** Next.js 16.3 (App Router), React 19.2, Tailwind CSS v4, Radix UI accessible primitives, TanStack Query v5, Zustand.
- **Mobile Client (`apps/mobile/` & `app/`):** Android Native (Kotlin Jetpack Compose) / Flutter 3.x, Room / SQLite 3, Silero VAD, ExoPlayer.

### Tier 2 — Application & Gateway
- **NestJS 11 Gateway (`services/web-backend/`):** OpenAPI 3.1 Swagger docs, JWT / Argon2id authentication, rate limiting, and BullMQ job dispatchers.

### Tier 3 — Education Domain Core
- **Domain Modules:** `curriculum`, `lessons`, `assessments`, `reviews`, `devices`, and `sync_reconciliation`.

### Tier 4 — Intelligence & Language Services
- **FastAPI AI Engine (`services/ai-platform/`):** Hybrid RAG, BGE-M3 embeddings, Whisper/Bhashini ASR, NLLB/Gemini MT, Kokoro/Piper TTS, COMETKiwi QE.

### Tier 5 — Persistence & Caching
- **PostgreSQL 18:** System of record with Row-Level Security (RLS) and pgvector/StreamingDiskANN indexing.
- **Redis 7.4:** Session store, distributed cache, and BullMQ background task queue.
- **S3 / GCS Storage:** Signed offline content packs, audio snippets, and visual worksheets.

### Tier 6 — Edge Runtime
- **On-Device SQLite:** Local curriculum packs, append-only student attempt logs, durable outbox queue.
- **Local Embedding Matcher:** On-device cosine similarity & BM25 for offline keyword matching.

### Tier 7 — Operations & Observability
- **OpenTelemetry & Prometheus:** Metrics, traces, correlated structured logging, and health checks.

---

## 4. Subsystem Specifications

### 4.1 Web Frontend Architecture (`apps/web-frontend/`)
```text
apps/web-frontend/
├── app/                        # Next.js 16.3 App Router pages & server layouts
│   ├── (auth)/login/
│   ├── (dashboard)/lessons/
│   └── (dashboard)/admin/
├── features/                   # Domain-driven feature packages
│   ├── lesson-studio/          # Multi-step creation, RAG review, and Ol Chiki canvas
│   ├── voice-dialogue/         # Real-time audio streaming visualizer
│   └── analytics/              # FLN progress charts & sync health maps
├── components/ui/              # Radix UI + Tailwind accessible primitives
├── lib/api/                    # TanStack Query hooks generated from OpenAPI 3.1
└── stores/                     # Zustand stores for transient UI states
```

### 4.2 Web Backend Architecture (`services/web-backend/`)
```text
services/web-backend/
├── src/
│   ├── auth/                   # JWT, Argon2id, Session Guards, RBAC Decorators
│   ├── curriculum/             # JCERT/NCERT hierarchy, Learning Outcomes, Competencies
│   ├── lessons/                # Lesson State Machine, HITL Review, Versioning
│   ├── sync/                   # Outbox Reconciliation, Delta Generator, Conflict Resolver
│   ├── ai-orchestration/       # gRPC / HTTP client dispatching to AI Platform
│   └── common/                 # RLS Interceptors, Audit Loggers, OpenTelemetry Tracing
```

### 4.3 AI Platform Architecture (`services/ai-platform/`)
```text
services/ai-platform/
├── rag/                        # Chunking, BGE-M3 Dense Indexing, BM25, RRF, Cross-Encoder
├── translation/                # NLLB-200, Bhashini Adapter, Gemini 3.1/3.5 Pro with Glossaries
├── voice/                      # Silero VAD, Whisper ASR, Kokoro-82M / Piper TTS Engine
├── quality/                    # COMETKiwi Reference-Free QE, XCOMET MQM Error Spans
└── pedagogy/                   # Local cultural analogy injector, Grade-level text simplifier
```

---

## 5. Storage, Vector Search & Retrieval Architecture

### 5.1 Hybrid Retrieval Pipeline (BM25 + BGE-M3 + DiskANN)
1. **Metadata Filtering:** Queries are hard-scoped by `state_id = 'JH'`, `grade IN (1..5)`, `subject = 'FLN'`, and `curriculum_version = 'JCERT_2025'`.
2. **Dense Vector Search:** Embeddings generated via `BAAI/bge-m3` (1024-dimensional dense vectors).
3. **StreamingDiskANN Optimization (pgvectorscale):**
   - Employs DiskANN graph indexing on SSD storage, reducing RAM consumption by $\sim 10\times$ compared to standard HNSW ($21\text{ MB}$ index vs $193\text{ MB}$ for $25\text{k}$ vectors).
   - Achieves $3\text{--}6\text{ ms}$ P95 query latency while maintaining Recall@10 $> 0.95$.
4. **Reciprocal Rank Fusion (RRF):** Fuses lexical BM25 and vector results:
   $$\text{RRF\_Score}(d) = \sum_{m \in \{\text{BM25}, \text{Dense}\}} \frac{1}{k + \text{Rank}_m(d)} \quad (k=60)$$

---

## 6. Voice-to-Voice Streaming Architecture & Latency Budget

```text
Teacher Speaks (Hindi)
         │
         ▼
[Silero VAD] (50-100ms) ──► [Whisper ASR / Bhashini] (400-800ms)
                                       │
                                       ▼ (Hindi Text)
                       [FastAPI Language Engine]
                       - Glossary Lookup (50ms)
                       - NLLB / Gemini MT (300-600ms)
                       - Guardrail & Terminology Check (100ms)
                                       │
                                       ▼ (Target Language Text)
                       [Kokoro / Piper TTS] (500-800ms)
                                       │
                                       ▼ (Chunked Opus Audio)
                       [Mobile ExoPlayer] (Playback Start <= 3.0s)
```

---

## 7. Offline Synchronization & Conflict Resolution Engine

### 7.1 Sync Flow
1. **Client Outbox:** Local SQLite stores actions in `outbox` table with UUID `operation_id`.
2. **Push Phase (`POST /api/v1/sync/push`):**
   - Transmits batch of pending operations with gzip compression.
   - Backend processes each record inside an ACID transaction and applies tenant-specific conflict strategies.
3. **Pull Phase (`GET /api/v1/sync/pull?cursor=...`):**
   - Server returns verified lesson packages and curriculum updates created after `cursor`.
4. **Cursor Advancement:** The mobile client advances its local sync cursor only after local SQLite transactions succeed.

### 7.2 Conflict Resolution Policies
| Entity Type | Conflict Scenario | Applied Policy | Justification |
|---|---|---|---|
| **Published Lesson** | Concurrent edits on different devices | **IMMUTABLE** | Published versions are immutable; creates Version $N+1$ |
| **Student Attempt** | Multiple submissions for same quiz | **APPEND-ONLY** | Every attempt is preserved with unique UUID for historical evaluation |
| **Teacher Review** | Automated score vs. Teacher edit | **TEACHER_AUTHORITATIVE** | Human educator judgment strictly overrides algorithmic outputs |
| **Device Config** | School policy change vs. local setting | **SERVER_AUTHORITATIVE** | Central administrative safety policies strictly enforce compliance |

---

## 8. Security Architecture & Threat Model

```text
[Public Internet / Cellular]
            │
            ▼ (TLS 1.3 / Strict Content Security Policy)
  [API Gateway (NestJS)]
  - Rate Limiting (Redis Token Bucket)
  - CORS & Helmet Headers
  - JWT Access Token Verification (15m TTL)
            │
            ▼
  [Application Security Layer]
  - Argon2id Password Hashing
  - RBAC Permission Decorators (@Roles('TEACHER', 'ADMIN'))
  - PostgreSQL Row-Level Security (SET LOCAL app.current_school_id = '...')
            │
            ▼
  [AI Subsystem Security Boundary]
  - Untrusted Context XML Isolation (<curriculum_evidence>...</curriculum_evidence>)
  - Tool Invocation Allowlist (Strict Read-Only for RAG)
  - Output Sanitization & Content Filters
```

---

## 9. Architectural Decision Records (ADR Register)

| ADR ID | Title | Status | Decision & Core Rationale |
|---|---|---|---|
| **ADR-001** | Decoupled Polyglot Monorepo | **APPROVED** | Separate `apps/web-frontend`, `services/web-backend`, and `services/ai-platform` to allow independent framework lifecycles (React 19, NestJS 11, Python 3.12). |
| **ADR-002** | NestJS 11 as Gateway & Domain Core | **APPROVED** | Provides enterprise-grade TypeScript modular architecture, robust dependency injection, and native OpenAPI generation. |
| **ADR-003** | FastAPI for AI/ML Microservice | **APPROVED** | High-performance Python async runtime with native PyTorch, Hugging Face, and Pydantic v2 support. |
| **ADR-004** | PostgreSQL 18 with pgvector | **APPROVED** | Consolidates relational data of record and vector embeddings into a single ACID-compliant database, reducing operational footprint. |
| **ADR-005** | StreamingDiskANN for Vector Scaling | **APPROVED** | Delivers $10\times$ memory reduction over HNSW on SSDs while keeping P95 search latency under $6\text{ ms}$. |
| **ADR-006** | Local-First Android SQLite (Room/Drift) | **APPROVED** | Guarantees 100% classroom reliability in disconnected Jharkhand villages without blocking UI threads. |
| **ADR-007** | UUID Idempotent Outbox Sync | **APPROVED** | Eliminates duplicate student records and network replay failures across unstable 2G/3G connections. |
| **ADR-008** | Contract-First Integration via OpenAPI 3.1 | **APPROVED** | Single schema source of truth generates frontend TypeScript and mobile Dart types, preventing contract drift. |
| **ADR-009** | Redis 7.4 & BullMQ for Background Jobs | **APPROVED** | Lightweight, high-throughput asynchronous job queue for offline package building and heavy AI inference tasks. |
| **ADR-010** | Multilingual LanguageProvider Abstraction | **APPROVED** | Decouples business logic from specific translation engines, facilitating seamless integration of Bhashini, Gemini, and local models. |
