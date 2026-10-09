package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.FuturisticButton
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminPanelScreen(
    currentUser: User,
    adminStats: AdminStats,
    providers: List<ProviderConfig>,
    users: List<User>,
    auditLogs: List<AdminLog>,
    onUpdateProvider: (
        providerId: String,
        endpoint: String,
        apiKey: String,
        modelId: String,
        isEnabled: Boolean,
        isPrimary: Boolean
    ) -> Boolean,
    onTestProvider: (providerId: String) -> String,
    onSetDailyQuota: (Int) -> Boolean,
    onToggleMaintenance: (Boolean) -> Boolean,
    onToggleUserSuspension: (String) -> Boolean,
    onResetUserCredits: (String) -> Boolean,
    onSwitchToOwner: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Strict Owner Security Enforcement
    if (!currentUser.isOwner) {
        AccessDeniedOwnerGuard(
            currentUser = currentUser,
            onSwitchToOwner = onSwitchToOwner,
            modifier = modifier
        )
        return
    }

    var activeAdminTab by remember { mutableStateOf("Overview") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Owner Shield Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = SurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(DeepViolet),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "OWNER CONTROL PANEL",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = NeonGreen.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "VERIFIED",
                                    color = NeonGreen,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Authenticated Principal: ${currentUser.email}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Admin Sub-Navigation Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Overview", "Providers", "Quotas", "Users", "Audit Logs").forEach { tab ->
                val isSelected = activeAdminTab == tab
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .testTag("admin_tab_${tab.lowercase().replace(" ", "_")}")
                        .clickable { activeAdminTab = tab },
                    color = if (isSelected) DeepViolet else SurfaceCard,
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, NeonCyan) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = tab,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tab Content
        when (activeAdminTab) {
            "Overview" -> AdminOverviewTab(adminStats = adminStats, onToggleMaintenance = onToggleMaintenance)
            "Providers" -> AdminProvidersTab(
                providers = providers,
                onUpdateProvider = onUpdateProvider,
                onTestProvider = onTestProvider
            )
            "Quotas" -> AdminQuotasTab(
                adminStats = adminStats,
                onSetDailyQuota = onSetDailyQuota
            )
            "Users" -> AdminUsersTab(
                users = users,
                onToggleSuspension = onToggleUserSuspension,
                onResetCredits = onResetUserCredits
            )
            "Audit Logs" -> AdminAuditLogsTab(logs = auditLogs)
        }
    }
}

@Composable
private fun AdminOverviewTab(
    adminStats: AdminStats,
    onToggleMaintenance: (Boolean) -> Boolean
) {
    val context = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Quick Metrics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminStatCard(
                    title = "Total Users",
                    value = adminStats.totalUsers.toString(),
                    subtext = "${adminStats.activeUsersToday} Active Today",
                    icon = Icons.Default.People,
                    color = CyberBlue,
                    modifier = Modifier.weight(1f)
                )

                AdminStatCard(
                    title = "Total Jobs",
                    value = adminStats.totalGenerations.toString(),
                    subtext = "${adminStats.successfulGenerations} Success",
                    icon = Icons.Default.VideoLibrary,
                    color = NeonCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AdminStatCard(
                    title = "GPU Server Load",
                    value = "${adminStats.serverLoadPercent}%",
                    subtext = "Queue: ${adminStats.queueDepth} waiting",
                    icon = Icons.Default.Speed,
                    color = if (adminStats.serverLoadPercent > 80) AmberGlow else NeonGreen,
                    modifier = Modifier.weight(1f)
                )

                AdminStatCard(
                    title = "Success Rate",
                    value = "${((adminStats.successfulGenerations.toFloat() / adminStats.totalGenerations.coerceAtLeast(1)) * 100).toInt()}%",
                    subtext = "${adminStats.failedGenerations} Errors Logged",
                    icon = Icons.Default.CheckCircle,
                    color = DeepViolet,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Maintenance Mode Toggle Card
        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Platform Maintenance Mode",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "When active, non-owner users cannot submit new video generation jobs while GPU clusters upgrade.",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Switch(
                        checked = adminStats.maintenanceMode,
                        onCheckedChange = {
                            onToggleMaintenance(it)
                            Toast.makeText(context, "Maintenance mode: $it", Toast.LENGTH_SHORT).show()
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = AmberGlow
                        )
                    )
                }
            }
        }

        // Live Seedance 2.5 Node Status
        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SEEDANCE 2.5 CLUSTER HEALTH",
                        color = SoftCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ClusterNodeRow(node = "seedance-node-us-east-1", status = "Online (Latency 48ms)", vram = "94 / 128 GB")
                    ClusterNodeRow(node = "seedance-node-eu-central", status = "Online (Latency 62ms)", vram = "82 / 128 GB")
                    ClusterNodeRow(node = "cogvideo-free-tier-worker", status = "Active (Queued 0)", vram = "44 / 64 GB")
                }
            }
        }
    }
}

@Composable
private fun AdminProvidersTab(
    providers: List<ProviderConfig>,
    onUpdateProvider: (String, String, String, String, Boolean, Boolean) -> Boolean,
    onTestProvider: (String) -> String
) {
    val context = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(providers, key = { it.id }) { provider ->
            var endpoint by remember(provider.apiEndpoint) { mutableStateOf(provider.apiEndpoint) }
            var modelId by remember(provider.modelIdentifier) { mutableStateOf(provider.modelIdentifier) }
            var apiKeyInput by remember { mutableStateOf("") }
            var isEnabled by remember(provider.isEnabled) { mutableStateOf(provider.isEnabled) }
            var isPrimary by remember(provider.isPrimary) { mutableStateOf(provider.isPrimary) }
            var testStatusText by remember { mutableStateOf<String?>(null) }

            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = provider.name,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                if (provider.isPrimary) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = DeepViolet
                                    ) {
                                        Text(
                                            text = "PRIMARY",
                                            color = NeonCyan,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Status: ${provider.statusBadge}",
                                color = if (provider.isEnabled) NeonGreen else TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = if (isEnabled) "Active" else "Disabled", color = TextSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = isEnabled,
                                onCheckedChange = { isEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = NeonGreen)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "API Base URL / Endpoint", color = TextSecondary, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = endpoint,
                        onValueChange = { endpoint = it },
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
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Model Identifier", color = TextSecondary, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = modelId,
                        onValueChange = { modelId = it },
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
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Secret API Key (Current: ${provider.apiKeyMasked})", color = TextSecondary, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = apiKeyInput,
                        onValueChange = { apiKeyInput = it },
                        placeholder = { Text("Enter new secret key to update", color = TextMuted, fontSize = 11.sp) },
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
                        singleLine = true
                    )

                    if (testStatusText != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = testStatusText!!,
                            color = NeonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val result = onTestProvider(provider.id)
                                testStatusText = result
                                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderHighlight)
                        ) {
                            Icon(imageVector = Icons.Default.Sensors, contentDescription = null, tint = SoftCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Test Ping", color = TextPrimary, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val ok = onUpdateProvider(provider.id, endpoint, apiKeyInput, modelId, isEnabled, isPrimary)
                                if (ok) {
                                    apiKeyInput = ""
                                    Toast.makeText(context, "Provider saved successfully!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberBlue)
                        ) {
                            Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminQuotasTab(
    adminStats: AdminStats,
    onSetDailyQuota: (Int) -> Boolean
) {
    val context = LocalContext.current
    var dailyLimit by remember(adminStats.dailyFreeLimitPerUser) { mutableIntStateOf(adminStats.dailyFreeLimitPerUser) }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "FREE TIER USAGE POLICIES",
                        color = SoftCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Daily Free Video Quota Per User: $dailyLimit videos",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Slider(
                        value = dailyLimit.toFloat(),
                        onValueChange = { dailyLimit = it.toInt() },
                        valueRange = 1f..50f,
                        steps = 49,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = SurfaceDark
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Standard creators receive this number of free high-fidelity video credits every 24 hours without paying any subscription fees.",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            onSetDailyQuota(dailyLimit)
                            Toast.makeText(context, "Daily quota updated to $dailyLimit videos/day", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepViolet),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save Quota Policy", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminUsersTab(
    users: List<User>,
    onToggleSuspension: (String) -> Boolean,
    onResetCredits: (String) -> Boolean
) {
    val context = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(users, key = { it.id }) { user ->
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = user.displayName,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (user.isOwner) DeepViolet else SurfaceDark
                            ) {
                                Text(
                                    text = user.role.name,
                                    color = if (user.isOwner) NeonCyan else TextSecondary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(text = user.email, color = TextMuted, fontSize = 11.sp)

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Credits: ${user.freeCreditsRemaining}/${user.dailyQuotaLimit} • Status: ${if (user.isSuspended) "SUSPENDED" else "ACTIVE"}",
                            color = if (user.isSuspended) ErrorGlow else SoftCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (!user.isOwner) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            IconButton(
                                onClick = {
                                    onResetCredits(user.id)
                                    Toast.makeText(context, "Credits reset for ${user.displayName}", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceCardElevated)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restore,
                                    contentDescription = "Reset Credits",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    onToggleSuspension(user.id)
                                    Toast.makeText(context, "User status updated", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceCardElevated)
                            ) {
                                Icon(
                                    imageVector = if (user.isSuspended) Icons.Default.Check else Icons.Default.Block,
                                    contentDescription = "Suspend",
                                    tint = if (user.isSuspended) NeonGreen else ErrorGlow,
                                    modifier = Modifier.size(18.dp)
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
private fun AdminAuditLogsTab(logs: List<AdminLog>) {
    val dateFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(logs, key = { it.id }) { log ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = SurfaceDark,
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    val badgeColor = when (log.level) {
                        "SECURITY" -> NeonPink
                        "ERROR" -> ErrorGlow
                        "WARN" -> AmberGlow
                        else -> CyberBlue
                    }

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = badgeColor.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = log.level,
                            color = badgeColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = log.action,
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = dateFormat.format(Date(log.timestamp)),
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = log.details,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminStatCard(
    title: String,
    value: String,
    subtext: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    GlassmorphicCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            }
            Text(text = value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text(text = subtext, color = SoftCyan, fontSize = 10.sp)
        }
    }
}

@Composable
private fun ClusterNodeRow(node: String, status: String, vram: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = node, color = TextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            Text(text = status, color = NeonGreen, fontSize = 10.sp)
        }
        Text(text = "VRAM: $vram", color = SoftCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AccessDeniedOwnerGuard(
    currentUser: User,
    onSwitchToOwner: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            borderBrush = Brush.horizontalGradient(listOf(ErrorGlow, NeonPink)),
            elevation = 14.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(ErrorGlow.copy(alpha = 0.2f))
                        .border(1.5.dp, ErrorGlow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = ErrorGlow,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Owner Authorization Required",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "This area is strictly restricted to the verified platform administrator (masabsajid8@gmail.com). Current account '${currentUser.email}' has role '${currentUser.role}' and is not authorized.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = AmberGlow, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SECURITY AUDIT: Unauthorized access attempt logged.",
                            color = AmberGlow,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                FuturisticButton(
                    text = "Switch to Verified Owner Account",
                    icon = Icons.Default.Key,
                    onClick = onSwitchToOwner,
                    gradientColors = listOf(DeepViolet, CyberBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("switch_to_owner_btn")
                )
            }
        }
    }
}
