# BhashaSetu AI (भाषासेतु) — Offline-First Multilingual Education Platform
**SIH Problem Statement:** SIH26042 | **Domain:** Mother-Tongue-Based Multilingual Education (MTB-MLE)  
**Target Region:** Jharkhand Primary Schools (Grades 1–5; focus on Dumka, West Singhbhum, Khunti, Chaibasa, Pakur)  
**Target Languages:** Hindi → Santhali (Ol Chiki ᱥᱟᱱᱛᱟᱲᱤ), Ho (Warang Chiti ᱦᱳ), Mundari (Devanagari मुण्डारी)

> **Bridging the Classroom Language Barrier:** Empowering Hindi-speaking teachers and tribal students in Jharkhand with Mother-Tongue-Based Multilingual Education (MTB-MLE), local edge RAG, real-time voice translation, and AI pedagogical adaptations.

---

## 🌟 Overview

**BhashaSetu AI (भाषासेतु)** is a decoupled, offline-first multilingual educational scaffolding and delivery ecosystem designed for primary school teachers (Grades 1–5) in tribal districts of Jharkhand. It bridges national/state curriculum standards (NCERT/JCERT, NIPUN Bharat FLN) with indigenous tribal languages:

- **Santhali (ᱥᱟᱱᱛᱟᱲᱤ)** — Native Ol Chiki & Devanagari/Latin transliteration
- **Ho (ᱦᱳ)** — Native Warang Chiti & Devanagari/Latin transliteration
- **Mundari (मुण्डारी)** — Devanagari & Nag Mundari/Latin transliteration

Designed with an **offline-first** architecture, BhashaSetu AI operates reliably in remote classrooms without internet connectivity, utilizing on-device SQLite/Room databases, dense vector embeddings, and BM25 hybrid indexing. When network connectivity is available, the platform synchronizes with Cloud Firestore / PostgreSQL and leverages AI models for pedagogical adaptations, multimodal learning, and real-time live voice translation.

---

## 🏗️ Polyglot Monorepo Architecture

```text
/bhashasetu
├── apps/
│   ├── web-frontend/           # Next.js 16.3 + React 19.2 (App Router, Radix UI, TanStack Query v5)
│   └── mobile/                 # Android Native (Kotlin Compose) / Flutter 3.x (Edge Offline Client)
├── services/
│   ├── web-backend/            # NestJS 11 + Node.js 22 LTS (Auth, RBAC, Domain Core, Sync)
│   └── ai-platform/            # FastAPI + Python 3.12 (Hybrid RAG, ASR, MT, TTS, XCOMET Quality Gates)
├── packages/
│   ├── contracts/              # OpenAPI 3.1 Schemas, Shared TypeScript & Dart DTOs
│   └── ui-kit/                 # Design System & Accessible Visual Primitives
├── infra/                      # Docker Compose, PostgreSQL 18 (pgvector/DiskANN), Redis 7.4, Nginx
└── docs/                       # PRD, TAD, SAD, FSD, FTL, Architecture Blueprints
```

---

## 🚀 Key Subsystems & Features

### 1. 📖 MTB-MLE Lesson Studio & Pedagogical Adaptation
- Scaffolds core Hindi concepts into tribal languages with cultural analogies (e.g., Sarhul, Sohrai, Karam festivals, indigenous flora/fauna).
- Formulates grade-appropriate explanations, bilingual classroom activities, and pronunciation guides.
- Automated quality estimation (COMETKiwi / XCOMET) and formative quiz generation.

### 2. ⚡ Offline-First Local Curriculum RAG
- Dual-mode retrieval engine combining **Dense Vector Cosine Similarity (BGE-M3)** and **BM25 Lexical Matching**.
- Preloaded with foundational FLN outcomes, vocabulary tables, and JCERT curriculum chunks.
- Sub-150ms on-device retrieval latency with zero cloud dependency during active teaching.

### 3. 🎙️ Live Voice Translation & TTS Engine
- Sub-3-second streaming conversational translation between Hindi and indigenous tribal languages.
- Transliteration display and phonetic pronunciation support to assist teachers in proper vocalization.

### 4. 📝 Offline Student Practice & Durable Outbox Sync
- Interactive student practice quizzes with instant visual feedback and audio reinforcement.
- Assessment attempts tracked locally and queued in an offline sync outbox (`UUID` idempotency keys) for automatic sync upon network availability.

### 5. 🛡️ Multi-Tenant Security & Human-in-the-Loop Governance
- Row-Level Security (RLS) enforcing tenant isolation at school and district levels.
- AI-generated educational content requires teacher review and sign-off before official publication.

---

## 📑 Living Master Documentation Suite

All architectural and engineering specifications are maintained in the [`docs/`](file:///d:/bhashasetu-ai/docs) directory:

- **[PRD.md](file:///d:/bhashasetu-ai/docs/PRD.md):** Product Requirements Document (17 parts, 50 sections, JTBD, personas, 25 core requirements).
- **[TAD.md](file:///d:/bhashasetu-ai/docs/TAD.md):** Technical Architecture Document (7 tiers, P01–P18 principles, hybrid vector search, DiskANN, ADR register).
- **[SAD.md](file:///d:/bhashasetu-ai/docs/SAD.md):** Software Architecture Document (Module boundaries, state machines, use cases, testing pyramid).
- **[FSD.md](file:///d:/bhashasetu-ai/docs/FSD.md):** Functional Specification Document (30 functional domains, UI 5-state contracts, acceptance criteria).
- **[FTL.md](file:///d:/bhashasetu-ai/docs/FTL.md):** Functional Traceability Ledger (100% verified traceability chain, SIH live demo map, evidence tiers E0–E5).
- **[ARCHITECTURE_OVERVIEW.md](file:///d:/bhashasetu-ai/docs/ARCHITECTURE_OVERVIEW.md):** Full-stack architecture and workflow blueprint.

---

## 🎬 SIH 2026 Live Demonstration Pipeline

The live evaluation executes the following verified 8-step vertical slice:
1. **Teacher Speech Input (Hindi):** Teacher speaks Hindi lesson intent into the tablet.
2. **Hybrid RAG Grounding:** System retrieves JCERT Grade 2 EVS curriculum context with provenance.
3. **Pedagogical Adaptation:** Generates Santhali explanation with Ol Chiki script (ᱚᱞ ᱪᱤᱠᱤ), phonetic transliteration, and Sarhul festival analogy.
4. **Teacher HITL Approval:** Teacher reviews audio preview and COMET quality score ($0.91$).
5. **Offline Classroom Delivery:** Tablet switched to Airplane Mode; plays native Santhali TTS audio 100% offline.
6. **Formative Student Quiz:** Student completes 3 interactive visual questions; attempt saved locally to SQLite outbox.
7. **Reconnection & Outbox Sync:** Airplane mode disabled; background worker executes idempotent batch push to cloud backend.
8. **Live Admin Dashboard:** Web admin instantly shows updated school FLN progress and sync health telemetry.

---

## 🚀 Getting Started

### 1. Docker Compose (Full Stack)
```bash
cd infra
docker-compose up --build
```
- **Web Frontend:** `http://localhost:3000`
- **Web Backend (NestJS):** `http://localhost:3001/api/v1`
- **AI Platform (FastAPI):** `http://localhost:8000/docs`
- **PostgreSQL 18 + pgvector:** `localhost:5432`
- **Redis:** `localhost:6379`

### 2. Android Edge Application
1. Open Android Studio (Ladybug 2024.2+).
2. Open `d:\bhashasetu-ai`.
3. Set your `GEMINI_API_KEY` in `.env`.
4. Build and run on target tablet or emulator (API 24+).

---

## 🔒 Security & Privacy
- **Zero Secret Leakage:** Keystores and `.env` files are strictly excluded from source control.
- **Offline Data Sovereignty:** Student assessments and teacher lesson drafts remain strictly on-device in encrypted SQLite/Room storage until explicitly synchronized by the teacher.

---

## 📄 License & Attribution
Developed for educational equity in multilingual tribal education under the **MIT License**.
