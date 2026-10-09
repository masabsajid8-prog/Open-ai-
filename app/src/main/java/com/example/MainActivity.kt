package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.*
import com.example.data.repository.MotionVerseRepository
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val repository = MotionVerseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MotionVerseApp(repository = repository)
            }
        }
    }
}

@Composable
fun MotionVerseApp(repository: MotionVerseRepository) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Reactive State from Repository
    val currentUser by repository.currentUser.collectAsStateWithLifecycle()
    val projects by repository.projects.collectAsStateWithLifecycle()
    val providers by repository.providers.collectAsStateWithLifecycle()
    val adminStats by repository.adminStats.collectAsStateWithLifecycle()
    val adminLogs by repository.adminLogs.collectAsStateWithLifecycle()
    val allUsers by repository.allUsers.collectAsStateWithLifecycle()

    // Navigation and Modal State
    var currentDestination by remember { mutableStateOf(AppDestination.HOME) }
    var selectedProjectForPlayback by remember { mutableStateOf<VideoProject?>(null) }
    var showAuthDialog by remember { mutableStateOf(false) }

    // Studio transfer parameters
    var studioInitialPrompt by remember { mutableStateOf<String?>(null) }
    var studioInitialMode by remember { mutableStateOf<GenerationMode?>(null) }
    var studioInitialStyle by remember { mutableStateOf<String?>(null) }

    // Back handling: If on a sub-screen, pressing Back navigates to Home
    BackHandler(enabled = currentDestination != AppDestination.HOME) {
        currentDestination = AppDestination.HOME
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MidnightNavy,
                modifier = Modifier.width(300.dp)
            ) {
                MotionVerseDrawer(
                    currentUser = currentUser,
                    currentDestination = currentDestination,
                    onNavigate = { destination ->
                        currentDestination = destination
                        coroutineScope.launch { drawerState.close() }
                    },
                    onAuthClick = {
                        showAuthDialog = true
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = VoidBlack,
            topBar = {
                TopNavBar(
                    currentUser = currentUser,
                    onMenuClick = {
                        coroutineScope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    },
                    onProfileClick = { showAuthDialog = true },
                    onSearchClick = { currentDestination = AppDestination.STYLE_EXPLORER }
                )
            },
            bottomBar = {
                // Mobile-friendly Bottom Navigation Bar
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    color = MidnightNavy,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.Home,
                            label = "Home",
                            isSelected = currentDestination == AppDestination.HOME,
                            onClick = { currentDestination = AppDestination.HOME }
                        )

                        BottomNavItem(
                            icon = Icons.Default.Videocam,
                            label = "Studio",
                            isSelected = currentDestination == AppDestination.STUDIO,
                            onClick = { currentDestination = AppDestination.STUDIO }
                        )

                        BottomNavItem(
                            icon = Icons.Default.Palette,
                            label = "Styles",
                            isSelected = currentDestination == AppDestination.STYLE_EXPLORER,
                            onClick = { currentDestination = AppDestination.STYLE_EXPLORER }
                        )

                        BottomNavItem(
                            icon = Icons.Default.FolderSpecial,
                            label = "Projects",
                            isSelected = currentDestination == AppDestination.MY_PROJECTS,
                            onClick = { currentDestination = AppDestination.MY_PROJECTS }
                        )

                        BottomNavItem(
                            icon = if (currentUser.isOwner) Icons.Default.AdminPanelSettings else Icons.Default.Lock,
                            label = if (currentUser.isOwner) "Admin" else "Locked",
                            isSelected = currentDestination == AppDestination.ADMIN_PANEL,
                            badgeColor = if (currentUser.isOwner) NeonGreen else AmberGlow,
                            onClick = { currentDestination = AppDestination.ADMIN_PANEL }
                        )
                    }
                }
            },
            floatingActionButton = {
                // Quick Creation Action Button
                if (currentDestination == AppDestination.HOME || currentDestination == AppDestination.MY_PROJECTS) {
                    FloatingActionButton(
                        onClick = { currentDestination = AppDestination.STUDIO },
                        containerColor = CyberBlue,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .testTag("fab_create_video")
                            .padding(bottom = 60.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Create Video", fontSize = 13.sp)
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentDestination) {
                    AppDestination.HOME -> {
                        HomeScreen(
                            projects = projects,
                            currentUser = currentUser,
                            onNavigateToStudio = { prompt, mode, style ->
                                studioInitialPrompt = prompt
                                studioInitialMode = mode
                                studioInitialStyle = style
                                currentDestination = AppDestination.STUDIO
                            },
                            onOpenProject = { project ->
                                selectedProjectForPlayback = project
                            },
                            onNavigateToStyles = {
                                currentDestination = AppDestination.STYLE_EXPLORER
                            }
                        )
                    }

                    AppDestination.STUDIO -> {
                        StudioScreen(
                            currentUser = currentUser,
                            projects = projects,
                            initialPrompt = studioInitialPrompt,
                            initialMode = studioInitialMode,
                            initialStyle = studioInitialStyle,
                            onGenerate = { prompt, enhancedPrompt, negativePrompt, mode, style, aspectRatio, duration, resolution, camera, motionPreset, motionIntensity, referenceImageRes ->
                                val result = repository.createAndStartGeneration(
                                    prompt = prompt,
                                    enhancedPrompt = enhancedPrompt,
                                    negativePrompt = negativePrompt,
                                    mode = mode,
                                    styleCategory = style,
                                    aspectRatio = aspectRatio,
                                    durationSeconds = duration,
                                    resolution = resolution,
                                    cameraMovement = camera,
                                    motionPreset = motionPreset,
                                    motionIntensity = motionIntensity,
                                    referenceImageRes = referenceImageRes
                                )

                                result.onSuccess {
                                    // Successfully queued
                                    studioInitialPrompt = null
                                    studioInitialMode = null
                                    studioInitialStyle = null
                                }
                            },
                            onCancelJob = { projectId ->
                                repository.cancelJob(projectId)
                            },
                            onOpenProject = { project ->
                                selectedProjectForPlayback = project
                            }
                        )
                    }

                    AppDestination.STYLE_EXPLORER -> {
                        StyleExplorerScreen(
                            onSelectStyle = { style ->
                                studioInitialStyle = style.name
                                studioInitialPrompt = style.promptModifier
                                currentDestination = AppDestination.STUDIO
                            }
                        )
                    }

                    AppDestination.MY_PROJECTS -> {
                        MyProjectsScreen(
                            projects = projects,
                            onOpenProject = { project ->
                                selectedProjectForPlayback = project
                            },
                            onDeleteProject = { id ->
                                repository.deleteProject(id)
                            },
                            onRetryProject = { id ->
                                repository.retryProject(id)
                            },
                            onRemixProject = { project ->
                                studioInitialPrompt = project.prompt
                                studioInitialMode = project.mode
                                studioInitialStyle = project.styleCategory
                                currentDestination = AppDestination.STUDIO
                            },
                            onNavigateToCreate = {
                                currentDestination = AppDestination.STUDIO
                            }
                        )
                    }

                    AppDestination.ADMIN_PANEL -> {
                        AdminPanelScreen(
                            currentUser = currentUser,
                            adminStats = adminStats,
                            providers = providers,
                            users = allUsers,
                            auditLogs = adminLogs,
                            onUpdateProvider = { id, endpoint, key, modelId, isEnabled, isPrimary ->
                                repository.updateProviderConfig(id, endpoint, key, modelId, isEnabled, isPrimary)
                            },
                            onTestProvider = { id ->
                                repository.testProviderConnection(id)
                            },
                            onSetDailyQuota = { limit ->
                                repository.setDailyFreeLimit(limit)
                            },
                            onToggleMaintenance = { enabled ->
                                repository.setMaintenanceMode(enabled)
                            },
                            onToggleUserSuspension = { userId ->
                                repository.toggleUserSuspension(userId)
                            },
                            onResetUserCredits = { userId ->
                                repository.resetUserCredits(userId)
                            },
                            onSwitchToOwner = {
                                repository.switchUser(asOwner = true)
                            }
                        )
                    }

                    AppDestination.SETTINGS_AND_DOCS -> {
                        SettingsAndDocsScreen(
                            currentUser = currentUser,
                            onSwitchUser = { asOwner ->
                                repository.switchUser(asOwner)
                            },
                            onOpenAuthDialog = {
                                showAuthDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Video Player & Results Dialog
    if (selectedProjectForPlayback != null) {
        VideoPlayerModal(
            project = selectedProjectForPlayback!!,
            onDismiss = { selectedProjectForPlayback = null },
            onRemixVariation = { proj ->
                studioInitialPrompt = proj.prompt
                studioInitialMode = proj.mode
                studioInitialStyle = proj.styleCategory
                currentDestination = AppDestination.STUDIO
                selectedProjectForPlayback = null
            },
            onRegenerate = { proj ->
                repository.retryProject(proj.id)
                currentDestination = AppDestination.STUDIO
                selectedProjectForPlayback = null
            }
        )
    }

    // Auth & Session Switcher Dialog
    if (showAuthDialog) {
        AuthDialog(
            onDismiss = { showAuthDialog = false },
            onSignIn = { email, name ->
                repository.signInAs(email, name)
            },
            onQuickOwner = {
                repository.switchUser(asOwner = true)
            },
            onQuickUser = {
                repository.switchUser(asOwner = false)
            }
        )
    }
}

@Composable
private fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    badgeColor: Color? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) NeonCyan else TextMuted,
                modifier = Modifier.size(22.dp)
            )

            if (badgeColor != null) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(badgeColor)
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = label,
            color = if (isSelected) Color.White else TextMuted,
            fontSize = 10.sp,
            fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
        )
    }
}
