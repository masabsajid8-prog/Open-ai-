package com.example.ui.components

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.ui.theme.*

@Composable
fun TopNavBar(
    currentUser: User,
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = VoidBlack
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Hamburger Menu + Logo Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier
                        .testTag("nav_menu_button")
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceCard)
                        .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open Navigation",
                        tint = TextPrimary
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "MOTION",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "VERSE",
                            color = NeonCyan,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = DeepViolet
                        ) {
                            Text(
                                text = "2.5",
                                color = TextPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Seedance AI Studio",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            // Right: Credits indicator & User Profile
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Free Quota / Credits Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (currentUser.isOwner) DeepViolet.copy(alpha = 0.7f) else SurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (currentUser.isOwner) NeonCyan else GlassBorderHighlight
                    ),
                    modifier = Modifier
                        .testTag("credits_indicator")
                        .clickable { onProfileClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (currentUser.isOwner) Icons.Default.Shield else Icons.Default.Bolt,
                            contentDescription = null,
                            tint = if (currentUser.isOwner) NeonCyan else AmberGlow,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (currentUser.isOwner) "Owner: Unlimited" else "${currentUser.freeCreditsRemaining}/${currentUser.dailyQuotaLimit} Free",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                // Profile Avatar Button
                Box(
                    modifier = Modifier
                        .testTag("user_avatar_button")
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                if (currentUser.isOwner) listOf(ElectricViolet, NeonCyan) else listOf(SurfaceCardElevated, CyberBlue)
                            )
                        )
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser.displayName.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
