package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.CurriculumContentEntity
import com.example.domain.model.TargetLanguage
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.GlassmorphicSurface
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurriculumBrowserScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onNavigateToStudio: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val curriculumList by viewModel.searchedCurriculum.collectAsState()
    val allCurriculum by viewModel.allCurriculumContent.collectAsState()
    val searchQuery by viewModel.curriculumSearchQuery.collectAsState()
    val selectedLanguageFilter by viewModel.selectedCurriculumLanguageFilter.collectAsState()
    val selectedGradeFilter by viewModel.selectedCurriculumGradeFilter.collectAsState()
    val selectedSubjectFilter by viewModel.selectedCurriculumSubjectFilter.collectAsState()
    val selectedDetailChunk by viewModel.selectedCurriculumDetail.collectAsState()
    val isAddSheetOpen by viewModel.isAddCurriculumSheetOpen.collectAsState()

    var chunkToDelete by remember { mutableStateOf<CurriculumContentEntity?>(null) }

    // Detail Modal Sheet
    if (selectedDetailChunk != null) {
        CurriculumDetailModal(
            chunk = selectedDetailChunk!!,
            onDismiss = { viewModel.openCurriculumDetail(null) },
            onLoadIntoStudio = {
                viewModel.loadCurriculumChunkIntoLessonStudio(it)
                viewModel.openCurriculumDetail(null)
                Toast.makeText(context, "पाठ स्टूडियो में लोड किया गया!", Toast.LENGTH_SHORT).show()
                onNavigateToStudio?.invoke()
            }
        )
    }

    // Add Custom Lesson Sheet
    if (isAddSheetOpen) {
        AddCustomCurriculumDialog(
            onDismiss = { viewModel.openAddCurriculumSheet(false) },
            onSave = { newChunk ->
                viewModel.insertCustomCurriculumChunk(newChunk)
                Toast.makeText(context, "स्थानीय पाठ्यक्रम Room DB में सुरक्षित!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Delete Confirmation Dialog
    if (chunkToDelete != null) {
        AlertDialog(
            onDismissRequest = { chunkToDelete = null },
            title = { Text("पाठ हटाएं? (Delete Chunk)") },
            text = { Text("क्या आप '${chunkToDelete?.chapterTitle}' को स्थानीय Room DB से हटाना चाहते हैं?") },
            confirmButton = {
                Button(
                    onClick = {
                        chunkToDelete?.let { viewModel.deleteCurriculumChunk(it.id) }
                        chunkToDelete = null
                        Toast.makeText(context, "पाठ हटाया गया", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("हटाएं", color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(onClick = { chunkToDelete = null }) {
                    Text("रद्द करें")
                }
            },
            shape = RoundedCornerShape(18.dp)
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp)
    ) {
        // 1. Glassmorphic Hero Banner
        item {
            GlassmorphicCard(
                containerColor = CoffeePrimary.copy(alpha = 0.94f),
                borderBrush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.5f),
                        CoffeePrimaryContainer.copy(alpha = 0.3f)
                    )
                ),
                elevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.22f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.LibraryBooks,
                                        contentDescription = "Curriculum Library",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "स्थानीय पाठ्यक्रम ज्ञानकोष",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "JCERT / NCERT Grounded • On-Device Room DB",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }

                        // Add Local Curriculum Action
                        FilledTonalButton(
                            onClick = { viewModel.openAddCurriculumSheet(true) },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = Color.White.copy(alpha = 0.25f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("add_custom_curriculum_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircleOutline,
                                contentDescription = "Add",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "नया पाठ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Metrics Strip: Room DB Status & Local Chapters
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${allCurriculum.size}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "कुल पाठ (Room DB)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.85f),
                                    maxLines = 1
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "3 बोलियाँ",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "संथाली • हो • मुण्डारी",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.85f),
                                    maxLines = 1
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "100%",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = "ऑफलाइन कैश",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.85f),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Search Box with Glassmorphic Translucency
        item {
            GlassmorphicCard(
                containerColor = GlassSurfaceFloating,
                borderBrush = Brush.linearGradient(listOf(GlassBorderHighlight, GlassBorderLight)),
                elevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setCurriculumSearch(it) },
                        placeholder = {
                            Text(
                                "पाठ, विषय, या Ol Chiki शब्द खोजें (e.g. साल, ᱫᱟᱨᱮ, गिनती)...",
                                style = MaterialTheme.typography.bodySmall,
                                color = CoffeeTextSecondaryLight
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setCurriculumSearch("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = CoffeeTextSecondaryLight
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = GlassSurfaceLight,
                            unfocusedContainerColor = GlassSurfaceLight,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = GlassBorderLight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("curriculum_search_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Language Filter Chips
                    Text(
                        text = "मातृभाषा फ़िल्टर (Language Filter):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedLanguageFilter == null,
                                onClick = { viewModel.setCurriculumLanguageFilter(null) },
                                label = { Text("सभी बोलियाँ (All)") },
                                shape = RoundedCornerShape(14.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedLanguageFilter == null,
                                    borderColor = GlassBorderLight,
                                    selectedBorderColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.testTag("filter_lang_all")
                            )
                        }
                        items(TargetLanguage.values()) { lang ->
                            val isSelected = selectedLanguageFilter == lang.name
                            val accent = when (lang) {
                                TargetLanguage.SANTHALI -> SanthaliAccent
                                TargetLanguage.HO -> HoAccent
                                TargetLanguage.MUNDARI -> MundariAccent
                            }
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setCurriculumLanguageFilter(if (isSelected) null else lang.name) },
                                label = { Text("${lang.displayName} (${lang.nativeName})") },
                                shape = RoundedCornerShape(14.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = accent.copy(alpha = 0.2f),
                                    selectedLabelColor = accent
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = GlassBorderLight,
                                    selectedBorderColor = accent
                                ),
                                modifier = Modifier.testTag("filter_lang_${lang.code}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Grade Filter Chips
                    Text(
                        text = "कक्षा स्तर (Grade Level):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val gradeList = listOf("1", "2", "3", "4", "5")
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedGradeFilter == null,
                                onClick = { viewModel.setCurriculumGradeFilter(null) },
                                label = { Text("सभी कक्षाएँ") },
                                shape = RoundedCornerShape(14.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedGradeFilter == null,
                                    borderColor = GlassBorderLight,
                                    selectedBorderColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                        items(gradeList) { gradeNum ->
                            val isSelected = selectedGradeFilter == "Grade $gradeNum" || selectedGradeFilter == gradeNum
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setCurriculumGradeFilter(if (isSelected) null else "Grade $gradeNum") },
                                label = { Text("कक्षा $gradeNum") },
                                shape = RoundedCornerShape(14.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = GlassBorderLight,
                                    selectedBorderColor = MaterialTheme.colorScheme.secondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subject Area Filter Chips
                    Text(
                        text = "विषय (Subject Area):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val subjectFilters = listOf(
                        "भाषा" to "भाषा (FLN)",
                        "गणित" to "गणित व संख्या",
                        "पर्यावरण" to "पर्यावरण (EVS)",
                        "संस्कृति" to "कला व संस्कृति"
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                selected = selectedSubjectFilter == null,
                                onClick = { viewModel.setCurriculumSubjectFilter(null) },
                                label = { Text("सभी विषय") },
                                shape = RoundedCornerShape(14.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedSubjectFilter == null,
                                    borderColor = GlassBorderLight,
                                    selectedBorderColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                        items(subjectFilters) { (key, label) ->
                            val isSelected = selectedSubjectFilter == key
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setCurriculumSubjectFilter(if (isSelected) null else key) },
                                label = { Text(label) },
                                shape = RoundedCornerShape(14.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = GlassBorderLight,
                                    selectedBorderColor = MaterialTheme.colorScheme.tertiary
                                )
                            )
                        }
                    }

                    // Reset Filters row if active
                    val hasActiveFilters = searchQuery.isNotBlank() || selectedLanguageFilter != null || selectedGradeFilter != null || selectedSubjectFilter != null
                    if (hasActiveFilters) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${curriculumList.size} पाठ मिले",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            TextButton(
                                onClick = { viewModel.resetCurriculumFilters() },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("फ़िल्टर रीसेट करें", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // 3. Section Title & List
        item {
            SectionHeader(
                title = "स्थानीय पाठ्य सामग्री सूची (${curriculumList.size})",
                subtitle = "कक्षा में शिक्षण हेतु सीधे पाठ स्टूडियो में लोड करें या विस्तार से पढ़ें",
                icon = Icons.Default.MenuBook
            )
        }

        // 4. Empty State
        if (curriculumList.isEmpty()) {
            item {
                GlassmorphicCard(
                    containerColor = GlassSurfaceLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = "No Curriculum Found",
                            tint = CoffeeTextSecondaryLight,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "कोई पाठ्यक्रम नहीं मिला",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "कृपया अपने खोज शब्द या फ़िल्टर बदलें, अथवा नया स्थानीय पाठ जोड़ें।",
                            style = MaterialTheme.typography.bodySmall,
                            color = CoffeeTextSecondaryLight,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.resetCurriculumFilters() },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("सभी पाठ देखें")
                        }
                    }
                }
            }
        } else {
            items(curriculumList, key = { it.id }) { chunk ->
                CurriculumChunkCard(
                    chunk = chunk,
                    onOpenDetail = { viewModel.openCurriculumDetail(chunk) },
                    onLoadIntoStudio = {
                        viewModel.loadCurriculumChunkIntoLessonStudio(chunk)
                        Toast.makeText(context, "'${chunk.chapterTitle}' पाठ स्टूडियो में लोड हुआ!", Toast.LENGTH_SHORT).show()
                        onNavigateToStudio?.invoke()
                    },
                    onDelete = { chunkToDelete = chunk }
                )
            }
        }
    }
}

/**
 * Interactive Glassmorphic Card for displaying an individual curriculum entity from Room DB.
 */
@Composable
fun CurriculumChunkCard(
    chunk: CurriculumContentEntity,
    onOpenDetail: () -> Unit,
    onLoadIntoStudio: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val langAccent = when (chunk.tribalLanguage.uppercase()) {
        "SANTHALI" -> SanthaliAccent
        "HO" -> HoAccent
        "MUNDARI" -> MundariAccent
        else -> MaterialTheme.colorScheme.primary
    }

    GlassmorphicCard(
        containerColor = GlassSurfaceFloating,
        borderBrush = Brush.linearGradient(
            listOf(
                GlassBorderHighlight,
                langAccent.copy(alpha = 0.35f),
                GlassBorderLight
            )
        ),
        elevation = 3.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpenDetail() }
            .testTag("curriculum_card_${chunk.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Grade, Chapter, Language Badge, and Outcome Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Grade & Subject Pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        border = BorderStroke(1.dp, GlassBorderLight)
                    ) {
                        Text(
                            text = "${chunk.grade} • अध्याय ${chunk.chapterNumber}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Target Language Pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = langAccent.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, langAccent.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = chunk.tribalLanguage,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = langAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // JCERT Outcome Code Pill
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SuccessGreen.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.35f))
                ) {
                    Text(
                        text = chunk.learningOutcomeCode,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Chapter Title & Topic
            Text(
                text = chunk.chapterTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = chunk.topic,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Hindi Lesson Text Preview
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GlassSurfaceLight,
                border = BorderStroke(1.dp, GlassBorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "हिन्दी मुख्य पाठ:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = CoffeeTextSecondaryLight
                    )
                    Text(
                        text = chunk.lessonTextHindi,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tribal Native Script & Transliteration Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = langAccent.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, langAccent.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "स्थानीय भाषा व लिपि (${chunk.tribalScriptType}):",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = langAccent
                        )
                        Text(
                            text = chunk.dialectOrRegion,
                            style = MaterialTheme.typography.labelSmall,
                            color = CoffeeTextSecondaryLight
                        )
                    }
                    Text(
                        text = chunk.tribalLessonText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = langAccent,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    Text(
                        text = "उच्चारण: ${chunk.transliterationDevanagari}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Cultural Analogies & Region Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Park,
                    contentDescription = "Culture",
                    tint = SuccessGreen,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "सांस्कृतिक संदर्भ: ${chunk.culturalContextTag}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Bloom's taxonomy tag
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = "Bloom: ${chunk.bloomsTaxonomyLevel}",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions Row: Load into Studio Button, Details Button, Delete Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onLoadIntoStudio,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("load_studio_btn_${chunk.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Load",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("पाठ स्टूडियो में लोड करें", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onOpenDetail,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GlassBorderLight),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("detail_btn_${chunk.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Details",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("विवरण", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("delete_chunk_${chunk.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Detailed Modal Dialog showing full pedagogical breakdown for a curriculum entity.
 */
@Composable
fun CurriculumDetailModal(
    chunk: CurriculumContentEntity,
    onDismiss: () -> Unit,
    onLoadIntoStudio: (CurriculumContentEntity) -> Unit
) {
    val langAccent = when (chunk.tribalLanguage.uppercase()) {
        "SANTHALI" -> SanthaliAccent
        "HO" -> HoAccent
        "MUNDARI" -> MundariAccent
        else -> MaterialTheme.colorScheme.primary
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = GlassSurfaceFloating,
            border = BorderStroke(1.5.dp, GlassBorderHighlight),
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Modal Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = langAccent.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, langAccent.copy(alpha = 0.4f)),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = "Lesson",
                                    tint = langAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "पाठ्यक्रम विवरण कार्ड",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${chunk.curriculumBoard} • ${chunk.grade}",
                                style = MaterialTheme.typography.labelSmall,
                                color = CoffeeTextSecondaryLight
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(
                    color = GlassBorderLight,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                // Scrollable Content
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Chapter and Learning Outcome Header
                    item {
                        GlassmorphicCard(
                            containerColor = GlassSurfaceLight,
                            elevation = 1.dp
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "अध्याय ${chunk.chapterNumber}: ${chunk.chapterTitle}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "प्रकरण (Topic): ${chunk.topic}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = SuccessGreen.copy(alpha = 0.12f),
                                    border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "सीखने का प्रतिफल (Outcome Code: ${chunk.learningOutcomeCode}):",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = SuccessGreen
                                        )
                                        Text(
                                            text = chunk.learningOutcomeDescription,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Hindi Lesson Text & Pedagogical Explanation
                    item {
                        GlassmorphicCard(
                            containerColor = GlassSurfaceLight,
                            elevation = 1.dp
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "1. हिन्दी मूल पाठ (Hindi Primary Lesson)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = chunk.lessonTextHindi,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "शिक्षक अध्यापन विधि (Pedagogical Strategy):",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = CoffeeTextSecondaryLight
                                )
                                Text(
                                    text = chunk.pedagogicalExplanationHindi,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Tribal Translation & Scripts
                    item {
                        GlassmorphicCard(
                            containerColor = langAccent.copy(alpha = 0.08f),
                            borderBrush = Brush.linearGradient(listOf(langAccent.copy(alpha = 0.3f), GlassBorderLight)),
                            elevation = 1.dp
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "2. मातृभाषा अनुवाद (${chunk.tribalLanguage})",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = langAccent
                                    )
                                    Text(
                                        text = "लिपि: ${chunk.tribalScriptType}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = langAccent
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = chunk.tribalLessonText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (chunk.tribalNativeScriptText.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White.copy(alpha = 0.7f),
                                        border = BorderStroke(1.dp, langAccent.copy(alpha = 0.2f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = chunk.tribalNativeScriptText,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = langAccent,
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "देवनागरी उच्चारण (Phonetic Transliteration):",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = CoffeeTextSecondaryLight
                                )
                                Text(
                                    text = chunk.transliterationDevanagari,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Roman Pronunciation: ${chunk.transliterationLatin}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CoffeeTextSecondaryLight
                                )
                            }
                        }
                    }

                    // Classroom Activity & Oral Assessment
                    item {
                        GlassmorphicCard(
                            containerColor = GlassSurfaceLight,
                            elevation = 1.dp
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "3. खेल-आधारित कक्षा गतिविधि (Classroom Activity)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = chunk.classroomActivityPrompt,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "मौखिक मूल्यांकन प्रश्न (Oral Formative Assessment):",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = chunk.oralAssessmentQuestion,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Pedagogical Metadata & Textbook Reference
                    item {
                        GlassmorphicSurface(
                            containerColor = GlassSurfaceLight,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "📖 पाठ्यपुस्तक संदर्भ: ${chunk.textbookSourceReference}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CoffeeTextSecondaryLight
                                )
                                Text(
                                    text = "📍 क्षेत्र व बोली: ${chunk.dialectOrRegion}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CoffeeTextSecondaryLight,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Text(
                                    text = "🏷️ खोज कीवर्ड: ${chunk.keywordsForRetrieval}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CoffeeTextSecondaryLight,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("बंद करें")
                    }

                    Button(
                        onClick = { onLoadIntoStudio(chunk) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.weight(1.4f)
                    ) {
                        Icon(Icons.Default.School, contentDescription = "Studio", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("स्टूडियो में लोड करें", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Dialog for teachers to create and save a new local tribal curriculum chunk into Room DB.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomCurriculumDialog(
    onDismiss: () -> Unit,
    onSave: (CurriculumContentEntity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var topic by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("Grade 2") }
    val subject = "भाषा व बुनियादी साक्षरता (FLN)"
    var language by remember { mutableStateOf(TargetLanguage.SANTHALI) }
    var hindiText by remember { mutableStateOf("") }
    var tribalText by remember { mutableStateOf("") }
    var transliteration by remember { mutableStateOf("") }
    var outcomeCode by remember { mutableStateOf("FLN-JH-01") }
    var culturalContext by remember { mutableStateOf("सरहुल व प्रकृति पूजा") }
    var activity by remember { mutableStateOf("कक्षा में नए शब्दों का उच्चारण दोहराएं") }

    val gradeOptions = listOf("Grade 1", "Grade 2", "Grade 3", "Grade 4", "Grade 5")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = GlassSurfaceFloating,
            border = BorderStroke(1.5.dp, GlassBorderHighlight),
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.AddCircle, contentDescription = "Add", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                        Text(
                            text = "नया स्थानीय पाठ्यक्रम जोड़ें",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(color = GlassBorderLight, modifier = Modifier.padding(vertical = 10.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("पाठ का शीर्षक (Chapter Title)*") },
                            placeholder = { Text("e.g. साल का पेड़ और सरहुल पर्व") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("add_curriculum_title_input")
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = topic,
                            onValueChange = { topic = it },
                            label = { Text("प्रकरण (Topic / Concept)*") },
                            placeholder = { Text("e.g. सरजोम दारे व प्रकृति पूजा") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Language Selector
                    item {
                        Text("लक्ष्य मातृभाषा (Tribal Language):", style = MaterialTheme.typography.labelMedium)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TargetLanguage.values().forEach { lang ->
                                FilterChip(
                                    selected = language == lang,
                                    onClick = { language = lang },
                                    label = { Text(lang.displayName) },
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }

                    // Grade Selector
                    item {
                        Text("कक्षा (Grade):", style = MaterialTheme.typography.labelMedium)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            gradeOptions.forEach { gr ->
                                FilterChip(
                                    selected = grade == gr,
                                    onClick = { grade = gr },
                                    label = { Text(gr) },
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = outcomeCode,
                            onValueChange = { outcomeCode = it },
                            label = { Text("सीखने का प्रतिफल कोड (Learning Outcome Code)") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = hindiText,
                            onValueChange = { hindiText = it },
                            label = { Text("हिन्दी मुख्य पाठ (Hindi Core Text)*") },
                            minLines = 2,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("add_curriculum_hindi_input")
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = tribalText,
                            onValueChange = { tribalText = it },
                            label = { Text("मातृभाषा पाठ (Tribal Translation / Script)*") },
                            minLines = 2,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("add_curriculum_tribal_input")
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = transliteration,
                            onValueChange = { transliteration = it },
                            label = { Text("उच्चारण / देवनागरी लिप्यंतरण (Transliteration)") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = culturalContext,
                            onValueChange = { culturalContext = it },
                            label = { Text("सांस्कृतिक संदर्भ (Cultural Analogy / Festival)") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = activity,
                            onValueChange = { activity = it },
                            label = { Text("कक्षा गतिविधि (Classroom Activity Prompt)") },
                            minLines = 2,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("रद्द करें")
                    }

                    Button(
                        onClick = {
                            if (title.isBlank() || hindiText.isBlank()) return@Button
                            val chunk = CurriculumContentEntity(
                                id = "custom_curriculum_${UUID.randomUUID().toString().take(8)}",
                                state = "झारखंड (Jharkhand)",
                                curriculumBoard = "JCERT / स्थानीय",
                                grade = grade,
                                subject = subject,
                                chapterNumber = (1..10).random(),
                                chapterTitle = title.trim(),
                                topic = topic.ifBlank { title }.trim(),
                                learningOutcomeCode = outcomeCode.trim(),
                                learningOutcomeDescription = "स्थानीय आदिवासी प्राथमिक विद्यालय हेतु अनुकूलित पाठ",
                                lessonTextHindi = hindiText.trim(),
                                pedagogicalExplanationHindi = "शिक्षक स्थानीय संदर्भ और मातृभाषा का प्रयोग करें।",
                                classroomActivityPrompt = activity.trim(),
                                oralAssessmentQuestion = "बच्चे इस पाठ के मुख्य शब्दों को दोहराएं।",
                                tribalLanguage = language.name,
                                tribalLessonText = tribalText.ifBlank { hindiText }.trim(),
                                tribalScriptType = if (language == TargetLanguage.SANTHALI) "OL_CHIKI" else "DEVANAGARI_PHONETIC",
                                tribalNativeScriptText = tribalText.trim(),
                                transliterationLatin = transliteration.trim(),
                                transliterationDevanagari = transliteration.trim(),
                                dialectOrRegion = language.region,
                                culturalContextTag = culturalContext.trim(),
                                bloomsTaxonomyLevel = "APPLY",
                                difficultyLevel = "FOUNDATIONAL",
                                keywordsForRetrieval = "${title}, ${topic}, ${culturalContext}, ${language.name}",
                                textbookSourceReference = "स्थानीय शिक्षक स्व-निर्मित पाठ",
                                isOfflineAvailable = true
                            )
                            onSave(chunk)
                        },
                        enabled = title.isNotBlank() && hindiText.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("save_custom_curriculum_btn")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Save", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Room DB में सुरक्षित करें", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
