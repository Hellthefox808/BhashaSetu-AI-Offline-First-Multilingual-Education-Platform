# BhashaSetu AI (भाषासेतु) — Offline-First Multilingual Education Platform

> **Bridging the Classroom Language Barrier:** Empowering Hindi-speaking teachers and tribal students in Jharkhand with Mother-Tongue-Based Multilingual Education (MTB-MLE), local edge RAG, real-time voice translation, and AI pedagogical adaptations.

---

## 🌟 Overview

**BhashaSetu AI** is a specialized Android application tailored for primary school teachers (Grades 1–5) in tribal districts of Jharkhand (e.g., Santhal Pargana, Kolhan, West Singhbhum, Khunti, Dumka). It bridges national/state curriculum requirements (NCERT/JCERT, NIPUN Bharat) with indigenous tribal languages:

- **Santhali (ᱥᱟᱱᱛᱟᱲᱤ)** — Ol Chiki & Devanagari
- **Ho (ᱦᱳ)** — Warang Chiti & Devanagari
- **Mundari (मुण्डारी)** — Devanagari & Nag Mundari

Designed with an **offline-first** architecture, BhashaSetu AI operates reliably in remote classrooms without internet connectivity, utilizing on-device SQLite/Room databases, dense vector embeddings, and BM25 hybrid indexing. When network connectivity is available, the platform synchronizes with Cloud Firestore and leverages Google's Gemini models for pedagogical adaptations, multimodal learning, and real-time guidance.

---

## 🚀 Key Features

### 1. 📖 MTB-MLE Lesson Studio & Pedagogical Adaptation
- Scaffolds core Hindi concepts into tribal languages with cultural analogies (e.g., Sarhul, Sohrai, Karam festivals, indigenous flora/fauna).
- Formulates grade-appropriate explanations, bilingual classroom activities, and pronunciation guides.
- Automatic quality estimation and formative quiz generation.

### 2. ⚡ Offline-First Local Curriculum RAG
- Dual-mode retrieval engine combining **Dense Vector Cosine Similarity** and **BM25 Lexical Matching**.
- Preloaded with foundational FLN (Foundational Literacy and Numeracy) outcomes, vocabulary tables, and JCERT curriculum chunks.
- Sub-150ms on-device retrieval latency with zero cloud dependency during active teaching.

### 3. 🎙️ Live Voice Translation & TTS Engine
- Turn-by-turn conversational translation between Hindi and indigenous tribal languages.
- Transliteration display and phonetic pronunciation support to assist teachers in proper vocalization.

### 4. 🤖 Multi-Persona AI Mentor (Gemini 3.1 / 3.5)
- **MTB-MLE Specialist:** Guides child-centric pedagogy and multilingual classroom strategies.
- **Tribal Language Linguist:** Explains etymology, phonetic nuances, script writing (Ol Chiki, Warang Chiti), and grammar rules.
- **NIPUN Bharat FLN Planner:** Creates structured 45-minute lesson plans and circle-time games.

### 5. 📝 Offline Student Practice & Offline Sync Outbox
- Interactive student practice quizzes with instant visual feedback and audio reinforcement.
- Assessment attempts tracked locally and queued in an offline sync outbox for automatic Firestore sync upon network availability.

### 6. 🏗️ Full-Stack Architecture & Telemetry Visualizer
- Built-in interactive architectural inspection console demonstrating client-edge-cloud contracts, DiskANN vector comparisons, and COMET quality estimation.

---

## 🛠️ Architecture & Tech Stack

```
+-------------------------------------------------------------------------+
|                          Android Client Layer                           |
|  - Jetpack Compose + Material 3 Design System                           |
|  - Kotlin Coroutines & StateFlow Reactive Architecture                 |
|  - Room Database (SQLite 3) with Preloaded Curriculum Data              |
|  - Local Vector Cosine Engine + BM25 Hybrid Lexical Matcher            |
+-------------------------------------------------------------------------+
                                    |
                    (Periodic Sync / Online Actions)
                                    v
+-------------------------------------------------------------------------+
|                           Cloud & AI Services                           |
|  - Google Gemini API (Gemini 3.1 Pro / Gemini 3.5 Flash / Flash Lite)  |
|  - Google Cloud Firestore (Cloud Sync & Multi-Device Outbox)           |
|  - Firebase Authentication & Credential Manager Integration             |
+-------------------------------------------------------------------------+
```

- **Language & Runtime:** Kotlin (Target SDK 36, Min SDK 24, Java 11)
- **UI Toolkit:** Jetpack Compose with Material 3
- **Local Persistence:** AndroidX Room, KSP, SQLite 3
- **Networking & Serialization:** Retrofit, OkHttp, Moshi
- **AI Integration:** Google GenAI / Gemini API via Secure Secrets Plugin
- **Cloud Backend:** Firebase BoM, Cloud Firestore, Firebase Auth, Google Credentials

---

## 📂 Project Structure

```
d:\bhashasetu-ai\
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   ├── BhashaSetuApplication.kt
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/          # Room Database, DAOs, Entities
│   │   │   │   │   ├── remote/         # Gemini & Firebase Services
│   │   │   │   │   ├── repository/     # Unified Offline-First Repository
│   │   │   │   │   └── seed/           # Preloaded Curriculum & Tribal Lexicon
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/          # Core Domain Models & Enums
│   │   │   │   │   └── rag/            # Local RAG & Vector Embedding Engine
│   │   │   │   └── ui/
│   │   │   │       ├── components/     # Reusable Compose Components
│   │   │   │       ├── screens/        # Feature Screens (Lesson Studio, Chatbot, etc.)
│   │   │   │       ├── theme/          # Color Palettes, Typography, Theming
│   │   │   │       ├── util/           # TTS Manager & Audio Utilities
│   │   │   │       └── viewmodel/      # MainViewModel & State Holders
│   │   │   └── res/                    # Drawables, Strings, Icons, Themes
│   │   └── test/                       # Unit & Robolectric Screenshot Tests
│   └── build.gradle.kts
├── gradle/                             # Version catalog (libs.versions.toml) & Wrapper
├── .env.example                        # Environment variables template
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Ladybug (2024.2+) or newer
- **JDK 11** or **JDK 17**
- Android SDK 36 (API Level 36)

### Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Hellthefox808/BhashaSetu-AI-Offline-First-Multilingual-Education-Platform.git
   cd BhashaSetu-AI-Offline-First-Multilingual-Education-Platform
   ```

2. **Configure Environment Variables:**
   - Copy `.env.example` to `.env`:
     ```bash
     cp .env.example .env
     ```
   - Open `.env` and set your `GEMINI_API_KEY`:
     ```properties
     GEMINI_API_KEY=your_actual_gemini_api_key_here
     ```

3. **Open & Build in Android Studio:**
   - Open Android Studio and select **File > Open**, navigating to the project directory.
   - Sync Gradle dependencies and build the project.
   - Run the app on an Android Emulator or connected physical device (API 24+).

---

## 🔒 Security & Privacy

- **Zero Secret Leakage:** `.env` and local keystores are excluded via `.gitignore`. The Secrets Gradle plugin injects build configuration safely at compile time.
- **Offline Data Sovereignty:** Student assessments and teacher lesson drafts remain strictly on-device in encrypted SQLite/Room storage until explicitly synchronized by the teacher.

---

## 📄 License & Attribution

This project is developed for educational equity in multilingual tribal education under the **MIT License**. Contributions and feedback from educators, linguists, and community members are warmly welcome.
