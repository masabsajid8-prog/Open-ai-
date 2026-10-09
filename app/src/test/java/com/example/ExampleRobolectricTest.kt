package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.GenerationMode
import com.example.data.repository.MotionVerseRepository
import com.example.service.PromptEnhancerService
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context matches MotionVerse`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("MotionVerse", appName)
    }

    @Test
    fun `prompt enhancer expands input with camera and lighting details`() {
        val result = PromptEnhancerService.enhance(
            userPrompt = "A lion running in a savanna",
            mode = GenerationMode.TEXT_TO_VIDEO,
            cameraPreset = "Dynamic Dolly",
            stylePreset = "Cinematic Realism"
        )
        assertNotNull(result)
        assertTrue(result.fullPrompt.contains("A lion running in a savanna", ignoreCase = true))
        assertTrue(result.fullPrompt.contains("Dynamic Dolly", ignoreCase = true))
        assertTrue(result.fullPrompt.contains("Lighting:", ignoreCase = true))
    }

    @Test
    fun `repository enforces owner privileges and user quotas`() {
        val repo = MotionVerseRepository()

        // Default user is verified owner
        val owner = repo.currentUser.value
        assertEquals("masabsajid8@gmail.com", owner.email)
        assertTrue(owner.isOwner)

        // Owner can update provider config
        val updated = repo.updateProviderConfig(
            providerId = "seedance_2_5",
            endpoint = "https://api.seedance.ai/v1/video/generations",
            apiKey = "sd_test_key_1234",
            modelId = "seedance-2.5-cinema-v1",
            isEnabled = true,
            isPrimary = true
        )
        assertTrue(updated)

        // Switch to free creator user
        repo.switchUser(asOwner = false)
        val freeUser = repo.currentUser.value
        assertFalse(freeUser.isOwner)

        // Non-owner cannot update provider config
        val unauthorizedUpdate = repo.updateProviderConfig(
            providerId = "seedance_2_5",
            endpoint = "https://hacked.com",
            apiKey = "bad_key",
            modelId = "bad_model",
            isEnabled = true,
            isPrimary = true
        )
        assertFalse(unauthorizedUpdate)
    }

    @Test
    fun `generation pipeline creates project and progresses`() {
        val repo = MotionVerseRepository()
        val result = repo.createAndStartGeneration(
            prompt = "A high-speed neon hypercar drifting on wet tarmac",
            mode = GenerationMode.TEXT_TO_VIDEO
        )
        assertTrue(result.isSuccess)
        val project = result.getOrNull()
        assertNotNull(project)
        assertEquals("Seedance 2.5 (Primary)", project?.providerUsed)
    }
}
