package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.GradeLevel
import com.example.domain.model.SubjectArea
import com.example.domain.model.TargetLanguage
import com.example.ui.theme.*

/**
 * Ambient Light Coffee Gradient Background Canvas with warm mocha & cream glows.
 * Provides authentic optical backdrop refraction for all frosted glassmorphic elements.
 */
@Composable
fun AmbientCoffeeBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                // Base vertical canvas gradient: Light Coffee Latte
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CoffeeAmbientTop,
                            CoffeeAmbientMid,
                            CoffeeAmbientBottom
                        )
                    )
                )
                // Top-right warm caramel coffee orb glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(CoffeeGlowWarm, Color.Transparent),
                        center = Offset(size.width * 0.9f, size.height * 0.15f),
                        radius = size.width * 0.7f
                    ),
                    radius = size.width * 0.7f,
                    center = Offset(size.width * 0.9f, size.height * 0.15f)
                )
                // Bottom-left subtle roasted mocha orb glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(CoffeeGlowMocha, Color.Transparent),
                        center = Offset(size.width * 0.1f, size.height * 0.85f),
                        radius = size.width * 0.8f
                    ),
                    radius = size.width * 0.8f,
                    center = Offset(size.width * 0.1f, size.height * 0.85f)
                )
            },
        content = content
    )
}

/**
 * Reusable Glassmorphism Card Container.
 * Features frosted milky translucency, dual-layer light-gradient hairline border, and gentle depth elevation.
 */
@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(22.dp),
    containerColor: Color = GlassSurfaceLight,
    borderBrush: Brush = Brush.linearGradient(
        colors = listOf(
            GlassBorderHighlight,
            GlassBorderLight,
            GlassBorderHighlight.copy(alpha = 0.4f)
        )
    ),
    borderWidth: Dp = 1.2.dp,
    elevation: Dp = 3.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        shape = shape,
        color = containerColor,
        tonalElevation = elevation,
        shadowElevation = elevation,
        border = BorderStroke(borderWidth, borderBrush),
        modifier = modifier
            .clip(shape)
    ) {
        Column(content = content)
    }
}

/**
 * Reusable Glassmorphic Surface for rows, bars, and compact containers.
 */
@Composable
fun GlassmorphicSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    containerColor: Color = GlassSurfaceTinted,
    borderColor: Color = GlassBorderLight,
    borderWidth: Dp = 1.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        shape = shape,
        color = containerColor,
        border = BorderStroke(borderWidth, borderColor),
        modifier = modifier.clip(shape)
    ) {
        Box(content = content)
    }
}

@Composable
fun LanguageSelectorChipRow(
    selectedLanguage: TargetLanguage,
    onLanguageSelected: (TargetLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "लक्ष्य मातृभाषा (Target Language):",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TargetLanguage.values().forEach { lang ->
                val isSelected = lang == selectedLanguage
                val accentColor = when (lang) {
                    TargetLanguage.SANTHALI -> SanthaliAccent
                    TargetLanguage.HO -> HoAccent
                    TargetLanguage.MUNDARI -> MundariAccent
                }
                
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) accentColor.copy(alpha = 0.15f) else GlassSurfaceLight,
                    border = if (isSelected) {
                        BorderStroke(2.dp, accentColor)
                    } else {
                        BorderStroke(1.dp, GlassBorderLight)
                    },
                    shadowElevation = if (isSelected) 3.dp else 1.dp,
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onLanguageSelected(lang) }
                        .testTag("lang_chip_${lang.code}")
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = lang.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = lang.nativeName,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GradeSelectorChipRow(
    selectedGrade: GradeLevel,
    onGradeSelected: (GradeLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "कक्षा स्तर (FLN Grade):",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            GradeLevel.values().take(3).forEach { grade ->
                val isSelected = grade == selectedGrade
                FilterChip(
                    selected = isSelected,
                    onClick = { onGradeSelected(grade) },
                    label = {
                        Text(
                            text = grade.label.substringBefore(" ("),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = GlassSurfaceLight,
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = GlassBorderLight,
                        selectedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(1f).testTag("grade_chip_${grade.name}")
                )
            }
        }
    }
}

@Composable
fun QualityScoreBadge(
    qualityScore: Float,
    groundingScore: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SuccessGreen.copy(alpha = 0.12f))
            .border(1.dp, SuccessGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = "Grounded Verified",
            tint = SuccessGreen,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "Quality: ${(qualityScore * 100).toInt()}% • RAG Grounding: ${(groundingScore * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = SuccessGreen
        )
    }
}

@Composable
fun LatencyBadge(
    latencyMs: Long,
    modifier: Modifier = Modifier
) {
    val isBudgetMet = latencyMs <= 3000L
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isBudgetMet) MaterialTheme.colorScheme.primaryContainer else WarningAmber.copy(alpha = 0.15f))
            .border(
                1.dp,
                if (isBudgetMet) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else WarningAmber.copy(alpha = 0.4f),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Bolt,
            contentDescription = "Latency",
            tint = if (isBudgetMet) MaterialTheme.colorScheme.primary else WarningAmber,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "$latencyMs ms (${if (isBudgetMet) "⚡ ≤3s Budget Met" else "High latency"})",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isBudgetMet) MaterialTheme.colorScheme.primary else WarningAmber
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (icon != null) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                border = BorderStroke(1.dp, GlassBorderLight),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


