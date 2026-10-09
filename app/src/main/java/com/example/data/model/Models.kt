package com.example.data.model

import java.util.UUID

enum class UserRole {
    OWNER,
    CREATOR,
    FREE_USER
}

data class User(
    val id: String = UUID.randomUUID().toString(),
    val email: String,
    val displayName: String,
    val role: UserRole = UserRole.FREE_USER,
    val freeCreditsRemaining: Int = 10,
    val dailyGenerationsToday: Int = 0,
    val dailyQuotaLimit: Int = 10,
    val isSuspended: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    val isOwner: Boolean get() = role == UserRole.OWNER
}

enum class GenerationMode(val label: String, val description: String) {
    TEXT_TO_VIDEO("Text to Video", "Generate cinematic scenes from natural language descriptions"),
    IMAGE_TO_VIDEO("Image to Video", "Animate static photos with realistic physics and motion"),
    DANCE_AND_MOTION("Dance & Motion", "Create choreography and high-energy motion transfer clips")
}

enum class VideoAspectRatio(val label: String, val ratio: Float, val dimensionLabel: String) {
    RATIO_16_9("16:9", 16f / 9f, "1920 × 1080 (Landscape)"),
    RATIO_9_16("9:16", 9f / 16f, "1080 × 1920 (Portrait / Reels)"),
    RATIO_1_1("1:1", 1f, "1080 × 1080 (Square)"),
    RATIO_4_3("4:3", 4f / 3f, "1440 × 1080 (Classic)"),
    RATIO_21_9("21:9", 21f / 9f, "2560 × 1080 (Cinemascope)")
}

enum class GenerationStatus(val label: String) {
    QUEUED("Queued"),
    PREPARING("Latent Setup"),
    GENERATING("Synthesizing Motion"),
    UPSCALING("Neural Grade"),
    COMPLETED("Ready"),
    FAILED("Failed")
}

data class VideoProject(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val title: String,
    val prompt: String,
    val enhancedPrompt: String? = null,
    val negativePrompt: String? = null,
    val mode: GenerationMode = GenerationMode.TEXT_TO_VIDEO,
    val styleCategory: String = "Cinematic Realism",
    val aspectRatio: VideoAspectRatio = VideoAspectRatio.RATIO_16_9,
    val durationSeconds: Int = 6,
    val resolution: String = "1080p",
    val qualityTier: String = "Ultra Cinematic",
    val cameraMovement: String = "Cinematic Dolly Zoom",
    val motionPreset: String = "Dynamic Flow",
    val motionIntensity: Float = 0.75f,
    val seed: Long = 4219842L,
    val status: GenerationStatus = GenerationStatus.COMPLETED,
    val progressPercent: Int = 100,
    val currentStage: String = "Finished",
    val previewDrawableRes: Int? = null,
    val referenceImageRes: Int? = null,
    val videoUrl: String? = null,
    val providerUsed: String = "Seedance 2.5",
    val creditCost: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
    val errorMessage: String? = null
)

data class ProviderConfig(
    val id: String,
    val name: String,
    val modelIdentifier: String,
    val apiEndpoint: String,
    val apiKeyMasked: String,
    val isEnabled: Boolean,
    val isPrimary: Boolean,
    val supportsNegativePrompt: Boolean = true,
    val supportsImageInput: Boolean = true,
    val supportsMotionTransfer: Boolean = true,
    val maxDurationSeconds: Int = 10,
    val supportedResolutions: List<String> = listOf("720p", "1080p", "4k"),
    val dailyFreeQuota: Int = 10,
    val statusBadge: String = "Active",
    val isLiveCloudConfigured: Boolean = false
)

data class AdminLog(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val level: String, // "INFO", "SECURITY", "WARN", "ERROR"
    val action: String,
    val details: String,
    val actor: String
)

data class AdminStats(
    val totalUsers: Int = 142,
    val activeUsersToday: Int = 38,
    val totalGenerations: Int = 894,
    val successfulGenerations: Int = 862,
    val failedGenerations: Int = 32,
    val queueDepth: Int = 0,
    val serverLoadPercent: Int = 42,
    val maintenanceMode: Boolean = false,
    val dailyFreeLimitPerUser: Int = 10
)
