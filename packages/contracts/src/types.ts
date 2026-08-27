/**
 * BhashaSetu AI Shared Domain Contracts & Types
 * Target Languages: Santhali (Ol Chiki), Ho (Warang Chiti), Mundari (Devanagari/Nag Mundari)
 */

export type TargetLanguage = 'SANTHALI' | 'HO' | 'MUNDARI';
export type ScriptType = 'OL_CHIKI' | 'WARANG_CHITI' | 'DEVANAGARI' | 'LATIN_TRANSLITERATION';
export type GradeLevel = 'GRADE_1' | 'GRADE_2' | 'GRADE_3' | 'GRADE_4' | 'GRADE_5';
export type Subject = 'LANGUAGE_FLN' | 'MATHEMATICS' | 'ENVIRONMENTAL_STUDIES' | 'TRIBAL_HERITAGE';

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

export interface PedagogicalAdaptation {
  gradeLevel: GradeLevel;
  targetLanguage: TargetLanguage;
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
  adaptation: PedagogicalAdaptation;
  confidenceScore: number;
  qualityGateStatus: 'AUTO_APPROVED' | 'REQUIRES_REVIEW' | 'REJECTED';
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

export interface VoiceTranslateResponse {
  sourceTranscript: string;
  translatedText: string;
  script: ScriptType;
  phoneticTransliteration: string;
  audioTtsBase64?: string;
  latencyBreakdownMs: {
    vadMs: number;
    asrMs: number;
    ragMs: number;
    mtMs: number;
    ttsMs: number;
    totalMs: number;
  };
  qualityScore: number;
}

export interface OutboxSyncItem {
  operationId: string;
  idempotencyKey: string;
  entityType: 'LESSON' | 'ASSESSMENT_ATTEMPT' | 'PROGRESS_RECORD';
  entityId: string;
  schoolId: string;
  payload: Record<string, any>;
  timestamp: string;
  status: 'PENDING' | 'SYNCED' | 'CONFLICT';
}
