package com.ansuna.music.window

import com.ansuna.music.window.model.Song
import com.ansuna.music.window.player.PlayerViewModel
import com.ansuna.music.window.ui.component.MiniPlayer
import com.ansuna.music.window.ui.component.NavItem
import com.ansuna.music.window.ui.screen.HomeScreen
import com.ansuna.music.window.ui.screen.LibraryScreen
import com.ansuna.music.window.ui.screen.SearchScreen
import com.ansuna.music.window.ui.screen.Screen
import com.ansuna.music.window.ui.theme.AnsunaTheme
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.GridLayout
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities

/**
 * Main application window for Ansuna Music on Windows.
 *
 * Shell with a left navigation rail, the active screen content area, and a
 * docked mini-player at the bottom — replicating the Android app's navigation
 * structure (Home, Search, Library, Generate).
 */
class MainWindow : JFrame() {
  private val player = PlayerViewModel()
  private val content = JPanel()
  private var currentScreen: Screen? = null
  private val navButtons = mutableListOf<NavItem>()

  init {
    title = "Ansuna Music"
    defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    setSize(1100, 760)
    setLocationRelativeTo(null)
    setResizable(true)

    val root = JPanel(java.awt.BorderLayout())
    root.background = AnsunaTheme.background

    // Left navigation rail
    val nav = buildNav()
    nav.preferredSize = Dimension(72, Int.MAX_VALUE)
    nav.maximumSize = Dimension(72, Int.MAX_VALUE)
    root.add(nav, java.awt.BorderLayout.WEST)

    // Content area
    content.layout = java.awt.BorderLayout()
    content.background = AnsunaTheme.background
    content.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
    root.add(content, java.awt.BorderLayout.CENTER)

    // Docked mini-player
    root.add(MiniPlayer(player), java.awt.BorderLayout.SOUTH)

    add(root)

    // Default to Home
    showScreen(HomeScreen(player, ::selectSong))
  }

  /** Builds the left navigation rail. */
  private fun buildNav(): JPanel {
    val nav = JPanel()
    nav.layout = GridLayout(0, 1) // one column, variable rows
    (nav.layout as GridLayout).vgap = 8
    nav.background = AnsunaTheme.surface
    nav.border = BorderFactory.createMatteBorder(0, 0, 0, 1, AnsunaTheme.divider)

    val header = JLabel("Ansuna")
    header.font = AnsunaTheme.headingFont(18)
    header.foreground = AnsunaTheme.textOnSurface
    header.horizontalAlignment = javax.swing.SwingConstants.CENTER
    header.border = BorderFactory.createEmptyBorder(20, 8, 20, 8)
    nav.add(header)
    nav.add(Box.createRigidArea(Dimension(0, 16)))

    val items = listOf(
      NavItem("🏠", "Home", true, { showScreen(HomeScreen(player, ::selectSong)) }),
      NavItem("🔍", "Search", false, { showScreen(SearchScreen(player, ::selectSong)) }),
      NavItem("📚", "Library", false, { showScreen(LibraryScreen()) }),
      NavItem("✨", "Generate", false, { showScreen(LibraryScreen()) }) // placeholder
    )

    for (item in items) {
      nav.add(item)
      navButtons.add(item)
    }

    nav.add(Box.createVerticalGlue())
    return nav
  }

  /** Switches the content area to [screen]. */
  fun showScreen(screen: Screen) {
    currentScreen?.onHide()
    content.removeAll()
    content.add(screen.component, java.awt.BorderLayout.CENTER)
    currentScreen = screen
    content.revalidate()
    content.repaint()
    screen.onShow()
  }

  /** Selects a song from a screen: plays it and shows now-playing. */
  fun selectSong(song: Song) {
    player.play(song)
  }
}

