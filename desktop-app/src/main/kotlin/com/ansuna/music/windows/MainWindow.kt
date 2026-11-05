package com.ansuna.music.window

import com.ansuna.music.window.model.Song
import com.ansuna.music.window.player.PlayerViewModel
import com.ansuna.music.window.ui.screen.HomeScreen
import com.ansuna.music.window.ui.screen.SearchScreen
import com.ansuna.music.window.ui.screen.Screen
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Font

/**
 * Main application window for Ansuna Music.
 *
 * Shell with a sidebar navigation, the active screen content, and a docked
 * mini-player at the bottom showing the currently playing track.
 */
class MainWindow : JFrame() {
  private val player = PlayerViewModel()
  private val content = JPanel()
  private var currentScreen: Screen? = null

  // Now-playing mini player
  private val nowPlayingLabel = JLabel("No track playing")
  private val playPauseBtn = JButton("▶")

  init {
    title = "Ansuna Music"
    defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    setSize(1100, 760)
    setLocationRelativeTo(null)

    val root = JPanel(BorderLayout())
    root.background = Color(0x0A0A0A)

    // Sidebar navigation
    val nav = buildNav()
    nav.preferredSize = Dimension(220, Int.MAX_VALUE)
    root.add(nav, BorderLayout.WEST)

    // Content area
    content.layout = BorderLayout()
    content.background = Color(0x121212)
    content.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
    root.add(content, BorderLayout.CENTER)

    // Mini player dock
    root.add(buildMiniPlayer(), BorderLayout.SOUTH)

    add(root)

    // Default to Home
    showScreen(HomeScreen(player, ::selectSong))
  }

  /** Builds the left sidebar navigation. */
  private fun buildNav(): JPanel {
    val nav = JPanel()
    nav.layout = BoxLayout(nav, BoxLayout.Y_AXIS)
    nav.background = Color(0x121212)
    nav.border = BorderFactory.createEmptyBorder(16, 12, 16, 12)

    val header = JLabel("Ansuna")
    header.font = Font("Segoe UI", Font.BOLD, 20)
    header.foreground = Color.WHITE
    header.alignmentX = 0f
    nav.add(header)
    nav.add(javax.swing.Box.createRigidArea(Dimension(0, 24)))

    val items = listOf(
      NavItem("Home", HomeScreen(player, this::selectSong)),
      NavItem("Search", SearchScreen(player, this::selectSong)),
      NavItem("Library", null), // TODO: library screen
      NavItem("Settings", null), // TODO: settings screen
    )

    for (item in items) {
      val btn = JButton(item.title)
      btn.font = Font("Segoe UI", Font.PLAIN, 14)
      btn.background = Color(0x121212)
      btn.foreground = Color(0xA0A0A0)
      btn.setFocusPainted(false)
      btn.border = BorderFactory.createEmptyBorder(8, 4, 8, 4)
      btn.maximumSize = Dimension(Int.MAX_VALUE, 36)
      btn.alignmentX = 0f
      btn.setContentAreaFilled(false)
      btn.setBorderPainted(false)
      btn.addActionListener {
        // highlight
        for (b in nav.components) {
          if (b is JButton) b.background = Color(0x121212)
        }
        btn.background = Color(0x1A1A1A)
        item.screen?.let { showScreen(it) }
      }
      item.button = btn
      nav.add(btn)
      nav.add(javax.swing.Box.createRigidArea(Dimension(0, 6)))
    }

    nav.add(javax.swing.Box.createVerticalGlue())
    return nav
  }

  /** Builds the bottom mini-player dock. */
  private fun buildMiniPlayer(): JPanel {
    val bar = JPanel(BorderLayout())
    bar.background = Color(0x1A1A1A)
    bar.border = BorderFactory.createMatteBorder(1, 0, 0, 0, Color(0x2A2A2A))
    bar.preferredSize = Dimension(Int.MAX_VALUE, 64)
    bar.maximumSize = Dimension(Int.MAX_VALUE, 64)

    nowPlayingLabel.font = Font("Segoe UI", Font.BOLD, 13)
    nowPlayingLabel.foreground = Color.WHITE
    nowPlayingLabel.border = BorderFactory.createEmptyBorder(0, 14, 0, 0)

    playPauseBtn.font = Font("Segoe UI", Font.PLAIN, 13)
    playPauseBtn.background = Color(0xED5564)
    playPauseBtn.foreground = Color.WHITE
    playPauseBtn.setFocusPainted(false)
    playPauseBtn.border = BorderFactory.createEmptyBorder(4, 14, 4, 14)
    playPauseBtn.addActionListener { player.toggle() }

    bar.add(nowPlayingLabel, BorderLayout.WEST)
    bar.add(playPauseBtn, BorderLayout.EAST)

    // React to playback state changes
    player.player // keep reference; poll below
    SwingUtilities.invokeLater(object : Runnable {
      override fun run() {
        updateMiniPlayer()
        // refresh every 500 ms while something is loaded
        javax.swing.Timer(500, { _ ->
          updateMiniPlayer()
        }).start()
      }
    })

    return bar
  }

  private fun updateMiniPlayer() {
    val song = player.nowPlaying
    val text = if (song != null) "${song.title} — ${song.artist}" else "No track playing"
    nowPlayingLabel.text = text
    playPauseBtn.text = if (player.isPlaying) "⏸" else "▶"
    playPauseBtn.isEnabled = song != null
  }

  /** Switches the content area to [screen]. */
  fun showScreen(screen: Screen) {
    currentScreen?.onHide()
    content.removeAll()
    content.add(screen.component, BorderLayout.CENTER)
    currentScreen = screen
    content.revalidate()
    content.repaint()
    screen.onShow()
  }

  /** Selects a song from a screen: opens it and shows now-playing. */
  fun selectSong(song: Song) {
    player.play(song)
    updateMiniPlayer()
  }

  /** Nav item pairing a label with its screen. */
  private class NavItem(
    val title: String,
    val screen: Screen?,
  ) {
    var button: JButton? = null
  }
}
