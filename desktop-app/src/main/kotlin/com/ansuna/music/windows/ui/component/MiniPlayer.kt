package com.ansuna.music.window.ui.component

import com.ansuna.music.window.model.Song
import com.ansuna.music.window.player.PlayerViewModel
import com.ansuna.music.window.ui.theme.AnsunaTheme
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import javax.swing.JPanel
import javax.swing.border.EmptyBorder
import javax.swing.Timer

/**
 * Docked bottom mini-player showing the currently playing track with play/pause.
 * Mirrors Ansuna's now-playing bar at the bottom of the screen.
 */
class MiniPlayer(
  private val player: PlayerViewModel
) : JPanel() {
  private val trackLabel = javax.swing.JLabel("No track playing")
  private val artistLabel = javax.swing.JLabel("")
  private val playBtn = IconButton("▶", {
    player.toggle()
  }, bg = AnsunaTheme.accent, fg = Color.WHITE, size = 36)

  init {
    layout = java.awt.BorderLayout()
    background = AnsunaTheme.surfaceElevated
    setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, AnsunaTheme.divider))
    preferredSize = Dimension(Int.MAX_VALUE, AnsunaTheme.miniPlayerHeight)
    maximumSize = Dimension(Int.MAX_VALUE, AnsunaTheme.miniPlayerHeight)

    trackLabel.font = AnsunaTheme.font(bold = true, size = 13)
    trackLabel.foreground = AnsunaTheme.textOnSurface
    trackLabel.border = EmptyBorder(0, 16, 0, 0)

    artistLabel.font = AnsunaTheme.font(size = 11)
    artistLabel.foreground = AnsunaTheme.textMuted
    artistLabel.border = EmptyBorder(0, 16, 0, 0)

    val info = JPanel(java.awt.BorderLayout())
    info.background = Color(0x00000000, true)
    info.isOpaque = false
    info.add(trackLabel, java.awt.BorderLayout.NORTH)
    info.add(artistLabel, java.awt.BorderLayout.SOUTH)

    add(info, java.awt.BorderLayout.CENTER)
    add(playBtn, java.awt.BorderLayout.EAST)

    // refresh display periodically
    Timer(500, { _ ->
      val song = player.nowPlaying
      trackLabel.text = if (song != null) "${song.title} — ${song.artist}" else "No track playing"
      artistLabel.text = if (song != null) song.album else ""
      playBtn.setGlyph(if (player.isPlaying) "⏸" else "▶")
      playBtn.repaint()
    }).start()

    addMouseListener(object : java.awt.event.MouseAdapter() {
      override fun mouseClicked(e: java.awt.event.MouseEvent) { player.toggle() }
    })
  }
}

