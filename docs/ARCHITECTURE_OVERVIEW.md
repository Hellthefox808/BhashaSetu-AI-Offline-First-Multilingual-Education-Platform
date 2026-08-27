# BHASHASETU AI — FULL-STACK ARCHITECTURE & WORKFLOW BLUEPRINT
**SIH Problem Statement:** SIH26042 | **Domain:** Mother-Tongue-Based Multilingual Education (MTB-MLE)  
**Document Version:** 2.0.0-PROD | **Status:** Master Engineering Reference

---

## 1. Decoupled Polyglot Architecture

BhashaSetu AI implements a production-grade, decoupled polyglot architecture with strict separation of concerns across clients, application gateways, AI microservices, and edge runtimes:

```
WEB FRONTEND (Next.js 16.3 + React 19.2 + TS 5)
       │     (UI, state, caching, accessible Radix components)
       ▼
REST / SSE / WebSocket (OpenAPI 3.1 Contract)
       ▼
WEB BACKEND (NestJS 11, Node.js 22 LTS + TS)
       (Auth, RBAC, multi-tenancy, business logic, outbox sync)
       │
  Internal gRPC / HTTP (AI service)
       ▼
AI / ML PLATFORM (FastAPI + Python 3.12)
       (RAG retrieval, BGE-M3 embeddings, ASR, MT, TTS, pedagogy, XCOMET QE)
       │
       ▼
DATA & INFRASTRUCTURE
       (PostgreSQL 18 + pgvector/DiskANN, Redis 7.4 / BullMQ, S3 Object Storage)
```

---

## 2. Layer Specifications

### Web Frontend (apps/web-frontend/)
- **Next.js 16.3+ (App Router):** Instant Navigations for seamless SPA responsiveness, React Server Components for fast initial loads.
- **React 19.2 & TypeScript 5 Strict:** Modern hooks, strict type safety, zero `any` types.
- **TanStack Query v5 & Zustand:** Server-state caching and lightweight client UI state management.
- **Mock Service Worker (MSW v2):** Enables 100% offline front-end development against OpenAPI contracts.

### Mobile Edge Client (apps/mobile/ & app/)
- **Android Native (Kotlin Compose) / Flutter 3.x:** High-performance UI on low-cost Android tablets (Android 9+, 2GB RAM).
- **Offline-First Room / Drift SQLite 3:** Local-first storage ensures teachers can conduct lessons and assess students with zero network connectivity.
- **Durable Outbox & UUID Idempotency:** Sync transactions are queued locally with exponential backoff and jitter.

### Web Backend (services/web-backend/)
- **NestJS 11 on Node.js 22 LTS:** Scalable enterprise architecture with modular domains (auth, curriculum, lessons, sync).
- **OpenAPI 3.1 / Swagger:** Single source of truth generating client DTOs.
- **Multi-Tenant Row-Level Security:** Isolates school and district data strictly at the database level.

### AI / ML Microservice (services/ai-platform/)
- **FastAPI + Python 3.12:** High-throughput asynchronous endpoints with Pydantic v2 validation.
- **BGE-M3 Multilingual Embeddings:** Dense + sparse hybrid vector search across 100+ languages.
- **Streaming ASR & TTS:** Sub-3-second voice dialogue pipeline.
- **Unbabel COMET / XCOMET Quality Gate:** Reference-free MT quality estimation with MQM token-level error tagging.

### Data & Infrastructure
- **PostgreSQL 18 + StreamingDiskANN (pgvectorscale):** Ultra-efficient vector search (21MB index vs 193MB HNSW) with 3–6ms query latency.
- **Redis 7.4 + BullMQ:** Asynchronous job queues for offline package generation and AI batch processing.
