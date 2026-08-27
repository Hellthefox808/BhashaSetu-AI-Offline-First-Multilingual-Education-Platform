package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AmbientCoffeeBackground
import com.example.ui.components.UserProfileSheet
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

sealed class NavigationTab(
    val route: String,
    val title: String,
    val shortLabel: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object CurriculumBrowser : NavigationTab("curriculum", "पाठ्यक्रम", "पाठ्यक्रम", Icons.Filled.LibraryBooks, Icons.Outlined.LibraryBooks)
    object LessonStudio : NavigationTab("studio", "पाठ स्टूडियो", "स्टूडियो", Icons.Filled.School, Icons.Outlined.School)
    object LiveVoice : NavigationTab("voice", "लाइव अनुवाद", "अनुवाद", Icons.Filled.RecordVoiceOver, Icons.Outlined.RecordVoiceOver)
    object Chatbot : NavigationTab("chatbot", "गुरुमित्र AI", "AI चैट", Icons.Filled.Chat, Icons.Outlined.Chat)
    object Practice : NavigationTab("practice", "छात्र अभ्यास", "अभ्यास", Icons.Filled.Assignment, Icons.Outlined.Assignment)
    object Multimodal : NavigationTab("media", "मीडिया व Veo", "मीडिया", Icons.Filled.AutoAwesomeMotion, Icons.Outlined.AutoAwesomeMotion)
    object GlossarySync : NavigationTab("glossary", "शब्दकोश व सिंक", "सिंक व RAG", Icons.Filled.MenuBook, Icons.Outlined.MenuBook)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf<NavigationTab>(NavigationTab.CurriculumBrowser) }
    val pendingOutboxCount by viewModel.pendingOutboxCount.collectAsState()
    val isOfflineSimulated by viewModel.isOfflineSimulated.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val isAuthSheetOpen by viewModel.isAuthSheetOpen.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    // 5 Primary Bottom Navigation Pillars for clean layout without clipping
    val primaryTabs = listOf(
        NavigationTab.CurriculumBrowser,
        NavigationTab.LessonStudio,
        NavigationTab.LiveVoice,
        NavigationTab.Chatbot,
        NavigationTab.GlossarySync
    )

    val allTabs = listOf(
        NavigationTab.CurriculumBrowser,
        NavigationTab.LessonStudio,
        NavigationTab.LiveVoice,
        NavigationTab.Chatbot,
        NavigationTab.Practice,
        NavigationTab.Multimodal,
        NavigationTab.GlossarySync
    )

    if (isAuthSheetOpen) {
        UserProfileSheet(
            viewModel = viewModel,
            onDismiss = { viewModel.openAuthSheet(false) }
        )
    }

    AmbientCoffeeBackground(modifier = modifier) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Surface(
                    color = GlassSurfaceFloating,
                    border = BorderStroke(1.dp, GlassBorderLight),
                    shadowElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        TopAppBar(
                            title = {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "भाषासेतु AI (BhashaSetu)",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = CoffeeTextPrimaryLight
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            border = BorderStroke(1.dp, GlassBorderLight)
                                        ) {
                                            Text(
                                                text = selectedLanguage.code.uppercase(),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Jharkhand MTB-MLE Primary Education Bridge",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CoffeeTextSecondaryLight
                                    )
                                }
                            },
                            actions = {
                                // Outbox / Offline Status Pill
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isOfflineSimulated) WarningAmber.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                    border = BorderStroke(1.dp, if (isOfflineSimulated) WarningAmber else MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .padding(end = 6.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isOfflineSimulated) Icons.Default.CloudOff else Icons.Default.CloudDone,
                                            contentDescription = "Network State",
                                            tint = if (isOfflineSimulated) WarningAmber else MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = if (pendingOutboxCount > 0) "Outbox: $pendingOutboxCount" else "Room Sync",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isOfflineSimulated) WarningAmber else MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                // User Profile / Auth Avatar Button
                                IconButton(
                                    onClick = { viewModel.openAuthSheet(true) },
                                    modifier = Modifier.testTag("user_profile_avatar_button")
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                        border = BorderStroke(1.dp, GlassBorderLight),
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("👨‍🏫", fontSize = 16.sp)
                                        }
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                titleContentColor = CoffeeTextPrimaryLight
                            )
                        )

                        // Quick-Access Capsule Switcher Row (Scrollable, elegant, instant navigation)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            allTabs.forEach { tab ->
                                val isSelected = currentTab == tab
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else GlassSurfaceUltraLight,
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) MaterialTheme.colorScheme.primary else GlassBorderLight
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { currentTab = tab }
                                        .testTag("top_capsule_${tab.route}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = tab.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                Surface(
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = GlassSurfaceFloating,
                    tonalElevation = 6.dp,
                    shadowElevation = 8.dp,
                    border = BorderStroke(
                        1.2.dp,
                        Brush.verticalGradient(
                            listOf(
                                GlassBorderHighlight,
                                GlassBorderLight
                            )
                        )
                    ),
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                ) {
                    NavigationBar(
                        containerColor = Color.Transparent,
                        windowInsets = WindowInsets.navigationBars
                    ) {
                        primaryTabs.forEach { tab ->
                            val isSelected = currentTab == tab
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { currentTab = tab },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.title
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.shortLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        maxLines = 1
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                    unselectedIconColor = CoffeeTextSecondaryLight,
                                    unselectedTextColor = CoffeeTextSecondaryLight
                                ),
                                modifier = Modifier.testTag("nav_tab_${tab.route}")
                            )
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = {
                        fadeIn(animationSpec = androidx.compose.animation.core.tween(200)) togetherWith
                                fadeOut(animationSpec = androidx.compose.animation.core.tween(200))
                    },
                    label = "TabTransition"
                ) { tab ->
                    when (tab) {
                        NavigationTab.CurriculumBrowser -> CurriculumBrowserScreen(
                            viewModel = viewModel,
                            onNavigateToStudio = { currentTab = NavigationTab.LessonStudio }
                        )
                        NavigationTab.LessonStudio -> LessonStudioScreen(viewModel = viewModel)
                        NavigationTab.LiveVoice -> VoiceTranslateScreen(viewModel = viewModel)
                        NavigationTab.Chatbot -> GeminiChatbotScreen(viewModel = viewModel)
                        NavigationTab.Multimodal -> MultimodalScreen(viewModel = viewModel)
                        NavigationTab.Practice -> StudentPracticeScreen(viewModel = viewModel)
                        NavigationTab.GlossarySync -> GlossaryAndSyncScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

