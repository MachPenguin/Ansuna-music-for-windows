package com.ansuna.music.window.ui.screen

import com.ansuna.music.window.model.Song
import com.ansuna.music.window.network.YouTubeMusicClient
import com.ansuna.music.window.player.PlayerViewModel
import com.ansuna.music.window.ui.component.SongCard
import com.ansuna.music.window.ui.component.TrackRow
import com.ansuna.music.window.ui.theme.AnsunaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.SwingUtilities
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font

/**
 * Home screen: recommended tracks from YouTube Music's home page, shown as
 * song cards in a horizontal grid — mirrors the Android HomeScreen layout.
 */
class HomeScreen(
  private val player: PlayerViewModel,
  private val onSongSelected: (Song) -> Unit
) : Screen {
  override val title = "Home"

  private val scope = CoroutineScope(Dispatchers.Default)
  private val root = JPanel()
  private val content = JPanel()
  private val status = JLabel("Loading recommendations…")

  init {
    root.layout = BoxLayout(root, BoxLayout.Y_AXIS)
    root.background = AnsunaTheme.background

    val header = JPanel(FlowLayout(FlowLayout.LEFT))
    header.background = AnsunaTheme.background
    header.border = BorderFactory.createEmptyBorder(16, 24, 8, 24)
    val headerLabel = JLabel("Recommendations")
    headerLabel.font = AnsunaTheme.headingFont(20)
    headerLabel.foreground = AnsunaTheme.textOnSurface
    header.add(headerLabel)
    root.add(header)

    status.alignmentX = 0f
    status.foreground = AnsunaTheme.textMuted
    status.border = BorderFactory.createEmptyBorder(0, 24, 8, 24)
    root.add(status)

    content.layout = BoxLayout(content, BoxLayout.Y_AXIS)
    content.background = AnsunaTheme.background
    content.border = BorderFactory.createEmptyBorder(0, 24, 24, 24)

    val scroll = JScrollPane(content)
    scroll.border = BorderFactory.createEmptyBorder()
    scroll.background = AnsunaTheme.background
    scroll.getViewport().background = AnsunaTheme.background
    scroll.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    root.add(scroll)

    load()
  }

  override fun onShow() = load()

  private fun load() {
    scope.launch {
      val songs = YouTubeMusicClient.instance.home()
      SwingUtilities.invokeLater {
        render(songs)
      }
    }
  }

  private fun render(songs: List<Song>) {
    content.removeAll()
    status.text = if (songs.isEmpty()) "No recommendations found." else ""

    // show up to 8 recommendations as cards in rows of 4
    var row = JPanel(FlowLayout(FlowLayout.LEFT, AnsunaTheme.gapMedium, AnsunaTheme.gapMedium))
    row.background = Color(0x00000000, true)
    row.isOpaque = false

    for ((i, song) in songs.withIndex()) {
      row.add(SongCard(song, player, onSongSelected))
      if ((i + 1) % 4 == 0 || i == songs.lastIndex) {
        content.add(row)
        if (i != songs.lastIndex) {
          row = JPanel(FlowLayout(FlowLayout.LEFT, AnsunaTheme.gapMedium, AnsunaTheme.gapMedium))
          row.background = Color(0x00000000, true)
          row.isOpaque = false
        }
      }
    }
    content.revalidate()
    content.repaint()
  }

  override val component: JPanel get() = root
}

