package com.ansuna.music.window.ui.theme

import java.awt.Color
import java.awt.Font

/**
 * Ansuna's Windows design system — a faithful port of the Android app's
 * theme (see original DESIGN.md and Theme.kt in the Android source).
 *
 * Carries over the exact brand colors, shapes, spacing and typography so the
 * Windows app looks and feels like the same app.
 */
object AnsunaTheme {
  // ---------- Color palette (dark theme, matches Android default seed #ED5564) ----------
  val background = Color(0x0A0A0A) // outer window / app background
  val surface = Color(0x121212)    // panels / cards base
  val surfaceElevated = Color(0x1A1A1A)
  val surfaceHover = Color(0x242424)
  val surfaceSelected = Color(0x2A2A2A)

  // Brand (carried over from Android Ansuna)
  val accent = Color(0xED5564)
  val accentHover = Color(0xC44452)
  val accentPressed = Color(0xA83A44)

  // Text
  val textOnSurface = Color.WHITE
  val textMuted = Color(0xA0A0A0)
  val textDisabled = Color(0x666666)

  // Borders / dividers
  val divider = Color(0x2A2A2A)

  // Translucent card surface (Android: surfaceVariant.copy(alpha = 0.3f))
  val cardSurface = Color(0x1A1A1A) // approx. 30% white on surface

  // Playback states
  val playingGreen = Color(0x1DB954)

  // ---------- Shapes ----------
  val cornerRadius = 24     // RoundedCornerShape(24.dp)
  val pillRadius = 100      // fully rounded / pill
  val iconSize = 56         // 56.dp circular icon
  val searchHeight = 48     // 48.dp search field
  val tabBarHeight = 56     // 56.dp floating tab bar
  val miniPlayerHeight = 64 // docked mini player

  // ---------- Spacing ----------
  val paddingScreen = 24
  val paddingCard = 16
  val gapSmall = 8
  val gapMedium = 16
  val gapLarge = 24

  // ---------- Typography (mirrors AppTypography in Android Type.kt) ----------
  fun font(bold: Boolean = false, size: Int = 16): Font {
    val style = if (bold) Font.BOLD else Font.PLAIN
    return Font("Segoe UI", style, size)
  }

  fun headingFont(size: Int = 20): Font = Font("Segoe UI", Font.BOLD, size)

  // ---------- Color helpers ----------
  fun withAlpha(color: Color, alpha: Float): Color =
    Color(color.red, color.green, color.blue, (255 * alpha).toInt())
}
