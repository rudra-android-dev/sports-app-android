package com.example.sportsapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary            = SportsPrimary,
    onPrimary          = CardSurface,
    primaryContainer   = Color(0xFFDCEEFF),
    onPrimaryContainer = Color(0xFF001D36),
    secondary          = SportsAccent,
    onSecondary        = TextPrimary,
    background         = AppBackground,
    onBackground       = TextPrimary,
    surface            = CardSurface,
    onSurface          = TextPrimary,
    surfaceVariant     = Color(0xFFEAEDF4),
    onSurfaceVariant   = TextSecondary,
    outline            = OutlineLight
)

private val DarkColorScheme = darkColorScheme(
    primary            = SportsPrimaryDark,
    onPrimary          = AppBackgroundDark,
    primaryContainer   = Color(0xFF003259),
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary          = SportsAccentDark,
    onSecondary        = AppBackgroundDark,
    background         = AppBackgroundDark,
    onBackground       = TextPrimaryDark,
    surface            = CardSurfaceDark,
    onSurface          = TextPrimaryDark,
    surfaceVariant     = Color(0xFF1F2330),
    onSurfaceVariant   = TextSecondaryDark,
    outline            = OutlineDark
)

@Composable
fun SportsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}