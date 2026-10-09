package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.User
import com.example.ui.theme.*

enum class AppDestination(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    STUDIO("Create Video", Icons.Default.Videocam),
    STYLE_EXPLORER("Style Explorer", Icons.Default.Palette),
    MY_PROJECTS("My Projects", Icons.Default.FolderSpecial),
    ADMIN_PANEL("Owner Admin Panel", Icons.Default.AdminPanelSettings),
    SETTINGS_AND_DOCS("Settings & Model Docs", Icons.Default.Settings)
}

@Composable
fun MotionVerseDrawer(
    currentUser: User,
    currentDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    onAuthClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(MidnightNavy)
            .border(1.dp, GlassBorder)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        // App Logo & Branding
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCardElevated)
                    .border(1.dp, NeonCyan, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_motionverse_logo),
                    contentDescription = "MotionVerse Logo",
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Motion",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Verse",
                        color = NeonCyan,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                }
                Text(
                    text = "Seedance 2.5 Cinema Engine",
                    color = SoftCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Divider(color = GlassBorder, modifier = Modifier.padding(vertical = 8.dp))

        // Navigation Menu List
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "CREATIVE STUDIO",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 4.dp)
            )

            DrawerItem(
                destination = AppDestination.HOME,
                isSelected = currentDestination == AppDestination.HOME,
                onClick = { onNavigate(AppDestination.HOME) }
            )

            DrawerItem(
                destination = AppDestination.STUDIO,
                isSelected = currentDestination == AppDestination.STUDIO,
                badge = "Seedance 2.5",
                onClick = { onNavigate(AppDestination.STUDIO) }
            )

            DrawerItem(
                destination = AppDestination.STYLE_EXPLORER,
                isSelected = currentDestination == AppDestination.STYLE_EXPLORER,
                badge = "20 Styles",
                onClick = { onNavigate(AppDestination.STYLE_EXPLORER) }
            )

            DrawerItem(
                destination = AppDestination.MY_PROJECTS,
                isSelected = currentDestination == AppDestination.MY_PROJECTS,
                onClick = { onNavigate(AppDestination.MY_PROJECTS) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "MANAGEMENT & SYSTEM",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 4.dp)
            )

            DrawerItem(
                destination = AppDestination.ADMIN_PANEL,
                isSelected = currentDestination == AppDestination.ADMIN_PANEL,
                badge = if (currentUser.isOwner) "Owner Access" else "Locked",
                badgeColor = if (currentUser.isOwner) NeonGreen else AmberGlow,
                onClick = { onNavigate(AppDestination.ADMIN_PANEL) }
            )

            DrawerItem(
                destination = AppDestination.SETTINGS_AND_DOCS,
                isSelected = currentDestination == AppDestination.SETTINGS_AND_DOCS,
                onClick = { onNavigate(AppDestination.SETTINGS_AND_DOCS) }
            )
        }

        Divider(color = GlassBorder, modifier = Modifier.padding(vertical = 8.dp))

        // User Account Bar at bottom of drawer
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                .clickable { onAuthClick() },
            color = SurfaceCard
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                if (currentUser.isOwner) listOf(ElectricViolet, NeonCyan) else listOf(SurfaceCardElevated, CyberBlue)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser.displayName.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currentUser.displayName,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    }
                    Text(
                        text = if (currentUser.isOwner) "Owner Account (Verified)" else "Free Creator Tier",
                        color = if (currentUser.isOwner) NeonCyan else TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = "Switch Account",
                    tint = SoftCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun DrawerItem(
    destination: AppDestination,
    isSelected: Boolean,
    badge: String? = null,
    badgeColor: Color = DeepViolet,
    onClick: () -> Unit
) {
    val backgroundBrush = if (isSelected) {
        Brush.horizontalGradient(
            listOf(SurfaceCardElevated, DeepViolet.copy(alpha = 0.6f))
        )
    } else {
        null
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("drawer_item_${destination.name.lowercase()}")
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (isSelected) {
                    Modifier.border(1.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                } else Modifier
            )
            .clickable(onClick = onClick),
        color = if (isSelected) SurfaceCardElevated else Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = destination.icon,
                contentDescription = null,
                tint = if (isSelected) NeonCyan else TextSecondary,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = destination.label,
                color = if (isSelected) TextPrimary else TextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )

            if (badge != null) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeColor
                ) {
                    Text(
                        text = badge,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
