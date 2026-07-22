package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = PrimaryIndigoDark,
  onPrimary = BackgroundDark,
  primaryContainer = PrimaryContainerDark,
  onPrimaryContainer = OnPrimaryContainerDark,
  secondary = SecondaryTealDark,
  onSecondary = BackgroundDark,
  secondaryContainer = SecondaryContainerDark,
  onSecondaryContainer = OnSecondaryContainerDark,
  tertiary = AccentCoral,
  background = BackgroundDark,
  surface = SurfaceDark,
  surfaceVariant = SurfaceCardDark,
  onBackground = OnSurfaceDark,
  onSurface = OnSurfaceDark,
  onSurfaceVariant = OnSurfaceVariantDark
)

private val LightColorScheme = lightColorScheme(
  primary = PrimaryIndigo,
  onPrimary = OnPrimaryWhite,
  primaryContainer = PrimaryContainerIndigo,
  onPrimaryContainer = OnPrimaryContainerIndigo,
  secondary = SecondaryTeal,
  onSecondary = OnSecondaryWhite,
  secondaryContainer = SecondaryContainerTeal,
  onSecondaryContainer = OnSecondaryContainerTeal,
  tertiary = AccentCoral,
  background = SurfaceLight,
  surface = SurfaceCardLight,
  surfaceVariant = SurfaceVariantLight,
  onBackground = OnSurfaceLight,
  onSurface = OnSurfaceLight,
  onSurfaceVariant = OnSurfaceVariantLight
)

@Composable
fun TaskTrackerTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

