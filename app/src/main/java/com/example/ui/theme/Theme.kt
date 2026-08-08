package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val KtimesLightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    onPrimary = StudioWhite,
    primaryContainer = PrimaryPurpleLight,
    onPrimaryContainer = StudioWhite,
    secondary = VibrantOrange,
    onSecondary = StudioWhite,
    tertiary = CrimsonRed,
    onTertiary = StudioWhite,
    background = AppBackground,
    onBackground = CharcoalBlack,
    surface = CardBackgroundLight,
    onSurface = CharcoalBlack,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextMuted,
    outline = CardBorderLight
)

@Composable
fun KtimesMediaTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = KtimesLightColorScheme,
        typography = Typography,
        content = content
    )
}

