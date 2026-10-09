package com.example.service

import com.example.data.model.GenerationMode

data class EnhancedPromptResult(
    val fullPrompt: String,
    val subject: String,
    val environment: String,
    val actions: String,
    val camera: String,
    val lighting: String,
    val atmosphere: String,
    val composition: String,
    val motion: String,
    val style: String
)

object PromptEnhancerService {

    fun enhance(
        userPrompt: String,
        mode: GenerationMode,
        cameraPreset: String? = null,
        stylePreset: String? = null
    ): EnhancedPromptResult {
        val cleanInput = userPrompt.trim().ifEmpty { "A cinematic futuristic scene" }

        // Smart linguistic analysis & expansion
        val camera = cameraPreset?.takeIf { it.isNotBlank() } ?: chooseCamera(cleanInput)
        val lighting = chooseLighting(cleanInput)
        val atmosphere = chooseAtmosphere(cleanInput)
        val style = stylePreset?.takeIf { it.isNotBlank() } ?: "photorealistic 8k cinema master"
        val composition = "centered golden-ratio framing, anamorphic lens depth of field"
        val motionDynamics = when (mode) {
            GenerationMode.TEXT_TO_VIDEO -> "physically accurate kinetic momentum, fluid secondary micro-movements, 24fps motion blur"
            GenerationMode.IMAGE_TO_VIDEO -> "natural environmental physics, soft fabric/hair flutter, organic breathing parallax"
            GenerationMode.DANCE_AND_MOTION -> "rhythmic choreography dynamics, synchronized whole-body kinematics, explosive dance velocity"
        }

        val subject = cleanInput.replaceFirstChar { it.uppercase() }
        val actions = "moving organically through the space with realistic inertia and natural physical weight"
        val environment = "immersive high-fidelity setting with rich ambient depth, volumetric particle suspension, and realistic surface reflections"

        val enhancedText = buildString {
            append("$subject. ")
            append("Captured in an $environment. ")
            append("The scene features $actions. ")
            append("Camera: $camera with smooth stabilized tracking. ")
            append("Lighting: $lighting with realistic raytraced bounce and shadow falloff. ")
            append("Atmosphere: $atmosphere. ")
            append("Composition: $composition. ")
            append("Motion: $motionDynamics. ")
            append("Rendered in ultra-detailed $style with physically based rendering.")
        }

        return EnhancedPromptResult(
            fullPrompt = enhancedText,
            subject = subject,
            environment = environment,
            actions = actions,
            camera = camera,
            lighting = lighting,
            atmosphere = atmosphere,
            composition = composition,
            motion = motionDynamics,
            style = style
        )
    }

    private fun chooseCamera(text: String): String {
        val lower = text.lowercase()
        return when {
            lower.contains("fly") || lower.contains("sky") || lower.contains("city") ->
                "sweeping aerial drone flythrough, wide-angle 16mm perspective"
            lower.contains("dance") || lower.contains("run") || lower.contains("fight") ->
                "dynamic low-angle tracking dolly following the kinetic momentum"
            lower.contains("portrait") || lower.contains("face") || lower.contains("person") ->
                "intimate 85mm portrait lens with cinematic shallow depth of field"
            lower.contains("car") || lower.contains("speed") || lower.contains("race") ->
                "high-speed Russian arm chase camera skimming inches above the surface"
            else ->
                "cinematic slow dolly zoom with gentle orbital arc"
        }
    }

    private fun chooseLighting(text: String): String {
        val lower = text.lowercase()
        return when {
            lower.contains("cyberpunk") || lower.contains("neon") || lower.contains("night") ->
                "electric violet and neon cyan rim lighting with glistening wet pavement reflections"
            lower.contains("nature") || lower.contains("forest") || lower.contains("sunset") ->
                "golden hour sunbeams filtering through atmospheric dust, warm amber edge glow"
            lower.contains("space") || lower.contains("alien") || lower.contains("sci-fi") ->
                "bioluminescent deep nebula illumination with high-contrast starlight shadows"
            else ->
                "volumetric key light with subtle rim separation and realistic ambient bounce"
        }
    }

    private fun chooseAtmosphere(text: String): String {
        val lower = text.lowercase()
        return when {
            lower.contains("cyberpunk") || lower.contains("future") ->
                "moody cyberpunk haze, floating holographic digital sparks, distant skyline bokeh"
            lower.contains("forest") || lower.contains("water") ->
                "morning mist rising, drifting pollen particles, crisp pristine air"
            lower.contains("magic") || lower.contains("fantasy") ->
                "ethereal enchanted aura, swirling luminous particle motes, mystical depth"
            else ->
                "rich cinematic mist, subtle volumetric haze, emotive high-production atmosphere"
        }
    }

    val samplePrompts = listOf(
        "A cinematic aerial shot of a futuristic metropolis at sunset, flying vehicles passing between illuminated glass skyscrapers, volumetric purple lighting, smooth camera movement.",
        "An armored cybernetic samurai standing in a rain-slicked Tokyo alley, holographic billboards reflecting on chrome katana, neon cyan droplets splashing in slow motion.",
        "A majestic snow leopard gracefully leaping across a misty Himalayan precipice at dawn, ultra-slow motion, hyper-detailed fur physics and crystalline snow dust.",
        "A futuristic orbital space station docking sequence with an interstellar starship, Earth glowing in the background, realistic zero-gravity physics, 8k cinematic master.",
        "A street dancer performing fluid breakdance moves in an abandoned warehouse as glowing neon light trails follow each hand gesture, cinematic 120fps slow motion.",
        "A high-end luxury electric hypercar drifting through a curved alpine pass, dynamic speed ramps, autumn leaves swirling behind the carbon fiber chassis."
    )
}
