package com.ansuna.music.window.ui.screen

import com.ansuna.music.window.model.Song
import com.ansuna.music.window.network.YouTubeMusicClient
import com.ansuna.music.window.player.PlayerViewModel
import com.ansuna.music.window.ui.component.SearchBar
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

/**
 * Search screen: query YouTube Music and play results — mirrors the Android
 * SearchScreen with the same search bar + track rows.
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
  private val searchBar = SearchBar { query -> search(query) }

  init {
    root.layout = BoxLayout(root, BoxLayout.Y_AXIS)
    root.background = AnsunaTheme.background

    val header = JPanel(java.awt.FlowLayout(java.awt.FlowLayout.LEFT))
    header.background = AnsunaTheme.background
    header.border = BorderFactory.createEmptyBorder(16, 24, 8, 24)
    val headerLabel = JLabel("Search")
    headerLabel.font = AnsunaTheme.headingFont(20)
    headerLabel.foreground = AnsunaTheme.textOnSurface
    header.add(headerLabel)
    root.add(header)

    root.add(searchBar)

    status.alignmentX = 0f
    status.foreground = AnsunaTheme.textMuted
    status.border = BorderFactory.createEmptyBorder(0, 24, 8, 24)
    root.add(status)

    trackList.layout = BoxLayout(trackList, BoxLayout.Y_AXIS)
    trackList.background = AnsunaTheme.background
    trackList.border = BorderFactory.createEmptyBorder(0, 24, 24, 24)

    val scroll = JScrollPane(trackList)
    scroll.border = BorderFactory.createEmptyBorder()
    scroll.background = AnsunaTheme.background
    scroll.getViewport().background = AnsunaTheme.background
    scroll.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    root.add(scroll)
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
    status.border = BorderFactory.createEmptyBorder(0, 24, 8, 24)
    for (song in songs) {
      trackList.add(TrackRow(song, player, onSongSelected))
    }
    trackList.revalidate()
    trackList.repaint()
    searchBar.requestFocus()
  }

  override val component: JPanel get() = root
}

