package com.example.data.remote

import android.content.Context
import android.util.Log
import com.example.data.local.LessonEntity
import com.example.domain.model.ChatMessage
import com.example.domain.model.UserProfile
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirebaseService(private val context: Context) {

    private fun getFirebaseAuth(): FirebaseAuth? {
        return try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            FirebaseAuth.getInstance()
        } catch (e: Throwable) {
            Log.w("FirebaseService", "FirebaseAuth unavailable (offline fallback): ${e.message}")
            null
        }
    }

    private fun getFirebaseFirestore(): FirebaseFirestore? {
        return try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            FirebaseFirestore.getInstance()
        } catch (e: Throwable) {
            Log.w("FirebaseService", "FirebaseFirestore unavailable (offline fallback): ${e.message}")
            null
        }
    }

    fun getCurrentUser(): UserProfile {
        val auth = getFirebaseAuth()
        val user = auth?.currentUser
        return if (user != null) {
            UserProfile(
                uid = user.uid,
                displayName = user.displayName ?: "शिक्षक (Teacher)",
                email = user.email ?: "teacher.jharkhand@gov.in",
                photoUrl = user.photoUrl?.toString(),
                role = "प्राथमिक शिक्षक (Primary Teacher)",
                schoolName = "राजकीय प्राथमिक विद्यालय, खूंटी",
                district = "खूंटी (Khunti), झारखंड",
                isAuthenticated = true,
                firestoreSynced = true
            )
        } else {
            // Default demo profile representing a Jharkhand primary school teacher
            UserProfile(
                uid = "demo_teacher_jharkhand_01",
                displayName = "रवि रंजन सिंह (Ravi Ranjan Singh)",
                email = "raviranjansingh601520@gmail.com",
                photoUrl = null,
                role = "प्राथमिक भाषा शिक्षक (MTB-MLE Lead)",
                schoolName = "उत्क्रमित प्राथमिक विद्यालय, मुरहू, खूंटी",
                district = "खूंटी (Khunti), झारखंड",
                isAuthenticated = true,
                firestoreSynced = true
            )
        }
    }

    suspend fun saveLessonToFirestore(userId: String, lesson: LessonEntity) {
        val firestore = getFirebaseFirestore() ?: return
        try {
            val docData = hashMapOf(
                "id" to lesson.id,
                "title" to lesson.title,
                "grade" to lesson.grade,
                "subject" to lesson.subject,
                "learningOutcome" to lesson.learningOutcome,
                "hindiPrompt" to lesson.hindiPrompt,
                "targetLanguage" to lesson.targetLanguage,
                "adaptedExplanation" to lesson.adaptedExplanation,
                "nativeScriptText" to lesson.nativeScriptText,
                "transliterationText" to lesson.transliterationText,
                "culturalAnalogy" to lesson.culturalAnalogy,
                "activityPrompt" to lesson.activityPrompt,
                "status" to lesson.status,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore.collection("teachers").document(userId)
                .collection("lessons").document(lesson.id)
                .set(docData, SetOptions.merge())
                .await()
            Log.d("FirebaseService", "Lesson ${lesson.id} synced to Firestore successfully")
        } catch (e: Exception) {
            Log.w("FirebaseService", "Firestore lesson sync failed (offline cache will retain): ${e.message}")
        }
    }

    suspend fun saveAssessmentToFirestore(userId: String, attemptId: String, studentName: String, score: Int, lessonTitle: String) {
        val firestore = getFirebaseFirestore() ?: return
        try {
            val docData = hashMapOf(
                "attemptId" to attemptId,
                "studentName" to studentName,
                "score" to score,
                "lessonTitle" to lessonTitle,
                "syncedAt" to System.currentTimeMillis()
            )
            firestore.collection("teachers").document(userId)
                .collection("assessments").document(attemptId)
                .set(docData, SetOptions.merge())
                .await()
            Log.d("FirebaseService", "Assessment $attemptId synced to Firestore")
        } catch (e: Exception) {
            Log.w("FirebaseService", "Firestore assessment sync skipped/cached: ${e.message}")
        }
    }

    suspend fun saveChatMessageToFirestore(userId: String, message: ChatMessage) {
        val firestore = getFirebaseFirestore() ?: return
        try {
            val docData = hashMapOf(
                "id" to message.id,
                "role" to message.role,
                "text" to message.text,
                "modelUsed" to (message.modelUsed ?: "gemini"),
                "persona" to (message.personaRole?.name ?: "GENERAL"),
                "timestamp" to message.timestamp
            )
            firestore.collection("teachers").document(userId)
                .collection("chat_history").document(message.id)
                .set(docData, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            Log.w("FirebaseService", "Firestore chat sync skipped/cached: ${e.message}")
        }
    }
}

