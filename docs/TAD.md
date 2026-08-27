# BHASHASETU AI — TECHNICAL ARCHITECTURE DOCUMENT (TAD)
**SIH Problem Statement:** SIH26042 | **Domain:** Mother-Tongue-Based Multilingual Education (MTB-MLE)  
**Document Version:** 2.0.0-PROD | **Status:** Approved Architecture

---

## 1. System Context & High-Level Architecture

```
                                  BHASHASETU ECOSYSTEM
                                           │
         ┌─────────────────────────────────┴─────────────────────────────────┐
         │                                                                   │
    WEB PLATFORM                                                       MOBILE CLIENT
  (Next.js 16.3 + React 19.2)                                    (Android Kotlin Compose / Flutter 3.x)
  [Teacher Studio, Admin, Linguist Review]                       [Offline Classroom, Voice, Quizzes]
         │                                                                   │
         │ (REST / SSE / WebSocket)                                          │ (Sync Outbox / Idempotent REST)
         ▼                                                                   ▼
  ┌─────────────────────────────────────────────────────────────────────────────┐
  │                           API GATEWAY & DOMAIN CORE                         │
  │                         (NestJS 11 + Node.js 22 LTS)                        │
  │  - Auth (Argon2id, JWT, OAuth)              - Tenant / School Scoping       │
  │  - Curriculum & Lesson State Machines       - Outbox Sync Reconciliation    │
  │  - OpenAPI 3.1 Contract Engine              - OpenTelemetry Instrumentation │
  └──────────────────────┬───────────────────────────────┬──────────────────────┘
                         │                               │
         ┌───────────────┴───────────────┐               │ (gRPC / HTTP/2)
         ▼                               ▼               ▼
  ┌────────────────────────┐   ┌───────────────────┐   ┌──────────────────────────┐
  │  PRIMARY DATABASE      │   │ CACHE & QUEUES    │   │ AI / ML INFERENCE ENGINE │
  │  PostgreSQL 18         │   │ Redis 7.4 / BullMQ│   │ FastAPI + Python 3.12    │
  │  - pgvector + DiskANN  │   │ - Async Packaging │   │ - BGE-M3 / LaBSE RAG     │
  │  - RLS Multi-Tenancy   │   │ - AI Task Queue   │   │ - Whisper / Bhashini ASR │
  │  - Immutable Audits    │   │ - Rate Limiting   │   │ - NLLB / Gemini MT       │
  └────────────────────────┘   └───────────────────┘   │ - XCOMET / MQM Quality   │
                                                       └──────────────────────────┘
```

---

## 2. Architectural Principles
- **P01 — Domain-First Architecture:** Business rules isolated in use-case orchestrators; controllers are strictly transport-layer.
- **P02 — Contract-First Integration:** OpenAPI 3.1 generates TypeScript & Dart DTOs, preventing contract drift.
- **P03 — Strict Frontend/Backend Separation:** Frontend never connects directly to PostgreSQL/Redis or manages secret keys.
- **P04 — Local-First Field Operation:** Mobile writes to SQLite Room/Drift immediately; network is an asynchronous optimization.
- **P05 — Durable Outbox Sync:** UUID idempotency keys prevent duplicate operations across intermittent connectivity.
- **P06 — Human-in-the-Loop Governance:** AI generated curriculum cannot publish without teacher/reviewer sign-off.
- **P07 — Hardware-Aware Dynamic Routing:** Dynamic execution routing based on client RAM, thermal, battery, and network conditions.

---

## 3. Storage & Vector Retrieval Architecture
- **Relational System of Record:** PostgreSQL 18 with Row-Level Security (RLS) enforcing `school_id` tenant isolation.
- **StreamingDiskANN (pgvectorscale):** Achieves 10x memory reduction over HNSW (21 MB index vs 193 MB for 25k embeddings) with 3–6 ms P95 query latency.
- **Object Storage:** S3-compatible storage (MinIO / GCS) for pre-rendered audio snippets, flashcard graphics, and signed offline sync bundles.
