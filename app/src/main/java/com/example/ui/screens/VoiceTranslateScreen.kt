package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.TargetLanguage
import com.example.domain.model.VoiceTurn
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.LanguageSelectorChipRow
import com.example.ui.components.LatencyBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun VoiceTranslateScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val voiceTurns by viewModel.voiceTurns.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    var inputUtterance by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val quickPhrases = listOf(
        "नमस्ते बच्चों! (Welcome children)" to "नमस्ते बच्चों! आज हम सब मिलकर पढ़ाई करेंगे।",
        "अपनी किताबें खोलिए (Open your books)" to "अपनी किताबें खोलिए और पाठ निकालिए।",
        "साल के पेड़ का नाम बताइए (Sal tree)" to "इस पेड़ का नाम बताइए और इसके उपयोग बताएं।",
        "एक साथ मिलकर बोलिए (Repeat together)" to "सभी बच्चे एक साथ मिलकर बोलिए।",
        "बहुत शाबाश! (Well done!)" to "बहुत शाबाश! आप सभी ने सही उत्तर दिया।"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Target Language Selection Header
        LanguageSelectorChipRow(
            selectedLanguage = selectedLanguage,
            onLanguageSelected = { viewModel.setLanguage(it) },
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Real-Time Sub-3s SLA Indicator (Glassmorphic)
        GlassmorphicCard(
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
            borderBrush = Brush.linearGradient(
                listOf(GlassBorderHighlight, GlassBorderLight)
            ),
            elevation = 2.dp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = "Live SLA",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(
                            text = "Live Classroom Voice Relay",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "gemini-3.1-flash-lite + TTS • Target SLA ≤ 3000ms",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Surface(
                    shape = CircleShape,
                    color = SuccessGreen,
                    modifier = Modifier.size(10.dp)
                ) {}
            }
        }

        // Quick Classroom Phrases Carousel
        Text(
            text = "त्वरित कक्षा वाक्य (Quick Classroom Phrases):",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(quickPhrases) { (label, fullText) ->
                AssistChip(
                    onClick = {
                        viewModel.sendVoiceUtterance(fullText)
                        coroutineScope.launch {
                            listState.animateScrollToItem((voiceTurns.size).coerceAtLeast(0))
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = GlassSurfaceLight,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = AssistChipDefaults.assistChipBorder(
                        enabled = true,
                        borderColor = GlassBorderLight
                    ),
                    label = { Text(label.substringBefore(" ("), fontSize = 11.sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.VolumeUp,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }

        // Conversation History Thread
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(voiceTurns) { turn ->
                VoiceTurnBubble(
                    turn = turn,
                    targetLanguage = selectedLanguage,
                    isSpeaking = isSpeaking,
                    onPlayAudio = {
                        val speakText = if (turn.transliteration.isNotBlank()) {
                            "${turn.hindiText}. ${turn.transliteration}"
                        } else {
                            turn.hindiText
                        }
                        viewModel.speakText(speakText, "hi")
                    }
                )
            }
        }

        // Microphone & Utterance Input Bar (Glassmorphic)
        GlassmorphicCard(
            shape = RoundedCornerShape(24.dp),
            elevation = 3.dp,
            containerColor = GlassSurfaceFloating,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputUtterance,
                    onValueChange = { inputUtterance = it },
                    placeholder = { Text("शिक्षक हिन्दी में बोलें या टाइप करें...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = GlassSurfaceUltraLight,
                        unfocusedContainerColor = GlassSurfaceUltraLight,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = GlassBorderLight
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("voice_text_input"),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                // Voice Mic Button
                FilledIconButton(
                    onClick = {
                        if (inputUtterance.isNotBlank()) {
                            viewModel.sendVoiceUtterance(inputUtterance)
                            inputUtterance = ""
                            coroutineScope.launch {
                                listState.animateScrollToItem((voiceTurns.size).coerceAtLeast(0))
                            }
                        } else {
                            viewModel.sendVoiceUtterance("आज हम सब मिलकर नई कविता सीखेंगे।")
                            coroutineScope.launch {
                                listState.animateScrollToItem((voiceTurns.size).coerceAtLeast(0))
                            }
                        }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("mic_send_button")
                ) {
                    Icon(
                        imageVector = if (inputUtterance.isNotBlank()) Icons.Default.Send else Icons.Default.Mic,
                        contentDescription = "Voice Input",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun VoiceTurnBubble(
    turn: VoiceTurn,
    targetLanguage: TargetLanguage,
    isSpeaking: Boolean,
    onPlayAudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassmorphicCard(
        shape = RoundedCornerShape(20.dp),
        containerColor = GlassSurfaceLight,
        borderBrush = Brush.linearGradient(
            listOf(GlassBorderHighlight, GlassBorderLight)
        ),
        elevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Speaker Header & Latency
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        border = BorderStroke(1.dp, GlassBorderLight),
                        modifier = Modifier.size(26.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Teacher",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Text(
                        text = "शिक्षक (Hindi Source):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                LatencyBadge(latencyMs = turn.latencyMs)
            }

            // Hindi Text
            Text(
                text = turn.hindiText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            HorizontalDivider(color = GlassBorderLight)

            // Target Mother-Tongue Translation & Script
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Target Speech",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "${targetLanguage.displayName} (${targetLanguage.nativeName}):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(
                    onClick = onPlayAudio,
                    modifier = Modifier.size(32.dp).testTag("play_turn_audio_${turn.id}")
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Default.GraphicEq else Icons.Default.VolumeUp,
                        contentDescription = "Play Audio",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Native Script Spoken Line
            Text(
                text = turn.targetText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            // Transliteration Phonetic Guide
            if (turn.transliteration.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, GlassBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🔊 ${turn.transliteration}",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
