# BHASHASETU AI (भाषासेतु) — MASTER PRODUCT REQUIREMENTS DOCUMENT (PRD)
**SIH Problem Statement:** SIH26042 | **Domain:** Mother-Tongue-Based Multilingual Education (MTB-MLE)  
**Target Region:** Jharkhand Primary Education (Grades 1–5; focus on Dumka, West Singhbhum, Khunti, Chaibasa, Pakur)  
**Target Languages:** Hindi → Santhali (Ol Chiki ᱥᱟᱱᱛᱟᱲᱤ), Ho (Warang Chiti ᱦᱳ / Devanagari), Mundari (Devanagari मुण्डारी)  
**Document Version:** 3.0.0-PROD | **Status:** Approved / Execution Baseline  
**Classification:** Production-Oriented Hackathon-Feasible Engineering Specification

---

## PART 1 — Product Definition & Executive Summary

### 1.1 Executive Summary
**BhashaSetu AI (भाषासेतु)** is an offline-first, Mother-Tongue-Based Multilingual Education (MTB-MLE) scaffolding, translation, and pedagogical delivery ecosystem. It bridges the severe linguistic barrier encountered by non-native, Hindi-speaking primary school teachers instructing tribal students in Jharkhand whose primary mother tongues are **Santhali**, **Ho**, or **Mundari**.

By synthesizing curriculum-grounded Hybrid Retrieval-Augmented Generation (RAG), bidirectional live voice translation (target latency $\le 3.0\text{s}$), culturally situated pedagogical adaptation, and a durable local-first outbox synchronization engine, BhashaSetu AI empowers teachers to deliver NIPUN Bharat foundational literacy and numeracy (FLN) lessons without requiring prior personal fluency in the target tribal languages.

### 1.2 Problem Statement & Field Context (SIH26042)
In rural government schools across Jharkhand:
1. **The Language Mismatch:** Over 80% of entering Grade 1 students in tribal blocks speak exclusively Austroasiatic languages (Mundari, Ho, Santhali). However, state-prescribed learning materials and teachers use standard Hindi.
2. **Pedagogical Failure:** Children unable to comprehend classroom instruction suffer severe learning deficits in early childhood, resulting in high dropout rates and sub-30% Grade 3 FLN attainment.
3. **Severe Operational Constraints:** Remote single-teacher schools face zero or intermittent 2G/3G connectivity, erratic power, and rely on low-cost government-issued Android tablets ($\sim 2\text{ GB}$ RAM, Android 9+).
4. **Low-Resource Language Challenges:** Tribal languages lack rich digital corpora, standardized online MT APIs, and commercial ASR/TTS models. Direct literal translations often corrupt pedagogical intent and cultural meaning.

### 1.3 Core Product Promise
```text
Hindi Teacher (Non-native Speaker)
    │
    ▼
Curriculum-Grounded Understanding (JCERT/NCERT Learning Outcomes)
    │
    ▼
Language Translation & Native Transliteration (Ol Chiki / Warang Chiti / Devanagari)
    │
    ▼
Pedagogical & Cultural Adaptation (Local Analogies: Sarhul, Karam, Sohrai)
    │
    ▼
Human-in-the-Loop (HITL) Teacher Review & Approval
    │
    ▼
Voice + Text + Visual Classroom Delivery (High-Fidelity TTS, Bilingual Worksheets, Visual Flashcards)
    │
    ▼
Offline Learner Interaction & Interactive Practice
    │
    ▼
Local Assessment Storage (Append-Only SQLite Room / Drift DB)
    │
    ▼
Durable Outbox Synchronization (UUID Idempotent Sync with Gzip Compression)
    │
    ▼
Verified Cloud Learning Record & School Analytics (PostgreSQL 18 / Redis / Web Admin)
```

---

## PART 2 — Target Users, Personas & Jobs-to-Be-Done (JTBD)

### 2.1 User Personas Matrix
| Persona ID | Name & Role | Primary Device | Network Environment | Core Goal | Frustration / Failure Barrier |
|---|---|---|---|---|---|
| **PER-01** | **Ramesh Kumar**<br>*(Primary Teacher, Class 1–3)* | Low-cost Android Tablet (2GB RAM, Android 9+) | Intermittent 2G / 100% Offline in classroom | Teach Grade 2 Math & EVS in Ho/Santhali without speaking the language | Children stare blankly at Hindi textbooks; disengagement leads to absenteeism |
| **PER-02** | **Birsa Hembrom**<br>*(Grade 2 Tribal Student)* | Shared Classroom Tablet / Printable Worksheet | Zero connectivity in remote hamlet school | Understand counting, nature, and hygiene in his mother tongue (Santhali) | Feels alienated and intimidated by Hindi-only instruction |
| **PER-03** | **Dr. Sunita Soren**<br>*(Native Language Reviewer / Linguist)* | Web Portal (Desktop Chrome, High-speed) | Broadband / 4G LTE | Validate lexical accuracy, Ol Chiki script rendering, and cultural analogies | Existing generic commercial AI hallucinates and disrespects tribal idioms |
| **PER-04** | **Amitabh Das**<br>*(District Block Education Officer)* | Web Admin Console (Desktop/Tablet) | Stable 4G Office | Track school-level NIPUN Bharat FLN milestones and device sync health | Zero data visibility into single-teacher remote schools |
| **PER-05** | **Field Device Operator**<br>*(CRC Coordinator)* | Android Field App / USB OTG Pack | Mobile 3G/4G | Batch-update offline content packages and inspect tablet database health | Large video/model downloads fail over poor rural connectivity |

### 2.2 Jobs-to-Be-Done (JTBD) Framework
- **JTBD-01 (Teacher Delivery):** *When* I am introducing a new math concept (e.g., grouping in tens) to Grade 1 students who only speak Ho, *I want to* enter the Hindi lesson objective and receive culturally situated Ho audio, Ol Chiki/Warang Chiti visual aids, and phonetic transliterations, *so that* my students immediately understand without feeling linguistic alienation.
- **JTBD-02 (Live Assistance):** *When* a student asks a spontaneous question in Santhali during class, *I want to* speak a Hindi response into my tablet and have it play clear Santhali speech in under 3 seconds, *so that* classroom dialogue remains fluid.
- **JTBD-03 (Offline Practice):** *When* teaching in a disconnected village school, *I want to* run interactive quizzes on the tablet and print bilingual worksheets locally, *so that* learning continues seamlessly without internet.
- **JTBD-04 (Durable Record):** *When* I visit the block headquarters with 4G coverage, *I want* my tablet to silently and reliably upload all offline student progress without duplicate records or data loss.

---

## PART 3 — Non-Negotiable Product Requirements Matrix

| Req ID | Title | Priority | Core Description | Source Authority |
|---|---|---|---|---|
| **REQ-01** | **Hindi $\to$ Tribal Language Translation** | **P0** | High-accuracy translation of FLN curriculum content into Santhali (Ol Chiki), Ho (Warang Chiti/Devanagari), and Mundari (Devanagari). | Level 1 (SIH) |
| **REQ-02** | **Pedagogical & Cultural Adaptation** | **P0** | Contextualize explanations with local tribal references (e.g., Sarhul trees, Sohrai paintings, weekly Haat markets) while preserving core learning objectives. | Level 2 (Architecture) |
| **REQ-03** | **Native Script & Transliteration** | **P0** | Full dual rendering in native scripts (Ol Chiki ᱚᱞ ᱪᱤᱠᱤ, Warang Chiti ᱣᱟᱨᱟᱝ ᱪᱤᱛᱤ) alongside phonetic Devanagari and Latin transliteration. | Level 2 (Architecture) |
| **REQ-04** | **Target-Language Speech Synthesis (TTS)** | **P0** | Natural, intelligible, phonetically accurate acoustic speech generation for classroom playback. | Level 1 (SIH) |
| **REQ-05** | **Bidirectional Live Voice Dialogue** | **P0** | End-to-end voice-to-voice translation pipeline with a target latency budget $\le 3000\text{ ms}$. | Level 1 (SIH) |
| **REQ-06** | **Bilingual Printable Worksheets** | **P0** | Automated generation of printable/exportable PDF worksheets featuring dual-language prompts, MCQs, and visual placeholders. | Level 1 (SIH) |
| **REQ-07** | **Visual Multimodal Flashcards** | **P0** | Interactive cards with native tribal vocabulary, cultural illustrations, audio pronunciation triggers, and phonetic guides. | Level 1 (SIH) |
| **REQ-08** | **NIPUN Bharat / JCERT Alignment** | **P0** | Explicit grounding in state curriculum codes, competencies, and learning outcomes (LO codes). | Level 1 (SIH) |
| **REQ-09** | **Human-in-the-Loop (HITL) Teacher Review** | **P0** | Multi-signal validation interface allowing teachers to edit, approve, or reject AI outputs before publishing. | Level 2 (Architecture) |
| **REQ-10** | **100% Offline Classroom Operation** | **P0** | Complete lesson browsing, audio playback, interactive quizzes, and local progress recording without active internet. | Level 1 (SIH) |
| **REQ-11** | **Durable Outbox & Low-Bandwidth Sync** | **P0** | Resilient sync engine using UUID idempotency, delta sync cursors, and gzip compression over intermittent 2G/3G links. | Level 2 (Architecture) |
| **REQ-12** | **Hardware-Aware Adaptive Execution** | **P0** | Runtime resource inspector adjusting model execution between Edge (quantized), Local LAN, and Cloud based on tablet RAM ($\ge 2\text{GB}$), thermal state, and battery. | Level 2 (Architecture) |
| **REQ-13** | **Full Provenance & Audit Logging** | **P0** | Cryptographically verifiable provenance tracking chunk IDs, similarity scores, prompt templates, and reviewer actions. | Level 2 (Architecture) |
| **REQ-14** | **Multi-Language Extensibility** | **P1** | Abstract `LanguageProvider` architecture enabling rapid plug-in of additional languages (e.g., Kharia, Kurukh). | Level 3 (Design) |
| **REQ-15** | **Automated MT Quality Estimation (QE)** | **P0** | Multi-metric quality gating incorporating COMETKiwi, XCOMET error spans, terminology checks, and fallback flags. | Level 4 (Research) |
| **REQ-16** | **Multi-Tenant RBAC Security** | **P0** | Strict tenant isolation by state, district, block, and school using PostgreSQL Row-Level Security (RLS) and cryptographic sessions. | Level 3 (Standard) |

---

## PART 4 — Multilingual RAG, Language Intelligence & Quality Governance

### 4.1 Hybrid RAG Retrieval Engine
To guarantee factual curriculum fidelity and eliminate generative hallucinations, all lesson generation passes through a hybrid retrieval pipeline:
```text
User Query (Hindi Lesson Objective / Teacher Prompt)
       │
       ▼
Metadata Filter (State: Jharkhand, Grade: 1-5, Subject: FLN/Math/EVS, Chapter, LO Code)
       │
  ┌────┴──────────────────────────┐
  ▼                               ▼
Lexical BM25 Retrieval     Dense Multilingual Vector (BGE-M3 / LaBSE)
  │                               │
  └────┬──────────────────────────┘
       ▼
Reciprocal Rank Fusion (RRF) & Cross-Encoder Reranking
       │
       ▼
Curriculum Evidence Chunk Set + Provenance Metadata
       │
       ▼
Generative LLM (Gemini 3.1 Pro / Gemini 3.5 Flash / Claude 3.7 / Local Quantized Model)
       │
       ▼
Grounding & Hallucination Guardrail Check
```

### 4.2 Language Capability Matrix
| Language | ISO / Code | Primary Script | Fallback Script | ASR Maturity | MT Strategy | TTS Architecture | Benchmark Status |
|---|---|---|---|---|---|---|---|
| **Hindi** | `hin_Deva` | Devanagari | Latin | High (Whisper / Bhashini) | Native Pivot | High (Kokoro / VITS / Bhashini) | **VALIDATED** |
| **Santhali** | `sat_Olck` | Ol Chiki (ᱚᱞ ᱪᱤᱠᱤ) | Devanagari / Latin | Medium (Bhashini ASR / Fine-tuned Whisper) | NLLB-200 / Bhashini / Gemini with Glossary Constraints | FastPitch / VITS / Bhashini | **VALIDATED (Prototype Baseline)** |
| **Ho** | `hoc_Wara` | Warang Chiti (ᱣᱟᱨᱟᱝ ᱪᱤᱛᱤ) | Devanagari | Partial (Acoustic fine-tune) | Few-shot LLM + Dictionary Grounding + Bhashini | Formant / VITS | **PARTIAL (Benchmark Required)** |
| **Mundari** | `unr_Deva` | Devanagari (मुण्डारी) | Latin | Partial (Acoustic fine-tune) | Few-shot LLM + JCERT Glossary + NLLB | VITS / Local WaveNet | **PARTIAL (Benchmark Required)** |

### 4.3 Pedagogical Adaptation vs. Translation
**Core Invariant:** *Translation $\neq$ Pedagogy.* Literal machine translation fails primary classroom needs.
The adaptation pipeline executes five discrete transformations:
1. **Lexical Simplification:** Converts abstract terminology into age-appropriate foundational words.
2. **Cultural Situating:** Injects localized analogies (e.g., replaces apples with Mahua flowers or Sal seeds; replaces urban market scenes with weekly tribal Haats).
3. **Phonetic Scaffolding:** Emits pronunciation aids for Hindi-speaking teachers.
4. **Interactive Prompting:** Generates call-and-response chants and visual matching games.
5. **Learning Outcome Invariance:** Enforces that the core mathematical/scientific concept remains mathematically and factually identical to the JCERT textbook.

### 4.4 Automated Quality Estimation & Governance Gate
```text
Generated Translation & Pedagogical Output
                  │
                  ▼
   1. Terminology Compliance Check (JCERT Glossary)
                  │
                  ▼
   2. Curriculum Grounding Verification (Evidence Overlap)
                  │
                  ▼
   3. Reference-Free QE (COMETKiwi / XCOMET Token Error Spans)
                  │
                  ▼
   ┌───────────────────────────────┐
   │ Multi-Signal Governance Policy │
   └──────────────┬────────────────┘
                  │
     ┌────────────┼────────────┐
     ▼            ▼            ▼
[Score >= 0.85] [0.70 - 0.84] [Score < 0.70 or Critical Error]
High Confidence  Medium Conf.     Low Confidence / Rejected
     │            │            │
     ▼            ▼            ▼
Auto-Publish  Teacher Review   Block & Regenerate
Candidate     Mandatory        with Fallback Prompt
```

---

## PART 5 — Latency Budget & System SLA Targets

### 5.1 Voice-to-Voice Latency Budget (Target $\le 3000\text{ ms}$)
| Pipeline Stage | Subsystem / Component | Target P50 Budget | Upper Bound P95 Budget | Fallback Strategy |
|---|---|---|---|---|
| 1 | Voice Activity Detection (Silero VAD) | $100\text{ ms}$ | $150\text{ ms}$ | Fixed energy thresholding |
| 2 | Automatic Speech Recognition (Whisper / Bhashini) | $600\text{ ms}$ | $850\text{ ms}$ | On-device quantized tiny ASR |
| 3 | Translation & Pedagogical Adaptation | $500\text{ ms}$ | $800\text{ ms}$ | Translation memory lookup cache |
| 4 | Safety & Grounding Guardrail Check | $150\text{ ms}$ | $250\text{ ms}$ | Regex + heuristic filter |
| 5 | Text-to-Speech (TTS) Synthesis | $650\text{ ms}$ | $900\text{ ms}$ | Pre-cached audio chunks |
| 6 | Network Transport & Audio Buffer Playback | $200\text{ ms}$ | $350\text{ ms}$ | Stream chunked Opus audio |
| **Total** | **End-to-End Voice Interaction** | **$2200\text{ ms}$** | **$3300\text{ ms}$** | **Gist Audio Playback** |

*Note: All latency metrics represent engineering TARGETS. Claims of achieved performance require hardware-in-the-loop benchmarking on target Android tablets.*

---

## PART 6 — Offline-First Architecture & Durable Synchronization

### 6.1 Local Source of Truth
During classroom hours, the tablet's local SQLite database (managed via Room in Android / Drift in Flutter) operates as the **authoritative system of record**.

### 6.2 Durable Outbox Pattern
1. **Local Transaction:** When a teacher creates a lesson or a student completes a quiz, a record is written locally, and a synchronization task is atomically enqueued into the `outbox` table.
2. **Outbox Record Schema:**
   - `id`: UUID v4 primary key.
   - `operation_id`: Idempotency token (UUID).
   - `entity_type`: `LESSON` | `ASSESSMENT` | `ATTEMPT` | `REVIEW`.
   - `operation`: `CREATE` | `UPDATE` | `DELETE`.
   - `payload`: Encrypted/compressed JSON blob.
   - `sequence_no`: Monotonically increasing local sequence.
   - `status`: `PENDING` | `IN_FLIGHT` | `ACKNOWLEDGED` | `CONFLICT`.
   - `retry_count`: Integer with exponential backoff and jitter.
   - `created_at`: ISO 8601 timestamp.
3. **Reconciliation & Delta Pull:** When network connectivity is re-established, the sync worker executes an idempotent batch POST to `/api/v1/sync/push`, verifies server digital signatures, applies server-side deltas via `/api/v1/sync/pull`, and advances the local cursor.

---

## PART 7 — Security, Tenant Isolation & Responsible AI

### 7.1 Multi-Tenant Isolation
- **Row-Level Security (RLS):** All PostgreSQL tables enforce strict tenant separation by `school_id`, `block_id`, and `district_id`.
- **Identity & Session Management:** JWT access tokens with short TTL ($15\text{ mins}$) and secure HTTP-only refresh cookies; passwords hashed with Argon2id.

### 7.2 AI Safety & Untrusted Context Defense
- **Retrieval Isolation:** Text retrieved via RAG is treated as untrusted data input, wrapped in strict XML delimiters (`<curriculum_evidence>...</curriculum_evidence>`) to prevent prompt injection.
- **Least-Privilege Tool Execution:** The LLM cannot directly publish lessons, modify user permissions, or delete student records. All critical mutations require explicit authenticated teacher/admin confirmation.

---

## PART 8 — Monorepo Architecture & Subsystem Boundaries

```text
/bhashasetu
├── apps/
│   ├── web-frontend/           # Next.js 16.3 + React 19.2 (Teacher Studio, Admin, Linguist Review)
│   └── mobile/                 # Android Native (Kotlin Compose) / Flutter 3.x (Edge Offline Client)
├── services/
│   ├── web-backend/            # NestJS 11 + Node.js 22 LTS (Auth, RBAC, Domain Core, Sync)
│   └── ai-platform/            # FastAPI + Python 3.12 (Hybrid RAG, ASR, MT, TTS, Quality Gates)
├── packages/
│   ├── contracts/              # OpenAPI 3.1 Specs, Shared TypeScript & Dart DTOs
│   └── ui-kit/                 # Design System & Accessible Visual Primitives
├── infra/                      # Docker Compose, PostgreSQL 18, Redis 7.4, Nginx
└── docs/                       # PRD, TAD, SAD, FSD, FTL, Architecture Blueprints
```

---

## PART 9 — 16-Phase Living Execution Engine

| Phase ID | Phase Name | Primary Deliverables | Owner | Exit Gate Criteria |
|---|---|---|---|---|
| **Phase 0** | Problem Validation | Field survey synthesis, JCERT curriculum samples, Santhali/Ho glossaries | Research Lead | SIH problem requirements verified |
| **Phase 1** | Product Definition | Living PRD, user journeys, JTBD, persona approval | Product/UX Lead | Complete PRD approved |
| **Phase 2** | System Contracts | OpenAPI 3.1 specs, JSON schemas, shared TS/Dart DTOs | Team Lead | Zero type drift between apps |
| **Phase 3** | Web Foundation | Next.js 16.3 App Router shell, Tailwind v4, Radix UI components, MSW mocks | Frontend Lead | Web frontend builds and runs offline with MSW |
| **Phase 4** | Backend Foundation | NestJS 11 modular monolith, PostgreSQL 18 RLS, Argon2id auth, Redis queues | Full-Stack Lead | Auth, RBAC, and curriculum CRUD passing tests |
| **Phase 5** | Mobile Shell | Android Compose / Flutter offline shell, SQLite Room DB, UI screens | Mobile Lead | Offline UI renders without crash on 2GB RAM |
| **Phase 6** | Curriculum Ingestion | JCERT Grades 1–5 FLN textbook parser, chunking, and metadata tagging | AI/Data Lead | 100% of target curriculum chapters indexed |
| **Phase 7** | RAG & Translation | BGE-M3 hybrid retrieval, NLLB/Gemini MT, Ol Chiki transliteration | AI/Data Lead | Recall@5 $\ge 0.85$, Santhali MT verified by linguist |
| **Phase 8** | Pedagogical Gen | Contextual adaptation engine, worksheet generator, flashcard builder | AI/Data Lead | Cultural analogies generated; LO preserved |
| **Phase 9** | Voice-to-Voice | Silero VAD, Whisper ASR, Kokoro/Bhashini TTS streaming pipeline | AI/Data Lead | P50 latency $\le 2500\text{ms}$ on dev environment |
| **Phase 10** | Durable Sync | Outbox reconciliation engine, UUID idempotency, conflict resolution | Full-Stack Lead | 100% data recovery across network drop tests |
| **Phase 11** | Security & Gov | RLS policies, prompt injection defenses, HITL approval workflow | Team Lead | Zero unauthorized cross-tenant data leakage |
| **Phase 12** | Testing & Bench | Unit, contract, E2E, offline fault injection, latency benchmarks | QA Lead | All test suites green; benchmarks recorded |
| **Phase 13** | Deployment | Docker Compose, k8s configs, CI/CD pipeline, staging environment | DevOps Lead | Single-command container deployment passing |
| **Phase 14** | SIH Demonstration | 8-step live vertical slice demo script, backup offline videos | Team Lead | Complete end-to-end demo functional |
| **Phase 15** | Pilot Readiness | District rollout plan, teacher training guide, telemetry dashboards | Research Lead | Ready for field deployment in Dumka/Khunti |

---

## PART 10 — SIH 2026 Live Demonstration Strategy

### 10.1 The 8-Step Vertical Demonstration Slice
The SIH jury presentation executes a complete, unbroken end-to-end workflow:
1. **Teacher Speech Input:** Hindi teacher speaks: *"आज हम स्थानीय पेड़ों और पत्तियों के बारे में सीखेंगे।"*
2. **Hybrid RAG Grounding:** System retrieves JCERT Grade 2 EVS Chapter 3 curriculum context with provenance.
3. **Pedagogical Adaptation:** Generates Santhali explanation with Ol Chiki script (ᱚᱞ ᱪᱤᱠᱤ) + phonetic transliteration + Sarhul tree analogy.
4. **Teacher HITL Approval:** Teacher previews audio, verifies COMET score ($0.91$), approves publication.
5. **Offline Delivery in Airplane Mode:** Tablet switched to Airplane Mode; opens lesson, plays native Santhali TTS audio offline.
6. **Formative Student Quiz:** Student completes 3 interactive visual questions; score saved to local SQLite outbox.
7. **Reconnection & Outbox Sync:** Airplane mode disabled; sync worker pushes attempt with UUID idempotency key to Cloud backend.
8. **Live Admin Dashboard:** Web admin instantly shows updated school FLN progress and sync health metric.

---

## PART 11 — Definition of Done (DoD)

An architectural component or functional feature is declared **DONE** if and only if:
- [x] Traceable requirement ID mapped across PRD $\to$ TAD $\to$ SAD $\to$ FSD $\to$ FTL.
- [x] Clean architecture boundaries maintained (Zero direct DB access from frontend).
- [x] OpenAPI 3.1 schema defined and exported to shared contracts package.
- [x] 100% unit and integration test coverage for core business invariants.
- [x] Offline classroom behavior verified with network fault injection.
- [x] Security reviewed (RLS enabled, input sanitized, prompt injection guarded).
- [x] Benchmarks measured and recorded on target hardware ($\sim 2\text{GB}$ Android tablet).
- [x] Verified and demonstrable in the 8-step SIH demo pipeline.
