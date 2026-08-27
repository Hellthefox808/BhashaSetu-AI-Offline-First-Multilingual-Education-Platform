package com.example.ui.util

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class TtsManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking

    private val _currentUtteranceId = MutableStateFlow<String?>(null)
    val currentUtteranceId: StateFlow<String?> = _currentUtteranceId

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("TtsManager", "Error initializing TTS: ${e.message}")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.language = Locale("hi", "IN")
            tts?.setPitch(1.0f)
            tts?.setSpeechRate(0.92f) // Slightly slower rate for classroom clarity

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                    _currentUtteranceId.value = utteranceId
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentUtteranceId.value = null
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentUtteranceId.value = null
                }
            })
        }
    }

    fun speak(text: String, languageCode: String = "hi", utteranceId: String = "utt_${System.currentTimeMillis()}") {
        if (!isInitialized || tts == null || text.isBlank()) {
            return
        }

        try {
            if (languageCode.equals("en", ignoreCase = true)) {
                tts?.language = Locale.ENGLISH
            } else {
                tts?.language = Locale("hi", "IN")
            }

            val params = Bundle().apply {
                putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
            }
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        } catch (e: Exception) {
            Log.e("TtsManager", "TTS speak failed: ${e.message}")
        }
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _currentUtteranceId.value = null
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("TtsManager", "TTS shutdown error: ${e.message}")
        }
    }
}
