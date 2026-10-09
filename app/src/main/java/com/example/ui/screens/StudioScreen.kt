package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
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
fun StudioScreen(
    currentUser: User,
    projects: List<VideoProject>,
    initialPrompt: String? = null,
    initialMode: GenerationMode? = null,
    initialStyle: String? = null,
    onGenerate: (
        prompt: String,
        enhancedPrompt: String?,
        negativePrompt: String?,
        mode: GenerationMode,
        style: String,
        aspectRatio: VideoAspectRatio,
        duration: Int,
        resolution: String,
        camera: String,
        motionPreset: String,
        motionIntensity: Float,
        referenceImageRes: Int?
    ) -> Unit,
    onCancelJob: (String) -> Unit,
    onOpenProject: (VideoProject) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    // State Variables
    var currentMode by remember { mutableStateOf(initialMode ?: GenerationMode.TEXT_TO_VIDEO) }
    var promptText by remember {
        mutableStateOf(
            initialPrompt ?: "A cinematic aerial shot of a futuristic metropolis at sunset, flying vehicles passing between illuminated glass skyscrapers, volumetric purple lighting, smooth camera movement."
        )
    }
    var negativePromptText by remember { mutableStateOf("blurry, low quality, distorted, bad anatomy, artifacts") }
    var selectedStyle by remember { mutableStateOf(initialStyle ?: "Cinematic Realism") }
    var selectedAspectRatio by remember { mutableStateOf(VideoAspectRatio.RATIO_16_9) }
    var selectedDurationSeconds by remember { mutableIntStateOf(6) }
    var selectedResolution by remember { mutableStateOf("1080p") }
    var selectedCameraMovement by remember { mutableStateOf("Cinematic Dolly Zoom") }
    var selectedMotionPreset by remember { mutableStateOf("Hip-hop dance") }
    var motionIntensity by remember { mutableFloatStateOf(0.75f) }
    var selectedReferenceImageRes by remember { mutableStateOf<Int?>(R.drawable.img_dance_motion) }

    // Advanced Controls Accordion
    var showAdvancedControls by remember { mutableStateOf(false) }
    var showEnhancerDialog by remember { mutableStateOf(false) }
    var enhancedPromptResultText by remember { mutableStateOf<String?>(null) }

    // Find if there is an active running project
    val activeRunningProject = projects.firstOrNull {
        it.status != GenerationStatus.COMPLETED && it.status != GenerationStatus.FAILED
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Active Job Progress Card (If any job is currently generating)
        if (activeRunningProject != null) {
            item {
                ActiveJobProgressCard(
                    project = activeRunningProject,
                    onCancel = { onCancelJob(activeRunningProject.id) }
                )
            }
        }

        // Mode Selector Tabs (Text to Video, Image to Video, Dance & Motion)
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    GenerationMode.values().forEach { mode ->
                        val isSelected = currentMode == mode
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .testTag("studio_tab_${mode.name.lowercase()}")
                                .clickable { currentMode = mode },
                            color = if (isSelected) DeepViolet else Color.Transparent,
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, NeonCyan) else null
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = mode.label,
                                    color = if (isSelected) Color.White else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Dedicated Mode Header Details
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = SurfaceCardElevated.copy(alpha = 0.5f),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (currentMode) {
                            GenerationMode.TEXT_TO_VIDEO -> Icons.Default.TextFields
                            GenerationMode.IMAGE_TO_VIDEO -> Icons.Default.Image
                            GenerationMode.DANCE_AND_MOTION -> Icons.Default.SportsKabaddi
                        },
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = currentMode.label,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = currentMode.description,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Image-to-Video Reference Media Section
        if (currentMode == GenerationMode.IMAGE_TO_VIDEO) {
            item {
                ImageToVideoSelector(
                    selectedRes = selectedReferenceImageRes,
                    onSelect = { selectedReferenceImageRes = it }
                )
            }
        }

        // Dance & Motion Presets Section
        if (currentMode == GenerationMode.DANCE_AND_MOTION) {
            item {
                DanceAndMotionControls(
                    selectedPreset = selectedMotionPreset,
                    onPresetSelect = { selectedMotionPreset = it },
                    intensity = motionIntensity,
                    onIntensityChange = { motionIntensity = it }
                )
            }
        }

        // Main Prompt Editor Card
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header Row with Character Counter and AI Enhancer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PROMPT DESCRIPTION",
                            color = SoftCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "${promptText.length} / 1000",
                            color = if (promptText.length > 800) AmberGlow else TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Multiline Prompt TextField
                    OutlinedTextField(
                        value = promptText,
                        onValueChange = { promptText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp, max = 220.dp)
                            .testTag("studio_prompt_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceDark,
                            unfocusedContainerColor = SurfaceDark,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        placeholder = {
                            Text(
                                text = "Describe your video in detail. Example: A cinematic aerial shot of a futuristic city at sunset, flying vehicles passing between illuminated skyscrapers, realistic reflections, volumetric lighting, smooth camera movement, ultra-detailed cinematic atmosphere.",
                                color = TextMuted,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Prompt Actions Bar (Enhance, Random, Clear, Copy)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // AI Enhance Button
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = DeepViolet,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .testTag("prompt_enhancer_button")
                                .clickable {
                                    val enhanced = PromptEnhancerService.enhance(
                                        userPrompt = promptText,
                                        mode = currentMode,
                                        cameraPreset = selectedCameraMovement,
                                        stylePreset = selectedStyle
                                    )
                                    enhancedPromptResultText = enhanced.fullPrompt
                                    showEnhancerDialog = true
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "AI Enhancer",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Random Sample
                            IconButton(
                                onClick = {
                                    promptText = PromptEnhancerService.samplePrompts.random()
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceCardElevated)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Casino,
                                    contentDescription = "Random Prompt",
                                    tint = SoftCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Copy Prompt
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(promptText))
                                    Toast.makeText(context, "Prompt copied!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceCardElevated)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Prompt",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Clear Prompt
                            IconButton(
                                onClick = { promptText = "" },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceCardElevated)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Prompt",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Video Settings Card (Aspect Ratio, Duration, Resolution, Camera)
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "VIDEO SETTINGS & ENGINE",
                        color = SoftCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Aspect Ratio Selector
                    Text(text = "Aspect Ratio", color = TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VideoAspectRatio.values().forEach { ratio ->
                            val isSelected = selectedAspectRatio == ratio
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .testTag("ratio_${ratio.name.lowercase()}")
                                    .clickable { selectedAspectRatio = ratio },
                                color = if (isSelected) DeepViolet else SurfaceDark,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, NeonCyan) else androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = ratio.label,
                                        color = if (isSelected) Color.White else TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Duration & Resolution Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Duration (Seconds)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Duration", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(4, 6, 10).forEach { sec ->
                                    val isSelected = selectedDurationSeconds == sec
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { selectedDurationSeconds = sec },
                                        color = if (isSelected) CyberBlue else SurfaceDark,
                                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${sec}s",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Resolution
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Resolution", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("720p", "1080p", "4k").forEach { res ->
                                    val isSelected = selectedResolution.equals(res, ignoreCase = true)
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { selectedResolution = res },
                                        color = if (isSelected) CyberBlue else SurfaceDark,
                                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = res.uppercase(),
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Model Badge
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SurfaceDark,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderHighlight)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Memory,
                                    contentDescription = null,
                                    tint = NeonGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Active Engine: Seedance 2.5 Cinema",
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "High-fidelity temporal latent diffusion (24-60 fps)",
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = NeonGreen.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Ready",
                                    color = NeonGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Expandable Advanced Prompt Controls (Camera, Lighting, Negative Prompt)
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAdvancedControls = !showAdvancedControls },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = SoftCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ADVANCED CINEMATIC CONTROLS",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Icon(
                            imageVector = if (showAdvancedControls) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = SoftCyan
                        )
                    }

                    AnimatedVisibility(visible = showAdvancedControls) {
                        Column(modifier = Modifier.padding(top = 14.dp)) {
                            // Camera Movement Preset
                            Text(text = "Camera Movement", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(
                                    listOf(
                                        "Cinematic Dolly Zoom",
                                        "Drone Flythrough",
                                        "Slow Orbit 360",
                                        "Low Angle Swivel Follow",
                                        "FPV Drone Dive",
                                        "Static Tripod"
                                    )
                                ) { cam ->
                                    val isSelected = selectedCameraMovement == cam
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) DeepViolet else SurfaceDark,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) NeonCyan else GlassBorder
                                        ),
                                        modifier = Modifier.clickable { selectedCameraMovement = cam }
                                    ) {
                                        Text(
                                            text = cam,
                                            color = if (isSelected) Color.White else TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Negative Prompt
                            Text(text = "Negative Prompt (What to exclude)", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = negativePromptText,
                                onValueChange = { negativePromptText = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = SurfaceDark,
                                    unfocusedContainerColor = SurfaceDark,
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = GlassBorder,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }

        // Primary 3D Generate Video Button & Free Credit Usage Notice
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                val isGenerating = activeRunningProject != null

                FuturisticButton(
                    text = if (isGenerating) "Synthesizing Video..." else "Generate Video",
                    icon = if (isGenerating) null else Icons.Default.VideoCall,
                    isLoading = isGenerating,
                    enabled = promptText.isNotBlank() && !isGenerating,
                    onClick = {
                        if (promptText.isBlank()) {
                            Toast.makeText(context, "Please describe your video scene first.", Toast.LENGTH_SHORT).show()
                            return@FuturisticButton
                        }

                        onGenerate(
                            promptText,
                            enhancedPromptResultText,
                            negativePromptText,
                            currentMode,
                            selectedStyle,
                            selectedAspectRatio,
                            selectedDurationSeconds,
                            selectedResolution,
                            selectedCameraMovement,
                            selectedMotionPreset,
                            motionIntensity,
                            selectedReferenceImageRes
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("generate_video_main_button")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = SoftCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (currentUser.isOwner)
                            "Owner Privilege: Unlimited instant GPU cluster generation"
                        else
                            "Uses 1 Free Credit (${currentUser.freeCreditsRemaining} of ${currentUser.dailyQuotaLimit} remaining today)",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }

    // AI Prompt Enhancer Dialog
    if (showEnhancerDialog && enhancedPromptResultText != null) {
        AlertDialog(
            onDismissRequest = { showEnhancerDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Prompt Enhancer", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Seedance 2.5 has synthesized a complete cinematographic prompt with camera, lighting, and physics details:",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = enhancedPromptResultText ?: "",
                        onValueChange = { enhancedPromptResultText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 140.dp, max = 220.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceDark,
                            unfocusedContainerColor = SurfaceDark,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        promptText = enhancedPromptResultText ?: promptText
                        showEnhancerDialog = false
                        Toast.makeText(context, "Enhanced prompt applied!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberBlue)
                ) {
                    Text("Apply to Editor", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEnhancerDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SurfaceCard,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun ActiveJobProgressCard(
    project: VideoProject,
    onCancel: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("active_job_progress_card"),
        borderBrush = Brush.horizontalGradient(listOf(NeonCyan, DeepViolet, NeonPink)),
        elevation = 10.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        progress = { project.progressPercent / 100f },
                        modifier = Modifier.size(24.dp),
                        color = NeonCyan,
                        trackColor = SurfaceCardElevated
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "SYNTHESIZING VIDEO",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${project.progressPercent}% • ${project.status.label}",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ErrorGlow.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorGlow)
                ) {
                    Text("Cancel", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Stage Description
            Text(
                text = project.currentStage,
                color = SoftCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Animated Linear Progress Indicator
            LinearProgressIndicator(
                progress = { project.progressPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = NeonCyan,
                trackColor = SurfaceDark
            )
        }
    }
}

@Composable
private fun ImageToVideoSelector(
    selectedRes: Int?,
    onSelect: (Int) -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "SOURCE IMAGE SELECTION",
                color = SoftCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    R.drawable.img_dance_motion to "Dancer Reference",
                    R.drawable.img_cinematic_scifi to "Astronaut Explorer",
                    R.drawable.img_hero_cyberpunk to "Metropolis Scene"
                ).forEach { (resId, label) ->
                    val isSelected = selectedRes == resId
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                2.dp,
                                if (isSelected) NeonCyan else GlassBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelect(resId) },
                        color = SurfaceDark
                    ) {
                        Box {
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = label,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                        )
                                    )
                                    .padding(6.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Text(
                                    text = label,
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DanceAndMotionControls(
    selectedPreset: String,
    onPresetSelect: (String) -> Unit,
    intensity: Float,
    onIntensityChange: (Float) -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "DANCE & CHOREOGRAPHY PRESETS",
                color = SoftCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(
                    listOf(
                        "Hip-hop dance",
                        "Contemporary",
                        "Cinematic Character",
                        "Music Video Performance",
                        "Stylized Animation",
                        "Slow-motion Flow",
                        "Fantasy Character"
                    )
                ) { preset ->
                    val isSelected = selectedPreset == preset
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) DeepViolet else SurfaceDark,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) NeonPink else GlassBorder
                        ),
                        modifier = Modifier.clickable { onPresetSelect(preset) }
                    ) {
                        Text(
                            text = preset,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Motion Energy Dynamics", color = TextSecondary, fontSize = 12.sp)
                Text(text = "${(intensity * 100).toInt()}%", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Slider(
                value = intensity,
                onValueChange = onIntensityChange,
                valueRange = 0.2f..1.0f,
                colors = SliderDefaults.colors(
                    thumbColor = NeonPink,
                    activeTrackColor = NeonPink,
                    inactiveTrackColor = SurfaceDark
                )
            )
        }
    }
}
