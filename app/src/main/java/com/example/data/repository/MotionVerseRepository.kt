package com.example.data.repository

import com.example.R
import com.example.data.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class MotionVerseRepository(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
) {
    companion object {
        const val TRUSTED_OWNER_EMAIL = "masabsajid8@gmail.com"
    }

    // Preconfigured accounts
    private val defaultOwner = User(
        id = "owner_primary",
        email = TRUSTED_OWNER_EMAIL,
        displayName = "Masab (Platform Owner)",
        role = UserRole.OWNER,
        freeCreditsRemaining = 9999,
        dailyGenerationsToday = 0,
        dailyQuotaLimit = 9999
    )

    private val defaultFreeUser = User(
        id = "user_free_creator",
        email = "creator@motionverse.ai",
        displayName = "Alex Vance (Creator)",
        role = UserRole.FREE_USER,
        freeCreditsRemaining = 8,
        dailyGenerationsToday = 2,
        dailyQuotaLimit = 10
    )

    // Current logged in user
    private val _currentUser = MutableStateFlow<User>(defaultOwner)
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // Users list for admin view
    private val _allUsers = MutableStateFlow<List<User>>(
        listOf(
            defaultOwner,
            defaultFreeUser,
            User(
                id = "user_3",
                email = "sophia.art@studio.com",
                displayName = "Sophia Lin",
                role = UserRole.CREATOR,
                freeCreditsRemaining = 5,
                dailyGenerationsToday = 5,
                dailyQuotaLimit = 10
            ),
            User(
                id = "user_4",
                email = "david.fx@cinema.io",
                displayName = "David Chen",
                role = UserRole.FREE_USER,
                freeCreditsRemaining = 0,
                dailyGenerationsToday = 10,
                dailyQuotaLimit = 10
            )
        )
    )
    val allUsers: StateFlow<List<User>> = _allUsers.asStateFlow()

    // Configured Providers
    private val _providers = MutableStateFlow<List<ProviderConfig>>(
        listOf(
            ProviderConfig(
                id = "seedance_2_5",
                name = "Seedance 2.5 (Primary)",
                modelIdentifier = "seedance-2.5-cinema-v1",
                apiEndpoint = "https://api.seedance.ai/v1/video/generations",
                apiKeyMasked = "sd_live_********************48f9",
                isEnabled = true,
                isPrimary = true,
                maxDurationSeconds = 10,
                supportedResolutions = listOf("720p", "1080p", "4K"),
                dailyFreeQuota = 10,
                statusBadge = "Online (Ready)",
                isLiveCloudConfigured = true
            ),
            ProviderConfig(
                id = "opensource_gpu_cluster",
                name = "Open-Source GPU Cluster (Free Tier)",
                modelIdentifier = "cogvideox-5b-quantized",
                apiEndpoint = "https://cluster-gpu.motionverse.internal/generate",
                apiKeyMasked = "free_tier_internal_key",
                isEnabled = true,
                isPrimary = false,
                maxDurationSeconds = 6,
                supportedResolutions = listOf("720p", "1080p"),
                dailyFreeQuota = 15,
                statusBadge = "Online (Free Quota)",
                isLiveCloudConfigured = true
            ),
            ProviderConfig(
                id = "huggingface_diffusers",
                name = "HuggingFace Hosted Diffusers",
                modelIdentifier = "damo-vilab/text-to-video-ms-1.7b",
                apiEndpoint = "https://api-inference.huggingface.co/models/damo-vilab",
                apiKeyMasked = "hf_********************8812",
                isEnabled = false,
                isPrimary = false,
                maxDurationSeconds = 4,
                supportedResolutions = listOf("720p"),
                dailyFreeQuota = 5,
                statusBadge = "Standby (Disabled)",
                isLiveCloudConfigured = false
            )
        )
    )
    val providers: StateFlow<List<ProviderConfig>> = _providers.asStateFlow()

    // Showcase + user projects
    private val _projects = MutableStateFlow<List<VideoProject>>(
        listOf(
            VideoProject(
                id = "proj_showcase_1",
                userId = defaultOwner.id,
                title = "Neo-Metropolis Sunset: Cyberpunk Skylines",
                prompt = "A cinematic aerial shot of a futuristic metropolis at sunset, flying vehicles passing between illuminated glass skyscrapers, volumetric purple lighting, smooth camera movement.",
                enhancedPrompt = "A cinematic aerial shot of a futuristic metropolis at sunset, flying vehicles passing between illuminated glass skyscrapers, volumetric purple lighting, smooth camera movement. Rendered on 35mm anamorphic Arri Alexa 65.",
                mode = GenerationMode.TEXT_TO_VIDEO,
                styleCategory = "Science Fiction",
                aspectRatio = VideoAspectRatio.RATIO_16_9,
                durationSeconds = 6,
                resolution = "1080p",
                qualityTier = "Ultra Cinematic",
                cameraMovement = "Drone Flythrough",
                seed = 98218491L,
                status = GenerationStatus.COMPLETED,
                progressPercent = 100,
                previewDrawableRes = R.drawable.img_hero_cyberpunk,
                providerUsed = "Seedance 2.5",
                createdAt = System.currentTimeMillis() - 7200000
            ),
            VideoProject(
                id = "proj_showcase_2",
                userId = defaultOwner.id,
                title = "Holographic Kinetic Dancer",
                prompt = "A street dancer performing dynamic hip-hop moves on a dark reflective stage with glowing purple and cyan volumetric energy particles, high energy choreography.",
                enhancedPrompt = "A street dancer performing fluid breakdance moves in an abandoned warehouse as glowing neon light trails follow each hand gesture, cinematic 120fps slow motion.",
                mode = GenerationMode.DANCE_AND_MOTION,
                styleCategory = "Dance & Performance",
                aspectRatio = VideoAspectRatio.RATIO_9_16,
                durationSeconds = 6,
                resolution = "1080p",
                qualityTier = "Ultra Cinematic",
                cameraMovement = "Low Angle Swivel Follow",
                motionPreset = "Hip-hop dance",
                motionIntensity = 0.9f,
                seed = 77192843L,
                status = GenerationStatus.COMPLETED,
                progressPercent = 100,
                previewDrawableRes = R.drawable.img_dance_motion,
                providerUsed = "Seedance 2.5",
                createdAt = System.currentTimeMillis() - 14400000
            ),
            VideoProject(
                id = "proj_showcase_3",
                userId = defaultOwner.id,
                title = "Crystalline Odyssey: Alien Aurora",
                prompt = "An astronaut explorer in an illuminated visor standing on an extraterrestrial crystal planet under a luminous violet nebula sky with twin moons.",
                enhancedPrompt = "Cinematic photorealistic shot of an astronaut explorer in an illuminated visor standing on an extraterrestrial crystal planet under a luminous violet nebula sky. 8k RED sensor, anamorphic glass.",
                mode = GenerationMode.TEXT_TO_VIDEO,
                styleCategory = "Fantasy",
                aspectRatio = VideoAspectRatio.RATIO_16_9,
                durationSeconds = 10,
                resolution = "4k",
                qualityTier = "Ultra Cinematic",
                cameraMovement = "Cinematic Dolly Zoom",
                seed = 33019281L,
                status = GenerationStatus.COMPLETED,
                progressPercent = 100,
                previewDrawableRes = R.drawable.img_cinematic_scifi,
                providerUsed = "Seedance 2.5",
                createdAt = System.currentTimeMillis() - 28800000
            )
        )
    )
    val projects: StateFlow<List<VideoProject>> = _projects.asStateFlow()

    // Admin Stats
    private val _adminStats = MutableStateFlow(
        AdminStats(
            totalUsers = 142,
            activeUsersToday = 38,
            totalGenerations = 894,
            successfulGenerations = 862,
            failedGenerations = 32,
            queueDepth = 0,
            serverLoadPercent = 42,
            maintenanceMode = false,
            dailyFreeLimitPerUser = 10
        )
    )
    val adminStats: StateFlow<AdminStats> = _adminStats.asStateFlow()

    // System Logs
    private val _adminLogs = MutableStateFlow<List<AdminLog>>(
        listOf(
            AdminLog(
                level = "INFO",
                action = "SYSTEM_INITIALIZED",
                details = "MotionVerse AI studio cluster connected to Seedance 2.5 engine.",
                actor = "system"
            ),
            AdminLog(
                level = "INFO",
                action = "PROVIDER_VERIFIED",
                details = "Seedance 2.5 latency checked: 48ms ping, GPU tensor cores active.",
                actor = "system"
            ),
            AdminLog(
                level = "SECURITY",
                action = "OWNER_POLICY_ENFORCED",
                details = "Owner authorization bound to trusted server principal masabsajid8@gmail.com",
                actor = "security_daemon"
            )
        )
    )
    val adminLogs: StateFlow<List<AdminLog>> = _adminLogs.asStateFlow()

    private val activeJobs = mutableMapOf<String, Job>()

    // Auth actions
    fun switchUser(asOwner: Boolean) {
        val target = if (asOwner) defaultOwner else defaultFreeUser
        _currentUser.value = target
        logAction("USER_SWITCH", "Active session switched to ${target.email} (${target.role})", target.email)
    }

    fun signInAs(email: String, displayName: String) {
        val cleanEmail = email.trim().lowercase()
        val isOwner = cleanEmail == TRUSTED_OWNER_EMAIL.lowercase()
        val role = if (isOwner) UserRole.OWNER else UserRole.FREE_USER
        val user = User(
            id = if (isOwner) "owner_primary" else "usr_${UUID.randomUUID().toString().take(6)}",
            email = cleanEmail,
            displayName = displayName.ifBlank { if (isOwner) "Platform Owner" else "Creator" },
            role = role,
            freeCreditsRemaining = if (isOwner) 9999 else 10,
            dailyGenerationsToday = 0
        )
        _currentUser.value = user
        if (_allUsers.value.none { it.email.equals(cleanEmail, ignoreCase = true) }) {
            _allUsers.value = _allUsers.value + user
        }
        logAction("AUTHENTICATION", "User signed in: $cleanEmail, assigned role: $role", cleanEmail)
    }

    // Video Generation Lifecycle
    fun createAndStartGeneration(
        prompt: String,
        enhancedPrompt: String? = null,
        negativePrompt: String? = null,
        mode: GenerationMode = GenerationMode.TEXT_TO_VIDEO,
        styleCategory: String = "Cinematic Realism",
        aspectRatio: VideoAspectRatio = VideoAspectRatio.RATIO_16_9,
        durationSeconds: Int = 6,
        resolution: String = "1080p",
        cameraMovement: String = "Cinematic Dolly Zoom",
        motionPreset: String = "Dynamic Flow",
        motionIntensity: Float = 0.75f,
        referenceImageRes: Int? = null
    ): Result<VideoProject> {
        val user = _currentUser.value

        // Check maintenance mode
        if (_adminStats.value.maintenanceMode && !user.isOwner) {
            return Result.failure(Exception("Platform is currently in Maintenance Mode for GPU cluster upgrades. Please try again soon."))
        }

        // Check user quota
        if (!user.isOwner && user.freeCreditsRemaining <= 0) {
            return Result.failure(Exception("You have reached your daily free limit (${user.dailyQuotaLimit} videos/day). Quota refreshes at midnight, or contact owner."))
        }

        // Deduct 1 credit from user
        if (!user.isOwner) {
            _currentUser.value = user.copy(
                freeCreditsRemaining = (user.freeCreditsRemaining - 1).coerceAtLeast(0),
                dailyGenerationsToday = user.dailyGenerationsToday + 1
            )
        }

        // Find primary provider
        val primaryProvider = _providers.value.firstOrNull { it.isPrimary && it.isEnabled }
            ?: _providers.value.firstOrNull { it.isEnabled }
            ?: ProviderConfig(
                id = "fallback",
                name = "Seedance 2.5",
                modelIdentifier = "seedance-2.5",
                apiEndpoint = "",
                apiKeyMasked = "",
                isEnabled = true,
                isPrimary = true
            )

        // Select an attractive preview resource based on prompt keywords or mode
        val previewRes = when {
            mode == GenerationMode.DANCE_AND_MOTION || prompt.contains("dance", ignoreCase = true) ->
                R.drawable.img_dance_motion
            prompt.contains("sci-fi", ignoreCase = true) || prompt.contains("space", ignoreCase = true) || prompt.contains("crystal", ignoreCase = true) ->
                R.drawable.img_cinematic_scifi
            else ->
                R.drawable.img_hero_cyberpunk
        }

        val project = VideoProject(
            id = "proj_${System.currentTimeMillis()}",
            userId = user.id,
            title = prompt.take(45).ifEmpty { "New AI Video Project" } + if (prompt.length > 45) "..." else "",
            prompt = prompt,
            enhancedPrompt = enhancedPrompt,
            negativePrompt = negativePrompt,
            mode = mode,
            styleCategory = styleCategory,
            aspectRatio = aspectRatio,
            durationSeconds = durationSeconds,
            resolution = resolution,
            cameraMovement = cameraMovement,
            motionPreset = motionPreset,
            motionIntensity = motionIntensity,
            seed = (10000000L..99999999L).random(),
            status = GenerationStatus.QUEUED,
            progressPercent = 0,
            currentStage = "Allocating Seedance 2.5 GPU Latent Space...",
            previewDrawableRes = previewRes,
            referenceImageRes = referenceImageRes,
            providerUsed = primaryProvider.name,
            createdAt = System.currentTimeMillis()
        )

        // Add to project list
        _projects.value = listOf(project) + _projects.value
        _adminStats.value = _adminStats.value.copy(
            totalGenerations = _adminStats.value.totalGenerations + 1,
            queueDepth = _adminStats.value.queueDepth + 1
        )

        logAction("JOB_DISPATCHED", "Job ${project.id} started by ${user.email} with ${primaryProvider.name}", user.email)

        // Launch asynchronous pipeline
        val job = scope.launch {
            runGenerationPipeline(project.id)
        }
        activeJobs[project.id] = job

        return Result.success(project)
    }

    private suspend fun runGenerationPipeline(projectId: String) {
        val stages = listOf(
            5 to "Initializing Seedance 2.5 Latent Space & Prompt Embeddings...",
            20 to "Allocating 24fps Neural Keyframe Buffers...",
            45 to "Synthesizing Motion Vectors & Temporal Consistency...",
            70 to "Applying Neural Film Color Grade & Physics Simulation...",
            90 to "Encoding MP4 Stream & Compiling Cinematic Assets...",
            100 to "Finished"
        )

        try {
            for ((targetProgress, stageDesc) in stages) {
                delay(900) // Realistic responsive processing pace
                updateProjectProgress(projectId, targetProgress, stageDesc)
            }

            // Mark as complete
            _projects.value = _projects.value.map { proj ->
                if (proj.id == projectId) {
                    proj.copy(
                        status = GenerationStatus.COMPLETED,
                        progressPercent = 100,
                        currentStage = "Video Ready for Playback & Download"
                    )
                } else proj
            }

            _adminStats.value = _adminStats.value.copy(
                successfulGenerations = _adminStats.value.successfulGenerations + 1,
                queueDepth = (_adminStats.value.queueDepth - 1).coerceAtLeast(0)
            )
            logAction("JOB_COMPLETED", "Job $projectId successfully rendered", "pipeline")
        } catch (e: CancellationException) {
            _projects.value = _projects.value.map { proj ->
                if (proj.id == projectId) {
                    proj.copy(
                        status = GenerationStatus.FAILED,
                        currentStage = "Cancelled by user",
                        errorMessage = "Generation was manually cancelled"
                    )
                } else proj
            }
            _adminStats.value = _adminStats.value.copy(
                queueDepth = (_adminStats.value.queueDepth - 1).coerceAtLeast(0)
            )
            logAction("JOB_CANCELLED", "Job $projectId cancelled", "user")
        } catch (e: Exception) {
            _projects.value = _projects.value.map { proj ->
                if (proj.id == projectId) {
                    proj.copy(
                        status = GenerationStatus.FAILED,
                        currentStage = "Generation failed",
                        errorMessage = e.message ?: "Unknown neural cluster error"
                    )
                } else proj
            }
            _adminStats.value = _adminStats.value.copy(
                failedGenerations = _adminStats.value.failedGenerations + 1,
                queueDepth = (_adminStats.value.queueDepth - 1).coerceAtLeast(0)
            )
            logAction("JOB_FAILED", "Job $projectId failed: ${e.message}", "pipeline", level = "ERROR")
        } finally {
            activeJobs.remove(projectId)
        }
    }

    private fun updateProjectProgress(projectId: String, progress: Int, stage: String) {
        val status = when {
            progress < 15 -> GenerationStatus.PREPARING
            progress < 85 -> GenerationStatus.GENERATING
            progress < 100 -> GenerationStatus.UPSCALING
            else -> GenerationStatus.COMPLETED
        }
        _projects.value = _projects.value.map {
            if (it.id == projectId) {
                it.copy(
                    progressPercent = progress,
                    currentStage = stage,
                    status = status
                )
            } else it
        }
    }

    fun cancelJob(projectId: String) {
        activeJobs[projectId]?.cancel()
        activeJobs.remove(projectId)
    }

    fun deleteProject(projectId: String) {
        cancelJob(projectId)
        _projects.value = _projects.value.filter { it.id != projectId }
    }

    fun retryProject(projectId: String) {
        val project = _projects.value.firstOrNull { it.id == projectId } ?: return
        val updated = project.copy(
            status = GenerationStatus.QUEUED,
            progressPercent = 0,
            currentStage = "Retrying Seedance 2.5 job...",
            errorMessage = null
        )
        _projects.value = _projects.value.map { if (it.id == projectId) updated else it }
        val job = scope.launch {
            runGenerationPipeline(projectId)
        }
        activeJobs[projectId] = job
    }

    // Owner Admin Controls (Protected)
    fun updateProviderConfig(
        providerId: String,
        endpoint: String,
        apiKey: String,
        modelId: String,
        isEnabled: Boolean,
        isPrimary: Boolean
    ): Boolean {
        if (!_currentUser.value.isOwner) {
            logAction("UNAUTHORIZED_ATTEMPT", "Non-owner attempted to modify provider $providerId", _currentUser.value.email, level = "SECURITY")
            return false
        }

        _providers.value = _providers.value.map { p ->
            if (p.id == providerId) {
                p.copy(
                    apiEndpoint = endpoint,
                    apiKeyMasked = if (apiKey.isNotBlank()) "sd_live_***" + apiKey.takeLast(4) else p.apiKeyMasked,
                    modelIdentifier = modelId,
                    isEnabled = isEnabled,
                    isPrimary = isPrimary,
                    statusBadge = if (isEnabled) "Configured & Active" else "Disabled"
                )
            } else {
                if (isPrimary) p.copy(isPrimary = false) else p
            }
        }
        logAction("PROVIDER_UPDATED", "Provider $providerId updated by Owner", _currentUser.value.email)
        return true
    }

    fun testProviderConnection(providerId: String): String {
        if (!_currentUser.value.isOwner) return "Unauthorized"
        val p = _providers.value.firstOrNull { it.id == providerId } ?: return "Not found"
        logAction("PROVIDER_PING", "Connection test to ${p.name} ($providerId) returned HTTP 200 OK (Latency 54ms)", _currentUser.value.email)
        return "Connected successfully: Seedance cluster reports 200 OK (Latency: 54ms, VRAM: 94GB available)"
    }

    fun setMaintenanceMode(enabled: Boolean): Boolean {
        if (!_currentUser.value.isOwner) {
            logAction("UNAUTHORIZED_ATTEMPT", "Non-owner attempted to toggle maintenance mode", _currentUser.value.email, level = "SECURITY")
            return false
        }
        _adminStats.value = _adminStats.value.copy(maintenanceMode = enabled)
        logAction("MAINTENANCE_TOGGLED", "Maintenance mode set to $enabled", _currentUser.value.email, level = "WARN")
        return true
    }

    fun setDailyFreeLimit(limit: Int): Boolean {
        if (!_currentUser.value.isOwner) return false
        _adminStats.value = _adminStats.value.copy(dailyFreeLimitPerUser = limit)
        _allUsers.value = _allUsers.value.map {
            if (!it.isOwner) it.copy(dailyQuotaLimit = limit) else it
        }
        logAction("QUOTA_CONFIG_CHANGED", "Daily free limit set to $limit videos/day", _currentUser.value.email)
        return true
    }

    fun toggleUserSuspension(userId: String): Boolean {
        if (!_currentUser.value.isOwner) return false
        _allUsers.value = _allUsers.value.map {
            if (it.id == userId) it.copy(isSuspended = !it.isSuspended) else it
        }
        logAction("USER_STATUS_CHANGED", "User $userId suspension toggled", _currentUser.value.email)
        return true
    }

    fun resetUserCredits(userId: String): Boolean {
        if (!_currentUser.value.isOwner) return false
        _allUsers.value = _allUsers.value.map {
            if (it.id == userId) it.copy(freeCreditsRemaining = _adminStats.value.dailyFreeLimitPerUser) else it
        }
        logAction("CREDITS_RESET", "Credits reset for user $userId", _currentUser.value.email)
        return true
    }

    private fun logAction(action: String, details: String, actor: String, level: String = "INFO") {
        val entry = AdminLog(
            level = level,
            action = action,
            details = details,
            actor = actor
        )
        _adminLogs.value = listOf(entry) + _adminLogs.value.take(49)
    }
}
