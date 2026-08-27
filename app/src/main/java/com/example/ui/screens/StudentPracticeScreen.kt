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
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.LanguageSelectorChipRow
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

@Composable
fun StudentPracticeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val allStudents by viewModel.allStudents.collectAsState()
    val allAttempts by viewModel.allAttempts.collectAsState()
    val currentStudentIndex by viewModel.currentStudentIndex.collectAsState()
    val isQuizCompleted by viewModel.isQuizCompleted.collectAsState()
    val lastEarnedScore by viewModel.lastEarnedScore.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    val currentQuizQuestion by viewModel.currentQuizQuestion.collectAsState()
    val currentQuizQuestionIndex by viewModel.currentQuizQuestionIndex.collectAsState()
    val practiceQuestions by viewModel.practiceQuestions.collectAsState()

    val currentStudent = allStudents.getOrNull(currentStudentIndex)

    var selectedOption by remember { mutableStateOf<String?>(null) }

    // Reset local option state when question index changes
    LaunchedEffect(currentQuizQuestionIndex) {
        selectedOption = null
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // Active Student Header Card (Coffee Gradient Glass)
        item {
            GlassmorphicCard(
                shape = RoundedCornerShape(24.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                borderBrush = Brush.linearGradient(
                    listOf(Color.White.copy(alpha = 0.35f), Color.Transparent)
                ),
                elevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                        modifier = Modifier.size(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = "Student",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentStudent?.name ?: "सुनीता मुर्मू (Sunita Murmu)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "रोल नं: ${currentStudent?.rollNo ?: "01"} • ${currentStudent?.village ?: "डुमका (Dumka)"} • कक्षा 2",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    // Next Student Switcher Button
                    IconButton(
                        onClick = {
                            viewModel.resetQuiz()
                        },
                        modifier = Modifier.testTag("switch_student_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "अगला छात्र",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // Language Picker
        item {
            LanguageSelectorChipRow(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = { viewModel.setLanguage(it) }
            )
        }

        // Question Navigator Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "प्रश्न प्रगति (${currentQuizQuestionIndex + 1}/${practiceQuestions.size}):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = { viewModel.prevQuizQuestion() },
                            modifier = Modifier.size(32.dp).testTag("prev_quiz_button")
                        ) {
                            Icon(Icons.Default.ChevronLeft, contentDescription = "Previous")
                        }
                        IconButton(
                            onClick = { viewModel.nextQuizQuestion() },
                            modifier = Modifier.size(32.dp).testTag("next_quiz_button")
                        ) {
                            Icon(Icons.Default.ChevronRight, contentDescription = "Next")
                        }
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(practiceQuestions) { idx, q ->
                        val isCurrent = idx == currentQuizQuestionIndex
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCurrent) MaterialTheme.colorScheme.primary else GlassSurfaceLight,
                            border = BorderStroke(
                                1.dp,
                                if (isCurrent) MaterialTheme.colorScheme.primary else GlassBorderLight
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.selectQuizQuestion(idx) }
                                .testTag("quiz_tab_$idx")
                        ) {
                            Text(
                                text = "प्रश्न ${idx + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Quiz Question Card
        item {
            GlassmorphicCard(
                shape = RoundedCornerShape(24.dp),
                containerColor = GlassSurfaceLight,
                elevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            border = BorderStroke(1.dp, GlassBorderLight)
                        ) {
                            Text(
                                text = "अभ्यास प्रश्न ${currentQuizQuestionIndex + 1}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }

                        // Audio Pronunciation Button
                        IconButton(
                            onClick = {
                                viewModel.speakText(
                                    currentQuizQuestion.audioUtterance,
                                    "hi"
                                )
                            },
                            modifier = Modifier.testTag("play_quiz_audio_button")
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.GraphicEq else Icons.Default.VolumeUp,
                                contentDescription = "Audio question",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Hindi + Native Script Question
                    Text(
                        text = currentQuizQuestion.questionHindi,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = currentQuizQuestion.questionTarget,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    HorizontalDivider(color = GlassBorderLight)

                    // Options list
                    currentQuizQuestion.options.forEachIndexed { index, optionText ->
                        val isSelected = selectedOption == optionText
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f) else GlassSurfaceUltraLight,
                            border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, GlassBorderLight),
                            modifier = Modifier
                                .fillMaxWidth()
                                .minimumInteractiveComponentSize()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    if (!isQuizCompleted) selectedOption = optionText
                                }
                                .testTag("quiz_option_$index")
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { if (!isQuizCompleted) selectedOption = optionText }
                                )
                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    if (!isQuizCompleted) {
                        Button(
                            onClick = {
                                val isCorrect = selectedOption == currentQuizQuestion.options.getOrNull(currentQuizQuestion.correctIndex)
                                viewModel.submitStudentQuiz(selectedOption ?: "", isCorrect)
                            },
                            enabled = selectedOption != null,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("submit_quiz_button")
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("उत्तर सबमिट करें (Submit Attempt)")
                        }
                    } else {
                        // Submission Success Feedback
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = SuccessGreen.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, SuccessGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = SuccessGreen)
                                    Text(
                                        text = "शाबाश! अंक: $lastEarnedScore/100",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentQuizQuestion.explanationHindi,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "परिणाम स्थानीय Room DB में तुरंत दर्ज हो गया और आउटबॉक्स में सिंक के लिए कतारबद्ध है।",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SuccessGreen
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            viewModel.nextQuizQuestion()
                                        },
                                        shape = RoundedCornerShape(14.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                        modifier = Modifier.weight(1f).testTag("next_question_button")
                                    ) {
                                        Text("अगला प्रश्न (Next Q)")
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            selectedOption = null
                                            viewModel.resetQuiz()
                                        },
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.weight(1f).testTag("next_student_button")
                                    ) {
                                        Text("अगला छात्र (Next Student)")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Recent Offline Assessment Attempts
        item {
            SectionHeader(
                title = "हालिया मूल्यांकन रिकॉर्ड (Room DB)",
                subtitle = "${allAttempts.size} प्रयास स्थानीय तौर पर संग्रहित (Append-Only Log)",
                icon = Icons.Default.HistoryEdu
            )
        }

        items(allAttempts) { attempt ->
            GlassmorphicCard(
                shape = RoundedCornerShape(16.dp),
                containerColor = GlassSurfaceLight,
                elevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        Text(
                            text = attempt.studentName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = attempt.lessonTitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (attempt.score >= 80) SuccessGreen.copy(alpha = 0.15f) else WarningAmber.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, if (attempt.score >= 80) SuccessGreen.copy(alpha = 0.3f) else WarningAmber.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "${attempt.score} / ${attempt.maxScore} pts",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (attempt.score >= 80) SuccessGreen else WarningAmber,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }
}
