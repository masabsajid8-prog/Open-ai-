package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.data.model.VideoStyleItem
import com.example.data.model.VideoStylesCatalog
import com.example.ui.components.FuturisticButton
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.*

@Composable
fun StyleExplorerScreen(
    onSelectStyle: (VideoStyleItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedDetailStyle by remember { mutableStateOf<VideoStyleItem?>(null) }

    val filteredStyles = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            VideoStylesCatalog.categories
        } else {
            VideoStylesCatalog.categories.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.tag.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search 20 cinematic styles...", color = TextMuted, fontSize = 13.sp) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = SoftCyan)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("style_search_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = GlassBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Grid of Styles
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredStyles) { style ->
                StyleCard(
                    style = style,
                    onClick = { selectedDetailStyle = style }
                )
            }
        }
    }

    // Style Detail BottomSheet / Modal
    if (selectedDetailStyle != null) {
        val style = selectedDetailStyle!!
        val colors = style.previewGradientColors.map { Color(it) }

        AlertDialog(
            onDismissRequest = { selectedDetailStyle = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = colors[0].copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = style.tag,
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = style.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = style.description,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SurfaceDark,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "SUGGESTED CAMERA", color = SoftCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(text = style.suggestedCamera, color = TextPrimary, fontSize = 12.sp)

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(text = "SUGGESTED LIGHTING", color = SoftCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(text = style.suggestedLighting, color = TextPrimary, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "PROMPT MODIFIER", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = style.promptModifier,
                        color = TextTertiary,
                        fontSize = 11.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            },
            confirmButton = {
                FuturisticButton(
                    text = "Apply to Studio",
                    icon = Icons.Default.Check,
                    onClick = {
                        onSelectStyle(style)
                        selectedDetailStyle = null
                    },
                    modifier = Modifier.testTag("apply_style_btn")
                )
            },
            dismissButton = {
                TextButton(onClick = { selectedDetailStyle = null }) {
                    Text("Close", color = TextSecondary)
                }
            },
            containerColor = SurfaceCard,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun StyleCard(
    style: VideoStyleItem,
    onClick: () -> Unit
) {
    val colors = style.previewGradientColors.map { Color(it) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("style_card_${style.id}"),
        color = SurfaceCard
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            colors[0].copy(alpha = 0.35f),
                            SurfaceCardElevated.copy(alpha = 0.85f),
                            SurfaceCard
                        )
                    )
                )
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Black.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = style.tag,
                            color = NeonCyan,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = SoftCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Column {
                    Text(
                        text = style.name,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = style.description,
                        color = TextMuted,
                        fontSize = 10.sp,
                        maxLines = 2,
                        lineHeight = 13.sp
                    )
                }
            }
        }
    }
}
