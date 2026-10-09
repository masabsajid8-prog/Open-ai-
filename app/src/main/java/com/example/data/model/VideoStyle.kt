package com.example.data.model

data class VideoStyleItem(
    val id: String,
    val name: String,
    val tag: String,
    val description: String,
    val promptModifier: String,
    val suggestedCamera: String,
    val suggestedLighting: String,
    val previewGradientColors: List<Long>
)

object VideoStylesCatalog {
    val categories: List<VideoStyleItem> = listOf(
        VideoStyleItem(
            id = "cinematic_realism",
            name = "Cinematic Realism",
            tag = "35mm Film",
            description = "High-budget motion picture aesthetic with natural depth of field and anamorphic lens flare.",
            promptModifier = "shot on 35mm anamorphic lens, Arri Alexa 65, cinematic color grading, shallow depth of field, photorealistic textures, 8k resolution master",
            suggestedCamera = "Cinematic Dolly Zoom",
            suggestedLighting = "Warm Rim Light + Soft Key",
            previewGradientColors = listOf(0xFF7928CA, 0xFF0070F3)
        ),
        VideoStyleItem(
            id = "photorealistic",
            name = "Photorealistic",
            tag = "Ultra Raw",
            description = "True-to-life lighting, physical light bounce, and lifelike skin/surface micro-details.",
            promptModifier = "ultra photorealistic, Hasselblad medium format camera, natural daytime ambient light, ultra-detailed sub-surface scattering, pristine realism",
            suggestedCamera = "Slow Smooth Pan",
            suggestedLighting = "Natural Daylight Diffused",
            previewGradientColors = listOf(0xFF00F2FE, 0xFF4FACFE)
        ),
        VideoStyleItem(
            id = "3d_animation",
            name = "3D Animation",
            tag = "Stylized 3D",
            description = "Pixar and DreamWorks inspired rich 3D character animation with expressive lighting.",
            promptModifier = "stylized 3D CGI animation, Octane render, expressive character rigging, whimsical volumetric lighting, vibrant color palette, Ray Tracing",
            suggestedCamera = "Dynamic Orbit",
            suggestedLighting = "Vibrant Whimsical Glow",
            previewGradientColors = listOf(0xFFFF007A, 0xFF7928CA)
        ),
        VideoStyleItem(
            id = "anime_inspired",
            name = "Anime-Inspired",
            tag = "Makoto Shinkai",
            description = "Breathtaking hand-painted anime skyboxes, vibrant highlights, and emotional atmosphere.",
            promptModifier = "Makoto Shinkai anime aesthetic, cel-shaded animation, glowing sky clouds, particle light leaks, emotive cinematic framing, Studio Ghibli touch",
            suggestedCamera = "Upward Tilt to Sky",
            suggestedLighting = "Golden Sunset Light Leaks",
            previewGradientColors = listOf(0xFFFA709A, 0xFFFEE140)
        ),
        VideoStyleItem(
            id = "science_fiction",
            name = "Science Fiction",
            tag = "Cyberpunk Neo",
            description = "Dystopian skylines, volumetric holograms, neon reflections, and hyper-advanced tech.",
            promptModifier = "blade runner sci-fi aesthetic, neon holographic signs, wet asphalt reflections, flying vehicles, cyberpunk atmosphere, volumetric fog",
            suggestedCamera = "Drone Flythrough",
            suggestedLighting = "Neon Violet & Cyan Ambient",
            previewGradientColors = listOf(0xFF9D4EDD, 0xFF00F2FE)
        ),
        VideoStyleItem(
            id = "fantasy",
            name = "Fantasy",
            tag = "Ethereal Magic",
            description = "Glowing flora, enchanted landscapes, ancient monoliths, and magical particle flows.",
            promptModifier = "mystical fantasy epic, bioluminescent flora, ancient crystalline ruins, floating particles of magical energy, ethereal mood, Lord of the Rings scale",
            suggestedCamera = "Slow Majestic Crane Up",
            suggestedLighting = "Bioluminescent Purple Glow",
            previewGradientColors = listOf(0xFF8E2DE2, 0xFF4A00E0)
        ),
        VideoStyleItem(
            id = "historical_documentary",
            name = "Historical Documentary",
            tag = "Archival Film",
            description = "Authentic period recreation with vintage film grain, sepia undertones, and historical weight.",
            promptModifier = "historical documentary style, archival 16mm film texture, authentic period costume, moody documentary lighting, subtle film grain",
            suggestedCamera = "Handheld Documentary Drift",
            suggestedLighting = "Candlelight & Dusty Beams",
            previewGradientColors = listOf(0xFF8A5A36, 0xFF3D2314)
        ),
        VideoStyleItem(
            id = "nature_wildlife",
            name = "Nature & Wildlife",
            tag = "National Geographic",
            description = "BBC Planet Earth style ultra-sharp wildlife tracking with rich natural habitat detail.",
            promptModifier = "National Geographic documentary quality, 1000mm telephoto lens, ultra-slow motion wildlife capture, hyper-detailed animal fur and habitat, golden hour",
            suggestedCamera = "Tracking Zoom Follow",
            suggestedLighting = "Dawn Sunbeams",
            previewGradientColors = listOf(0xFF10B981, 0xFF059669)
        ),
        VideoStyleItem(
            id = "luxury_lifestyle",
            name = "Luxury Lifestyle",
            tag = "High Fashion",
            description = "High-end commercials, sleek yachts, minimalist modern mansions, and golden champagne tones.",
            promptModifier = "luxury brand commercial, 8k RED camera, high-end production value, warm champagne gold tones, architectural symmetry, elegant slow motion",
            suggestedCamera = "Gliding Steadicam",
            suggestedLighting = "Gleaming Golden Hour",
            previewGradientColors = listOf(0xFFF6D365, 0xFFFDA085)
        ),
        VideoStyleItem(
            id = "fashion_beauty",
            name = "Fashion & Beauty",
            tag = "Vogue Runway",
            description = "High-contrast editorial lighting, dynamic fabric flow, and striking haute-couture poses.",
            promptModifier = "Vogue editorial fashion film, dramatic studio lighting, flowing silk fabric in slow motion, avant-garde styling, ultra sharp skin tones",
            suggestedCamera = "Low Angle Hero Glides",
            suggestedLighting = "High Contrast Beauty Dish",
            previewGradientColors = listOf(0xFFFF0844, 0xFFFFB199)
        ),
        VideoStyleItem(
            id = "travel_films",
            name = "Travel Films",
            tag = "Wanderlust 4K",
            description = "Sweeping coastal cliffs, exotic markets, turquoise waters, and vibrant travel vlog transitions.",
            promptModifier = "cinematic travel film, FPV drone dive, turquoise ocean waves, vibrant local market colors, uplifting summer warmth, crisp 4k 60fps",
            suggestedCamera = "FPV Drone Dive",
            suggestedLighting = "Vibrant Tropical Sunlight",
            previewGradientColors = listOf(0xFF48C6EF, 0xFF6F86D6)
        ),
        VideoStyleItem(
            id = "product_advertisements",
            name = "Product Ads",
            tag = "Commercial Grade",
            description = "Macro lens product hero shots with liquid splashes, floating particles, and studio cyclorama.",
            promptModifier = "Apple commercial aesthetic, macro probe lens, floating liquid droplets, dramatic studio backlighting, clean dark minimal cyclorama, premium industrial design",
            suggestedCamera = "360 Spin Macro Zoom",
            suggestedLighting = "Edge Rim Light Strip",
            previewGradientColors = listOf(0xFF30CFD0, 0xFF330867)
        ),
        VideoStyleItem(
            id = "horror_suspense",
            name = "Horror & Suspense",
            tag = "Dark Gothic",
            description = "Chilling shadows, eerie fog, flickering lanterns, and tense psychological framing.",
            promptModifier = "A24 psychological horror film, low-key lighting, heavy shadows, creeping atmospheric fog, muted desaturated palette, unsettling tension",
            suggestedCamera = "Slow Creep Forward",
            suggestedLighting = "Dim Flickering Sodium Vapor",
            previewGradientColors = listOf(0xFF434343, 0xFF000000)
        ),
        VideoStyleItem(
            id = "comedy",
            name = "Comedy",
            tag = "Bright & Fun",
            description = "Snappy timing, vibrant colorful palette, expressive performances, and playful environments.",
            promptModifier = "Wes Anderson aesthetic, hyper-symmetric composition, whimsical pastel color palette, witty visual timing, quirky retro set design",
            suggestedCamera = "Symmetrical Whip Pan",
            suggestedLighting = "Bright Pastel Ambient",
            previewGradientColors = listOf(0xFFFEE140, 0xFFFA709A)
        ),
        VideoStyleItem(
            id = "action_sequences",
            name = "Action Sequences",
            tag = "Blockbuster FX",
            description = "High-octane car chases, martial arts combat, speed ramps, and particle-heavy explosions.",
            promptModifier = "Hollywood blockbuster action sequence, Michael Bay style dynamic camera work, speed ramps, debris particles, intense kinetic motion, explosive backdrop",
            suggestedCamera = "Shaky Cam Rapid Track",
            suggestedLighting = "Fiery Orange Backlight",
            previewGradientColors = listOf(0xFFFF416C, 0xFFFF4B2B)
        ),
        VideoStyleItem(
            id = "educational_videos",
            name = "Educational & Science",
            tag = "Infographic 3D",
            description = "Clear 3D anatomical cutaways, scientific simulations, planetary orbits, and clean graphics.",
            promptModifier = "Kurzgesagt 3D documentary style, accurate physics simulation, clean scientific rendering, cutaway cross-section, glowing technical indicators",
            suggestedCamera = "Isometric Exploded View",
            suggestedLighting = "Laboratory Clean White",
            previewGradientColors = listOf(0xFF00C6FF, 0xFF0072FF)
        ),
        VideoStyleItem(
            id = "social_media_content",
            name = "Social Media Viral",
            tag = "Vertical Reel",
            description = "Fast-paced hooks, bold neon captions aesthetic, punchy color grading, and modern mobile appeal.",
            promptModifier = "viral TikTok aesthetic, punchy high-contrast grading, instant visual hook, dynamic zoom cuts, smooth gimbal motion, trending aesthetic",
            suggestedCamera = "Fast Snap Zoom",
            suggestedLighting = "Ring Light Pop",
            previewGradientColors = listOf(0xFF8A2387, 0xFFE94057)
        ),
        VideoStyleItem(
            id = "dance_videos",
            name = "Dance & Performance",
            tag = "Choreography",
            description = "Dynamic body tracking, laser beams, rhythmic motion trails, and street dance energy.",
            promptModifier = "high energy dance music video, motion blur trails, dynamic choreography, neon laser beams slicing through smoke, urban warehouse backdrop, 120fps slow mo",
            suggestedCamera = "Low Angle Swivel Follow",
            suggestedLighting = "Strobe & Neon Lasers",
            previewGradientColors = listOf(0xFFB92B27, 0xFF1565C0)
        ),
        VideoStyleItem(
            id = "storytelling",
            name = "Storytelling & Drama",
            tag = "Character Arc",
            description = "Deep emotive close-ups, subtle character micro-expressions, and melancholic atmospheres.",
            promptModifier = "emotive narrative film, nuanced facial micro-expressions, golden hour light, poignant atmosphere, cinematic depth of field, poetic visual tone",
            suggestedCamera = "Intimate Slow Push-in",
            suggestedLighting = "Soft Window Light",
            previewGradientColors = listOf(0xFF536976, 0xFF292E49)
        ),
        VideoStyleItem(
            id = "architecture_design",
            name = "Architecture & Interior",
            tag = "ArchViz",
            description = "Minimalist brutalism, Japanese zen gardens, Bauhaus geometry, and realistic raytraced glass.",
            promptModifier = "Architectural Digest showcase, photorealistic ArchViz render, clean brutalist concrete, lush Japanese zen garden, raytraced glass reflections, golden sunlight beams",
            suggestedCamera = "Smooth Architectural Glide",
            suggestedLighting = "Morning Slanted Sunlight",
            previewGradientColors = listOf(0xFF2C3E50, 0xFFBDC3C7)
        )
    )
}
