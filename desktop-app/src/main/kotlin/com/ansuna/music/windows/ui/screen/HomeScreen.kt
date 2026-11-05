package com.ansuna.music.window.ui.screen

import com.ansuna.music.window.model.Song
import com.ansuna.music.window.network.YouTubeMusicClient
import com.ansuna.music.window.player.PlayerViewModel
import com.ansuna.music.window.ui.component.TrackRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font

/**
 * Home screen: recommended tracks from YouTube Music's home page.
 */
class HomeScreen(
  private val player: PlayerViewModel,
  private val onSongSelected: (Song) -> Unit
) : Screen {
  override val title = "Home"

  private val scope = CoroutineScope(Dispatchers.Default)
  private val root = JPanel()
  private val trackList = JPanel()
  private val status = JLabel("Loading recommendations…")

  init {
    root.layout = BoxLayout(root, BoxLayout.Y_AXIS)
    root.background = Color(0x0A0A0A)

    val header = JPanel(FlowLayout(FlowLayout.LEFT))
    header.background = Color(0x0A0A0A)
    header.border = BorderFactory.createEmptyBorder(16, 24, 8, 24)
    val headerLabel = JLabel("Recommendations")
    headerLabel.font = Font("Segoe UI", Font.BOLD, 20)
    headerLabel.foreground = Color.WHITE
    header.add(headerLabel)
    root.add(header)

    status.alignmentX = 0f
    status.foreground = Color(0xA0A0A0)
    status.border = BorderFactory.createEmptyBorder(0, 24, 8, 24)
    root.add(status)

    trackList.layout = BoxLayout(trackList, BoxLayout.Y_AXIS)
    trackList.background = Color(0x0A0A0A)
    trackList.border = BorderFactory.createEmptyBorder(0, 24, 24, 24)
    root.add(trackList)

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
    trackList.removeAll()
    status.text = if (songs.isEmpty()) "No recommendations found." else ""

    for (song in songs) {
      trackList.add(TrackRow(song, player, onSongSelected))
    }
    trackList.revalidate()
    trackList.repaint()
  }

  override val component: JPanel get() = root
}
