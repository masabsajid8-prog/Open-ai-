package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.service.PromptEnhancerService
import com.example.ui.components.FuturisticButton
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    projects: List<VideoProject>,
    currentUser: User,
    onNavigateToStudio: (initialPrompt: String?, mode: GenerationMode?, style: String?) -> Unit,
    onOpenProject: (VideoProject) -> Unit,
    onNavigateToStyles: () -> Unit,
    modifier: Modifier = Modifier
) {
    var quickPrompt by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Welcome Headline & 3D Hero Banner
        item {
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_hero_card"),
                borderBrush = Brush.horizontalGradient(listOf(NeonCyan, DeepViolet, NeonPink)),
                elevation = 12.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DeepViolet
                        ) {
                            Text(
                                text = "POWERED BY SEEDANCE 2.5",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                letterSpacing = 1.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NeonGreen.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen)
                        ) {
                            Text(
                                text = "FREE ACCESS",
                                color = NeonGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Bring Your Imagination to Life.",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Next-generation 3D AI video studio. Synthesize ultra-cinematic scenes, animate still photography, and craft fluid dance choreography.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Quick-Prompt Input Bar
                    OutlinedTextField(
                        value = quickPrompt,
                        onValueChange = { quickPrompt = it },
                        placeholder = {
                            Text(
                                text = "Describe your cinematic vision...",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_quick_prompt_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceDark,
                            unfocusedContainerColor = SurfaceDark,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    quickPrompt = PromptEnhancerService.samplePrompts.random()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Casino,
                                    contentDescription = "Random Prompt",
                                    tint = SoftCyan
                                )
                            }
                        },
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FuturisticButton(
                            text = "Generate in Studio",
                            icon = Icons.Default.AutoAwesome,
                            onClick = {
                                onNavigateToStudio(quickPrompt.ifBlank { null }, GenerationMode.TEXT_TO_VIDEO, null)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_generate_studio_btn")
                        )

                        IconButton(
                            onClick = {
                                onNavigateToStudio(
                                    PromptEnhancerService.samplePrompts.random(),
                                    GenerationMode.TEXT_TO_VIDEO,
                                    null
                                )
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceCardElevated)
                                .border(1.dp, GlassBorderHighlight, RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shuffle,
                                contentDescription = "Surprise Me",
                                tint = NeonCyan
                            )
                        }
                    }
                }
            }
        }

        // Quick-Start Creation Modes
        item {
            Column {
                Text(
                    text = "STUDIO MODES",
                    color = SoftCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StudioModeCard(
                        title = "Text to Video",
                        subtitle = "Natural prompts",
                        icon = Icons.Default.TextFields,
                        gradient = listOf(DeepViolet, CyberBlue),
                        modifier = Modifier.weight(1f),
                        testTag = "mode_text2video",
                        onClick = { onNavigateToStudio(null, GenerationMode.TEXT_TO_VIDEO, null) }
                    )

                    StudioModeCard(
                        title = "Image to Video",
                        subtitle = "Photo animation",
                        icon = Icons.Default.Image,
                        gradient = listOf(0xFF0070F3, 0xFF00F2FE),
                        modifier = Modifier.weight(1f),
                        testTag = "mode_image2video",
                        onClick = { onNavigateToStudio(null, GenerationMode.IMAGE_TO_VIDEO, null) }
                    )

                    StudioModeCard(
                        title = "Dance & Motion",
                        subtitle = "Choreography",
                        icon = Icons.Default.SportsKabaddi,
                        gradient = listOf(0xFFFF007A, 0xFF7928CA),
                        modifier = Modifier.weight(1f),
                        testTag = "mode_dance_motion",
                        onClick = { onNavigateToStudio(null, GenerationMode.DANCE_AND_MOTION, null) }
                    )
                }
            }
        }

        // Trending Video Styles Carousel
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TRENDING VIDEO STYLES",
                        color = SoftCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "View All 20",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .testTag("view_all_styles_btn")
                            .clickable { onNavigateToStyles() }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(VideoStylesCatalog.categories.take(6)) { style ->
                        StylePillCard(
                            style = style,
                            onClick = {
                                onNavigateToStudio(null, GenerationMode.TEXT_TO_VIDEO, style.name)
                            }
                        )
                    }
                }
            }
        }

        // Featured Seedance 2.5 Showcase Videos
        item {
            Column {
                Text(
                    text = "FEATURED SEEDANCE 2.5 SHOWCASE",
                    color = SoftCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                projects.take(3).forEach { project ->
                    FeaturedProjectCard(
                        project = project,
                        onClick = { onOpenProject(project) },
                        onRemix = {
                            onNavigateToStudio(project.prompt, project.mode, project.styleCategory)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun StudioModeCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: List<Any>,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    val colors = gradient.map {
        when (it) {
            is Color -> it
            is Long -> Color(it)
            else -> DeepViolet
        }
    }

    Surface(
        modifier = modifier
            .testTag(testTag)
            .height(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = SurfaceCard
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(colors[0].copy(alpha = 0.25f), SurfaceCard)))
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(colors)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = subtitle,
                        color = TextMuted,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun StylePillCard(
    style: VideoStyleItem,
    onClick: () -> Unit
) {
    val colors = style.previewGradientColors.map { Color(it) }

    Surface(
        modifier = Modifier
            .width(170.dp)
            .height(95.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        color = SurfaceCard
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(colors[0].copy(alpha = 0.35f), SurfaceCardElevated)))
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = style.tag,
                        color = NeonCyan,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Column {
                    Text(
                        text = style.name,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "1-Tap Apply",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedProjectCard(
    project: VideoProject,
    onClick: () -> Unit,
    onRemix: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("featured_project_${project.id}"),
        onClick = onClick
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val drawableRes = project.previewDrawableRes ?: R.drawable.img_hero_cyberpunk

                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = project.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Aspect ratio & duration badge
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, Color.Black.copy(alpha = 0.75f))
                            )
                        )
                        .padding(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd),
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.65f)
                    ) {
                        Text(
                            text = "${project.resolution} • ${project.durationSeconds}s",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Play Center Icon
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.65f))
                            .border(1.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Video",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = project.title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = project.prompt,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 2,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${project.providerUsed} • ${project.styleCategory}",
                        color = SoftCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    TextButton(
                        onClick = onRemix,
                        modifier = Modifier.testTag("remix_btn_${project.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Remix Prompt", color = NeonCyan, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
