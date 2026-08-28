/**
 * BHASHASETU AI (भाषासेतु) — Shared Domain Contracts & Types
 * Target Languages: Santhali (Ol Chiki), Ho (Warang Chiti / Devanagari), Mundari (Devanagari / Nag Mundari)
 * Version: 3.0.0-PROD
 */

export type TargetLanguage = 'SANTHALI' | 'HO' | 'MUNDARI';
export type TargetLanguageCode = 'sat_Olck' | 'hoc_Wara' | 'unr_Deva' | 'hin_Deva';
export type ScriptType = 'OL_CHIKI' | 'WARANG_CHITI' | 'DEVANAGARI' | 'LATIN_TRANSLITERATION';
export type GradeLevel = 'GRADE_1' | 'GRADE_2' | 'GRADE_3' | 'GRADE_4' | 'GRADE_5';
export type Subject = 'LANGUAGE_FLN' | 'MATHEMATICS' | 'ENVIRONMENTAL_STUDIES' | 'TRIBAL_HERITAGE';

export type LessonStatus = 
  | 'DRAFT' 
  | 'GENERATING' 
  | 'REVIEW_REQUIRED' 
  | 'APPROVED' 
  | 'PUBLISHED' 
  | 'ARCHIVED';

export type QualityStatus = 
  | 'HIGH_CONFIDENCE' 
  | 'MEDIUM_CONFIDENCE' 
  | 'LOW_CONFIDENCE' 
  | 'REJECTED';

export interface CurriculumNode {
  id: string;
  state: 'JHARKHAND';
  board: 'JCERT';
  grade: GradeLevel;
  subject: Subject;
  chapterNumber: number;
  chapterTitle: string;
  learningOutcomeCode: string;
  learningOutcomeDescription: string;
  hindiConceptSummary: string;
  culturalKeywords: string[];
}

export interface QualityReport {
  compositeScore: number;
  cometScore: number;
  terminologyScore: number;
  groundingScore: number;
  status: QualityStatus;
  decision?: 'AUTO_PUBLISH_CANDIDATE' | 'TEACHER_REVIEW_REQUIRED' | 'RETRY_ESCALATE';
  detectedErrorSpans?: Array<{
    token: string;
    severity: 'MINOR' | 'MAJOR' | 'CRITICAL';
    category: 'MISTRANSLATION' | 'OMISSION' | 'TERMINOLOGY' | 'FLUENCY';
    suggestedFix: string;
  }>;
  warnings: string[];
}

export interface PedagogicalAdaptation {
  gradeLevel: GradeLevel;
  targetLanguage: TargetLanguage;
  targetLanguageCode: TargetLanguageCode;
  nativeScript: ScriptType;
  translatedText: string;
  transliterationHindi: string;
  transliterationLatin: string;
  culturalAnalogy: string;
  localStoryContext: string;
  audioTtsUrl?: string;
}

export interface Lesson {
  id: string;
  schoolId: string;
  teacherId: string;
  curriculumNodeId: string;
  title: string;
  hindiPrompt: string;
  status: LessonStatus;
  adaptation: PedagogicalAdaptation;
  qualityReport: QualityReport;
  isApprovedByTeacher: boolean;
  version: number;
  createdAt: string;
  updatedAt: string;
}

export interface VoiceTranslateRequest {
  sourceLanguage: 'HINDI';
  targetLanguage: TargetLanguage;
  audioBase64?: string;
  transcriptText?: string;
  curriculumContextId?: string;
}

export interface LatencyBreakdown {
  vadMs: number;
  asrMs: number;
  ragMs: number;
  mtMs: number;
  ttsMs: number;
  totalMs: number;
}

export interface VoiceTranslateResponse {
  sourceTranscript: string;
  translatedText: string;
  script: ScriptType;
  phoneticTransliteration: string;
  audioTtsBase64?: string;
  latencyBreakdownMs: LatencyBreakdown;
  qualityReport: QualityReport;
  slaCompliant: boolean;
}

export interface AssessmentQuestion {
  questionId: string;
  promptHindi: string;
  promptTribal: string;
  options: string[];
  correctOptionIndex: number;
  audioPromptUrl?: string;
}

export interface AssessmentAttempt {
  attemptId: string;
  studentId: string;
  lessonId: string;
  versionNo: number;
  score: number;
  maxScore: number;
  answers: Record<string, number>;
  deviceId: string;
  timestamp: string;
  syncStatus: 'PENDING' | 'ACK_SYNCED';
}

export interface Worksheet {
  id: string;
  lessonId: string;
  title: string;
  grade: GradeLevel;
  targetLanguage: TargetLanguage;
  instructionsHindi: string;
  instructionsTribal: string;
  questions: AssessmentQuestion[];
  printablePdfUrl?: string;
}

export interface Flashcard {
  id: string;
  lessonId: string;
  tribalWord: string;
  nativeScript: string;
  hindiMeaning: string;
  phoneticTransliteration: string;
  illustrationUrl?: string;
  audioUrl?: string;
}

export interface OutboxSyncItem {
  id: string;
  operationId: string;
  entityType: 'LESSON' | 'ASSESSMENT_ATTEMPT' | 'PROGRESS_RECORD';
  entityId: string;
  schoolId: string;
  operation: 'CREATE' | 'UPDATE' | 'DELETE';
  payload: Record<string, any>;
  sequenceNo: number;
  timestamp: string;
  status: 'PENDING' | 'IN_FLIGHT' | 'ACKNOWLEDGED' | 'CONFLICT';
  retryCount: number;
}

export interface SyncPushRequest {
  deviceId: string;
  schoolId: string;
  operations: OutboxSyncItem[];
}

export interface SyncPushResponse {
  acknowledgedOperationIds: string[];
  conflicts: Array<{
    operationId: string;
    entityId: string;
    reason: string;
    resolution: 'SERVER_WON' | 'CLIENT_WON' | 'MERGED';
  }>;
  serverTimestamp: string;
}

export interface SyncPullResponse {
  newCursor: string;
  curriculumUpdates: CurriculumNode[];
  approvedLessons: Lesson[];
}

export interface HardwareState {
  deviceModel: string;
  totalRamMb: number;
  availableRamMb: number;
  batteryPercentage: number;
  isCharging: boolean;
  networkState: 'OFFLINE' | '2G_3G' | '4G_WIFI';
  targetExecutionEnvironment: 'EDGE' | 'LOCAL_LAN' | 'CLOUD';
}

export interface OfflinePackageManifest {
  packageId: string;
  version: string;
  targetLanguage: TargetLanguage;
  gradeLevels: GradeLevel[];
  lessonCount: number;
  audioAssetsCount: number;
  packageSizeBytes: number;
  sha256Checksum: string;
  signature: string;
  minimumAppVersion: string;
  createdAt: string;
}

export interface LanguageCapability {
  language: TargetLanguage;
  isoCode: string;
  script: ScriptType;
  detection: 'VALIDATED' | 'PARTIAL' | 'EXPERIMENTAL';
  asr: 'VALIDATED' | 'PARTIAL' | 'EXPERIMENTAL';
  mt: 'VALIDATED' | 'PARTIAL' | 'EXPERIMENTAL';
  transliteration: 'VALIDATED' | 'PARTIAL' | 'EXPERIMENTAL';
  tts: 'VALIDATED' | 'PARTIAL' | 'EXPERIMENTAL';
  offlineSupport: 'FULL' | 'CACHED' | 'EXPERIMENTAL';
  benchmarkStatus: string;
}

export interface DistrictTelemetrySummary {
  district: string;
  activeSchools: number;
  totalTablets: number;
  syncHealthPercentage: number;
  flnAttainmentGainPercentage: number;
  totalLessonsDelivered: number;
  totalStudentAssessments: number;
  primaryLanguageDistribution: Record<TargetLanguage, number>;
}
