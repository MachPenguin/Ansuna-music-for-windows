package com.ansuna.music.window.ui.theme

import java.awt.Color

/**
 * Ansuna's dark design system for Windows.
 *
 * Keeps the Android app's brand accent (#ED5564) on a near-black surface so
 * the port feels like the same app, just on desktop.
 */
object AnsunaDesign {
  // Surfaces
  val background = Color(0x0A0A0A) // outer window background
  val surface = Color(0x121212)    // panels / cards
  val surfaceElevated = Color(0x1A1A1A)
  val surfaceHover = Color(0x242424)
  val surfaceSelected = Color(0x2A2A2A)

  // Brand (carried over from Android Ansuna)
  val accent = Color(0xED5564)
  val accentHover = Color(0xC44452)

  // Text
  val textOnSurface = Color.WHITE
  val textMuted = Color(0xA0A0A0)
  val textDisabled = Color(0x666666)

  // Borders / dividers
  val divider = Color(0x2A2A2A)

  // Playback states
  val playingGreen = Color(0x1DB954)
}
