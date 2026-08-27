# BHASHASETU AI (भाषासेतु) — PRODUCT REQUIREMENTS DOCUMENT (PRD)
**SIH Problem Statement:** SIH26042 | **Domain:** Mother-Tongue-Based Multilingual Education (MTB-MLE)  
**Target Region:** Jharkhand Primary Schools (Grades 1–5)  
**Target Languages:** Hindi → Santhali (Ol Chiki), Ho (Warang Chiti), Mundari (Devanagari / Nag Mundari)  
**Document Version:** 2.0.0-PROD | **Status:** Approved / Execution Ready

---

## PART 1 — Product Definition & Executive Summary

### 1.1 Executive Summary
**BhashaSetu AI** is a specialized, offline-first multilingual educational scaffolding and delivery platform. It bridges the critical language barrier faced by non-tribal, Hindi-speaking primary school teachers instructing tribal children in Jharkhand whose mother tongues are **Santhali (ᱥᱟᱱᱛᱟᱲᱤ)**, **Ho (ᱦᱳ)**, or **Mundari (मुण्डारी)**. 

By combining on-device curriculum-grounded Retrieval-Augmented Generation (RAG), bidirectional live voice translation (<=3s target latency), context-aware pedagogical adaptation, and a durable offline-first outbox synchronization engine, BhashaSetu AI empowers teachers to deliver NIPUN Bharat foundational literacy and numeracy (FLN) lessons without requiring prior personal fluency in the tribal languages.

### 1.2 Problem Statement & Field Context (SIH26042)
In rural and tribal schools across Jharkhand (e.g., Dumka, West Singhbhum, Khunti, Chaibasa), standard textbooks and curricula are prescribed in Hindi. However, over 80% of entering Grade 1 students speak exclusively Austroasiatic languages (Mundari, Ho, Santhali). Teachers, predominantly fluent only in standard Hindi, struggle to convey foundational concepts. The resulting comprehension gap drives early childhood dropout rates and severe FLN deficits.

### 1.3 Core Product Promise
```
Hindi Teacher
    ↓
Curriculum-Grounded Understanding (JCERT/NCERT)
    ↓
Language Translation / Transliteration (Ol Chiki, Warang Chiti, Devanagari)
    ↓
Context-Aware Pedagogical Adaptation (Local Analogies: Sarhul, Karam, Sohrai)
    ↓
Teacher Verification & Human-in-the-Loop Approval
    ↓
Voice + Text + Visual Classroom Delivery (TTS, Worksheets, Flashcards)
    ↓
Offline Learner Interaction & Practice Quizzes
    ↓
Local Assessment Storage (SQLite Room / Drift)
    ↓
Durable Synchronization Outbox (UUID Idempotent Sync)
    ↓
Verified Cloud Learning Record (PostgreSQL / Firestore)
```

---

## PART 2 — Target Users, Personas & Jobs-to-Be-Done (JTBD)

### 2.1 User Personas
| Persona | Role | Primary Device | Network Environment | Key Goal | Frustration / Barrier |
|---|---|---|---|---|---|
| **P1: Ramesh Kumar** | Primary Teacher (Class 1-3) | Low-cost Android Tablet (Android 9+, 2GB RAM) | Intermittent 2G/Offline (syncs weekly at block HQ) | Deliver Grade 2 Math in Ho without speaking Ho | Students disengage when taught in unfamiliar Hindi |
| **P2: Birsa Hembrom** | Grade 2 Student | Shared Classroom Tablet / Paper Worksheets | Offline in Village School | Understand counting and nature concepts in Santhali | Textbooks contain only unfamiliar Devanagari Hindi |
| **P3: Dr. Sunita Soren** | Native Language Reviewer / Linguist | Web Portal (Desktop Chrome) | High-speed Broadband | Validate translations, Ol Chiki typography, and cultural analogies | Existing generic MT tools hallucinate tribal vocabulary |
| **P4: Block Officer** | District Admin / JCERT Officer | Web Admin Console | 4G/LTE Office | Track school-level FLN outcomes and offline sync health | Zero visibility into remote single-teacher school progress |

---

## PART 3 — Functional Requirements Matrix

### 3.1 Core System Capabilities (P0 = Prototype Essential, P1 = MVP Required)
- **REQ-01 [P0]:** Hindi to Target Tribal Language Curriculum Translation (Santhali, Ho, Mundari).
- **REQ-02 [P0]:** Context-Aware Pedagogical Adaptation (Grade-level simplification, local cultural analogies like Sohrai/Sarhul).
- **REQ-03 [P0]:** Multi-Script Native Text Generation (Ol Chiki for Santhali, Warang Chiti/Devanagari for Ho, Devanagari for Mundari) + Latin/Devanagari phonetic transliteration.
- **REQ-04 [P0]:** High-Fidelity Target-Language Speech Synthesis (TTS) with playback controls.
- **REQ-05 [P0]:** Bidirectional Live Voice Translation with Streaming Audio Pipeline (Target latency <= 3.0s).
- **REQ-06 [P0]:** Bilingual Worksheet Generator (PDF/Printable with MCQ, matching, visual prompts).
- **REQ-07 [P0]:** Visual Multimodal Flashcard Generation with audio pronunciation triggers.
- **REQ-08 [P0]:** NIPUN Bharat & JCERT Curriculum Alignment (Grounded in official grade-wise learning outcomes).
- **REQ-09 [P0]:** Human-in-the-Loop (HITL) Teacher Review, Edit, and Approval Workflow before publication.
- **REQ-10 [P0]:** 100% Offline Classroom Operation after initial sync (Local SQLite/Room database).
- **REQ-11 [P0]:** Durable Outbox & Low-Bandwidth Synchronization Engine (Idempotent UUID transactions, gzip compression).
- **REQ-12 [P0]:** Hardware-Aware Dynamic Execution (Adapting to tablet RAM, battery, thermal, and network state).
- **REQ-13 [P0]:** Full Auditability and Retrieval Provenance (Tracking chunk IDs, similarity scores, prompt versions).
- **REQ-14 [P1]:** Multi-Language Extensibility Framework via abstract LanguageProvider adapters.
- **REQ-15 [P0]:** Machine Translation Quality Estimation (COMETKiwi / XCOMET / MQM error span analysis).
- **REQ-16 [P0]:** Role-Based Access Control (RBAC) & Multi-Tenant School Scoping.

---

## PART 4 — Multilingual RAG, Language Intelligence & Quality Governance

### 4.1 Hybrid RAG Retrieval Engine
The system enforces strict curriculum grounding using a hybrid retrieval mechanism:
```
User Query (Hindi FLN Prompt)
       │
       ▼
Metadata Filter (State: JH, Grade: 1-5, Subject: FLN/Math/EVS, Outcome Code)
       │
  ┌────┴──────────────────────────┐
  ▼                               ▼
Lexical BM25 Search       Dense Multilingual Vector (BGE-M3 / LaBSE)
  │                               │
  └────┬──────────────────────────┘
       ▼
Reciprocal Rank Fusion (RRF) & Cross-Encoder Reranker
       │
       ▼
Curriculum Evidence Chunk Set + Provenance Metadata
       │
       ▼
Generative LLM (Gemini 3.1 Pro / 3.5 Flash / Claude 3.7 / Local LLaMA-3-8B-Q4)
       │
       ▼
Grounding & Hallucination Guardrail Check
```

---

## PART 5 — Latency Budget & SLA Targets

### 5.1 Voice-to-Voice Latency Budget (Target <= 3000 ms)
| Stage | Component | Target Budget (P50) | Upper Bound (P95) |
|---|---|---|---|
| 1 | Voice Activity Detection (VAD) | 100 ms | 150 ms |
| 2 | Automatic Speech Recognition (ASR) | 600 ms | 850 ms |
| 3 | Translation & Pedagogical Adaptation | 500 ms | 800 ms |
| 4 | Safety & Grounding Validation | 150 ms | 250 ms |
| 5 | Text-to-Speech (TTS) Synthesis | 650 ms | 900 ms |
| 6 | Network Transport / Playback Buffer | 200 ms | 350 ms |
| **Total** | **End-to-End Voice Dialogue** | **2200 ms** | **3300 ms** |
