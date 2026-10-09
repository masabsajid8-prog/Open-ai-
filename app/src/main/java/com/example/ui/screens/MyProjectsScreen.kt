package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.model.GenerationStatus
import com.example.data.model.VideoProject
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MyProjectsScreen(
    projects: List<VideoProject>,
    onOpenProject: (VideoProject) -> Unit,
    onDeleteProject: (String) -> Unit,
    onRetryProject: (String) -> Unit,
    onRemixProject: (VideoProject) -> Unit,
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var selectedFilter by remember { mutableStateOf("All") }

    val filteredProjects = remember(projects, selectedFilter) {
        when (selectedFilter) {
            "Ready" -> projects.filter { it.status == GenerationStatus.COMPLETED }
            "In Progress" -> projects.filter { it.status != GenerationStatus.COMPLETED && it.status != GenerationStatus.FAILED }
            "Failed" -> projects.filter { it.status == GenerationStatus.FAILED }
            else -> projects
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Filter Chips Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Ready", "In Progress", "Failed").forEach { filter ->
                val isSelected = selectedFilter == filter
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) DeepViolet else SurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) NeonCyan else GlassBorder
                    ),
                    modifier = Modifier
                        .testTag("filter_chip_${filter.lowercase().replace(" ", "_")}")
                        .clickable { selectedFilter = filter }
                ) {
                    Text(
                        text = "$filter (${countForFilter(projects, filter)})",
                        color = if (isSelected) Color.White else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredProjects.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 96.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(SurfaceCardElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideocamOff,
                            contentDescription = null,
                            tint = SoftCyan,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "No Video Projects Found",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Your generated Seedance 2.5 videos and active jobs will appear here.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = onNavigateToCreate,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Create First Video", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProjects, key = { it.id }) { project ->
                    ProjectHistoryCard(
                        project = project,
                        onOpen = { onOpenProject(project) },
                        onDelete = { onDeleteProject(project.id) },
                        onRetry = { onRetryProject(project.id) },
                        onRemix = { onRemixProject(project) },
                        onCopyPrompt = {
                            clipboardManager.setText(AnnotatedString(project.prompt))
                            Toast.makeText(context, "Prompt copied!", Toast.LENGTH_SHORT).show()
                        },
                        onDownload = {
                            Toast.makeText(context, "Downloading ${project.resolution} MP4 video...", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

private fun countForFilter(projects: List<VideoProject>, filter: String): Int {
    return when (filter) {
        "Ready" -> projects.count { it.status == GenerationStatus.COMPLETED }
        "In Progress" -> projects.count { it.status != GenerationStatus.COMPLETED && it.status != GenerationStatus.FAILED }
        "Failed" -> projects.count { it.status == GenerationStatus.FAILED }
        else -> projects.size
    }
}

@Composable
private fun ProjectHistoryCard(
    project: VideoProject,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    onRetry: () -> Unit,
    onRemix: () -> Unit,
    onCopyPrompt: () -> Unit,
    onDownload: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()) }
    val dateString = remember(project.createdAt) { dateFormat.format(Date(project.createdAt)) }

    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("project_item_${project.id}"),
        onClick = if (project.status == GenerationStatus.COMPLETED) onOpen else null
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Thumbnail or Status Box
                Box(
                    modifier = Modifier
                        .size(100.dp, 75.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceDark)
                        .border(1.dp, GlassBorder, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (project.previewDrawableRes != null) {
                        Image(
                            painter = painterResource(id = project.previewDrawableRes),
                            contentDescription = project.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    if (project.status == GenerationStatus.COMPLETED) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else if (project.status == GenerationStatus.FAILED) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = "Failed",
                            tint = ErrorGlow,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = NeonCyan,
                            strokeWidth = 2.5.dp
                        )
                    }
                }

                // Info Column
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(status = project.status)
                        Text(text = dateString, color = TextMuted, fontSize = 10.sp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = project.title,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = project.prompt,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        maxLines = 2,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Spec row & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${project.providerUsed} • ${project.aspectRatio.label} • ${project.durationSeconds}s • ${project.resolution}",
                    color = SoftCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (project.status == GenerationStatus.COMPLETED) {
                        IconButton(onClick = onDownload, modifier = Modifier.size(32.dp)) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = "Download", tint = SoftCyan, modifier = Modifier.size(16.dp))
                        }
                    }

                    IconButton(onClick = onCopyPrompt, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Prompt", tint = TextSecondary, modifier = Modifier.size(16.dp))
                    }

                    if (project.status == GenerationStatus.FAILED) {
                        IconButton(onClick = onRetry, modifier = Modifier.size(32.dp)) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Retry", tint = AmberGlow, modifier = Modifier.size(16.dp))
                        }
                    }

                    IconButton(onClick = onRemix, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Remix", tint = NeonCyan, modifier = Modifier.size(16.dp))
                    }

                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: GenerationStatus) {
    val (color, text) = when (status) {
        GenerationStatus.COMPLETED -> NeonGreen to "Ready"
        GenerationStatus.FAILED -> ErrorGlow to "Failed"
        else -> NeonCyan to status.label
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
