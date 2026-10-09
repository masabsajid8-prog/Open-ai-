package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = VoidBlack,
    primaryContainer = DeepViolet,
    onPrimaryContainer = TextPrimary,
    secondary = ElectricViolet,
    onSecondary = Color.White,
    secondaryContainer = SurfaceCardElevated,
    onSecondaryContainer = SoftCyan,
    tertiary = NeonPink,
    onTertiary = Color.White,
    background = VoidBlack,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = GlassBorder,
    outlineVariant = GlassBorderHighlight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // MotionVerse AI is intentionally a premium futuristic dark cinema studio
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
