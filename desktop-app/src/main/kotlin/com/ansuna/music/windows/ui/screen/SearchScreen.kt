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
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingUtilities
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font

/**
 * Search screen: query YouTube Music and play results.
 */
class SearchScreen(
  private val player: PlayerViewModel,
  private val onSongSelected: (Song) -> Unit
) : Screen {
  override val title = "Search"

  private val scope = CoroutineScope(Dispatchers.Default)
  private val root = JPanel()
  private val trackList = JPanel()
  private val status = JLabel("")

  init {
    root.layout = BoxLayout(root, BoxLayout.Y_AXIS)
    root.background = Color(0x0A0A0A)

    val header = JPanel(FlowLayout(FlowLayout.LEFT))
    header.background = Color(0x0A0A0A)
    header.border = BorderFactory.createEmptyBorder(16, 24, 8, 24)
    val headerLabel = JLabel("Search")
    headerLabel.font = Font("Segoe UI", Font.BOLD, 20)
    headerLabel.foreground = Color.WHITE
    header.add(headerLabel)
    root.add(header)

    val searchBar = JPanel(FlowLayout(FlowLayout.LEFT))
    searchBar.background = Color(0x0A0A0A)
    searchBar.border = BorderFactory.createEmptyBorder(0, 24, 16, 24)
    val field = JTextField(30)
    field.font = Font("Segoe UI", Font.PLAIN, 15)
    val btn = JButton("Search")
    btn.font = Font("Segoe UI", Font.PLAIN, 14)
    btn.background = Color(0xED5564)
    btn.foreground = Color.WHITE
    btn.setFocusPainted(false)
    btn.border = BorderFactory.createEmptyBorder(6, 16, 6, 16)

    btn.addActionListener {
      val q = field.text
      if (q.isNotBlank()) search(q)
    }
    field.addActionListener {
      val q = field.text
      if (q.isNotBlank()) search(q)
    }

    searchBar.add(field)
    searchBar.add(btn)
    root.add(searchBar)

    status.alignmentX = 0f
    status.foreground = Color(0xA0A0A0)
    status.border = BorderFactory.createEmptyBorder(0, 24, 8, 24)
    root.add(status)

    trackList.layout = BoxLayout(trackList, BoxLayout.Y_AXIS)
    trackList.background = Color(0x0A0A0A)
    trackList.border = BorderFactory.createEmptyBorder(0, 24, 24, 24)
    root.add(trackList)
  }

  private fun search(query: String) {
    scope.launch {
      val songs = YouTubeMusicClient.instance.search(query)
      SwingUtilities.invokeLater {
        render(songs, query)
      }
    }
  }

  private fun render(songs: List<Song>, query: String) {
    trackList.removeAll()
    status.text = if (songs.isEmpty()) "No results for '$query'." else "$query — ${songs.size} result(s)"
    for (song in songs) {
      trackList.add(TrackRow(song, player, onSongSelected))
    }
    trackList.revalidate()
    trackList.repaint()
  }

  override val component: JPanel get() = root
}
