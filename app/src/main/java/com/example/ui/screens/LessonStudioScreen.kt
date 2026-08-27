package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.local.LessonEntity
import com.example.data.seed.PreloadedData
import com.example.domain.model.GradeLevel
import com.example.domain.model.SubjectArea
import com.example.domain.model.TargetLanguage
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun LessonStudioScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedGrade by viewModel.selectedGrade.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val learningOutcome by viewModel.learningOutcome.collectAsState()
    val hindiPromptInput by viewModel.hindiPromptInput.collectAsState()
    val enableHighThinking by viewModel.enableHighThinking.collectAsState()
    val isGeneratingLesson by viewModel.isGeneratingLesson.collectAsState()
    val currentLessonDetail by viewModel.currentLessonDetail.collectAsState()
    val allLessons by viewModel.allLessons.collectAsState()

    var showOutcomeDropdown by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // Hero Banner: Pedagogical Studio Purpose (Glassmorphic Light Coffee Hero)
        item {
            GlassmorphicCard(
                containerColor = CoffeePrimary.copy(alpha = 0.92f),
                borderBrush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.45f),
                        CoffeePrimaryContainer.copy(alpha = 0.3f)
                    )
                ),
                elevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "Lesson Studio",
                                    tint = Color.White
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "शिक्षक पाठ स्टूडियो (Lesson Studio)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "NCERT/JCERT FLN Grounded • Santhali, Ho, Mundari",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }

        // Configuration Glass Card
        item {
            GlassmorphicCard(
                containerColor = GlassSurfaceLight,
                elevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Target Language Selector
                    LanguageSelectorChipRow(
                        selectedLanguage = selectedLanguage,
                        onLanguageSelected = { viewModel.setLanguage(it) }
                    )

                    HorizontalDivider(color = GlassBorderLight)

                    // Grade Selector
                    GradeSelectorChipRow(
                        selectedGrade = selectedGrade,
                        onGradeSelected = { viewModel.setGrade(it) }
                    )

                    // Learning Outcome Preset Selector
                    Column {
                        Text(
                            text = "पाठ्यक्रम अधिगम प्रतिफल (Learning Outcome):",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = learningOutcome,
                            onValueChange = { viewModel.setLearningOutcome(it) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = GlassSurfaceUltraLight,
                                unfocusedContainerColor = GlassSurfaceUltraLight,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = GlassBorderLight
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("learning_outcome_input"),
                            trailingIcon = {
                                IconButton(onClick = { showOutcomeDropdown = !showOutcomeDropdown }) {
                                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select Outcome")
                                }
                            },
                            shape = RoundedCornerShape(16.dp)
                        )
                        DropdownMenu(
                            expanded = showOutcomeDropdown,
                            onDismissRequest = { showOutcomeDropdown = false }
                        ) {
                            PreloadedData.flnSyllabusTopics.forEach { topic ->
                                DropdownMenuItem(
                                    text = { Text(topic) },
                                    onClick = {
                                        viewModel.setLearningOutcome(topic)
                                        showOutcomeDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // Hindi Lesson Input
                    Column {
                        Text(
                            text = "शिक्षक का हिन्दी निर्देश / पाठ विषय (Hindi Prompt):",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = hindiPromptInput,
                            onValueChange = { viewModel.setHindiPrompt(it) },
                            placeholder = { Text("उदा. बच्चों को साल के पेड़ (Sarjom) का महत्व और पत्तियों की गिनती सिखाएं...") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = GlassSurfaceUltraLight,
                                unfocusedContainerColor = GlassSurfaceUltraLight,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = GlassBorderLight
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 90.dp)
                                .testTag("hindi_prompt_input"),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    // High Thinking Mode Toggle (Gemini 3.1 Pro high reasoning)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f))
                            .border(BorderStroke(1.dp, GlassBorderLight), RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "Thinking Mode",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column {
                                Text(
                                    text = "High Thinking Mode (गहन तर्कण)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "gemini-3.1-pro-preview (ThinkingLevel.HIGH)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                        Switch(
                            checked = enableHighThinking,
                            onCheckedChange = { viewModel.toggleHighThinking(it) },
                            modifier = Modifier.testTag("high_thinking_switch")
                        )
                    }

                    // Generate Action Button
                    Button(
                        onClick = { viewModel.generateLesson() },
                        enabled = !isGeneratingLesson && hindiPromptInput.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("generate_lesson_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        if (isGeneratingLesson) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("शिक्षण अनुकूलन जनरेट हो रहा है...", color = Color.White)
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "पाठ जनरेट करें (Pedagogical RAG)",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Display Current Generated Result & Teacher Approval Gate
        if (currentLessonDetail != null) {
            val lesson = currentLessonDetail!!
            item {
                GlassmorphicCard(
                    containerColor = GlassSurfaceLight,
                    borderBrush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            GlassBorderHighlight
                        )
                    ),
                    borderWidth = 1.5.dp,
                    elevation = 4.dp,
                    modifier = Modifier.fillMaxWidth().testTag("generated_lesson_card")
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎯 शिक्षण अनुकूलन परिणाम",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (lesson.status == "APPROVED") SuccessGreen.copy(alpha = 0.15f) else WarningAmber.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, if (lesson.status == "APPROVED") SuccessGreen.copy(alpha = 0.3f) else WarningAmber.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = if (lesson.status == "APPROVED") "✅ APPROVED & READY" else "⚠️ REVIEW REQUIRED",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (lesson.status == "APPROVED") SuccessGreen else WarningAmber,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        QualityScoreBadge(
                            qualityScore = lesson.qualityScore,
                            groundingScore = lesson.groundingScore
                        )

                        // 1. Native Target Language Script (Ol Chiki / Devanagari)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, GlassBorderLight),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "मातृभाषा पाठ (${lesson.targetLanguage} - Native Script):",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = lesson.adaptedExplanation,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 24.sp
                                )
                            }
                        }

                        // 2. Roman Transliteration Guide for Non-Native Teacher
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, GlassBorderLight),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "🔤 शिक्षक उच्चारण लिपि (Roman Transliteration):",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = lesson.transliterationText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                        }

                        // 3. Cultural Analogy & Real Life Bridge
                        if (lesson.culturalAnalogy.isNotBlank()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Park, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                                Column {
                                    Text(
                                        text = "स्थानीय सांस्कृतिक संदर्भ (Local Analogy):",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = lesson.culturalAnalogy,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        // 4. Play-based Classroom Activity
                        if (lesson.activityPrompt.isNotBlank()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.SportsEsports, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(20.dp))
                                Column {
                                    Text(
                                        text = "कक्षा गतिविधि (Classroom Activity):",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = lesson.activityPrompt,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        // 5. Pronunciation guidance
                        if (lesson.pronunciationGuide.isNotBlank()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                Column {
                                    Text(
                                        text = "ध्वनि व उच्चारण निर्देश:",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = lesson.pronunciationGuide,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = GlassBorderLight)

                        // Teacher Verification & Approval Action
                        if (lesson.status != "APPROVED") {
                            Button(
                                onClick = { viewModel.approveLesson(lesson.id) },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("approve_lesson_button")
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("शिक्षक सत्यापन व स्वीकृति (Approve & Publish to Field)")
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Check, tint = SuccessGreen, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "स्वीकृत व ऑफलाइन टैबलेट्स के लिए आउटबॉक्स में कतारबद्ध",
                                    color = SuccessGreen,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }

        // Existing Lessons Library Section
        item {
            SectionHeader(
                title = "सहेजे गए FLN पाठ (Saved Curriculum Library)",
                subtitle = "${allLessons.size} पाठ उपलब्ध (ऑफलाइन सिंक सक्षम)",
                icon = Icons.Default.FolderSpecial
            )
        }

        items(allLessons) { lesson ->
            GlassmorphicCard(
                shape = RoundedCornerShape(18.dp),
                containerColor = GlassSurfaceLight,
                elevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = lesson.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${lesson.grade} • ${lesson.subject}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (lesson.status == "APPROVED" || lesson.status == "PUBLISHED") SuccessGreen.copy(alpha = 0.15f) else WarningAmber.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, if (lesson.status == "APPROVED" || lesson.status == "PUBLISHED") SuccessGreen.copy(alpha = 0.3f) else WarningAmber.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = lesson.status,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (lesson.status == "APPROVED" || lesson.status == "PUBLISHED") SuccessGreen else WarningAmber,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = lesson.adaptedExplanation,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

