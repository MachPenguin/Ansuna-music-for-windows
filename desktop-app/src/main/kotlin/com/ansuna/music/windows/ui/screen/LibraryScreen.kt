package com.ansuna.music.window.ui.screen

import com.ansuna.music.window.ui.theme.AnsunaTheme
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.FlowLayout

/**
 * Library screen placeholder — wired into navigation but not yet populated
 * with local media (keeps the same nav structure as the Android app).
 */
class LibraryScreen : Screen {
  override val title = "Library"

  private val root = JPanel()

  init {
    root.layout = FlowLayout(FlowLayout.LEFT)
    root.background = AnsunaTheme.background
    root.border = BorderFactory.createEmptyBorder(24, 24, 24, 24)

    val label = JLabel("Library")
    label.font = AnsunaTheme.headingFont(20)
    label.foreground = AnsunaTheme.textOnSurface
    root.add(label)

    val subtitle = JLabel("Your playlists, albums and artists appear here.")
    subtitle.foreground = AnsunaTheme.textMuted
    root.add(subtitle)
  }

  override val component: JPanel get() = root
}

