package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 8.dp,
    borderColor: Color = GlassBorder,
    backgroundColor: Color = SurfaceCard,
    borderBrush: Brush? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)

    val appliedBorder = if (borderBrush != null) {
        BorderStroke(1.dp, borderBrush)
    } else {
        BorderStroke(1.dp, borderColor)
    }

    Surface(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = ElectricViolet.copy(alpha = 0.25f),
                spotColor = NeonCyan.copy(alpha = 0.35f)
            )
            .clip(shape)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            ),
        shape = shape,
        color = backgroundColor,
        border = appliedBorder
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SurfaceCardElevated.copy(alpha = 0.45f),
                            backgroundColor
                        ),
                        radius = 800f
                    )
                )
        ) {
            content()
        }
    }
}
