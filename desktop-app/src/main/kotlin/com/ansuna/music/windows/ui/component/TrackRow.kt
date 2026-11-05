package com.ansuna.music.window.ui.component

import com.ansuna.music.window.model.Song
import com.ansuna.music.window.player.PlayerViewModel
import com.ansuna.music.window.ui.theme.AnsunaTheme
import java.awt.Color
import java.awt.Cursor
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

/**
 * A single track row: title, artist/album, and a play button — matching
 * Ansuna's list item design (24dp card surface, divider, accent play button).
 */
class TrackRow(
  song: Song,
  player: PlayerViewModel,
  private val onSelected: (Song) -> Unit
) : JPanel() {
  private var hovered = false

  init {
    layout = java.awt.BorderLayout()
    background = Color(0x121212)
    setBorder(EmptyBorder(8, 12, 8, 12))
    preferredSize = Dimension(Int.MAX_VALUE, 60)
    maximumSize = Dimension(Int.MAX_VALUE, 60)
    minimumSize = Dimension(0, 60)

    val titleLabel = javax.swing.JLabel(song.title)
    titleLabel.font = AnsunaTheme.font(bold = true, size = 14)
    titleLabel.foreground = AnsunaTheme.textOnSurface

    val subLabel = javax.swing.JLabel(
      "${song.artist}${if (song.album.isNotBlank()) " · ${song.album}" else ""}"
    )
    subLabel.font = AnsunaTheme.font(size = 12)
    subLabel.foreground = AnsunaTheme.textMuted

    val infoPanel = JPanel()
    infoPanel.layout = java.awt.BorderLayout()
    infoPanel.background = Color(0x00000000, true)
    infoPanel.isOpaque = false
    infoPanel.add(titleLabel, java.awt.BorderLayout.NORTH)
    infoPanel.add(subLabel, java.awt.BorderLayout.SOUTH)

    val playBtn = IconButton("▶", {
      player.play(song)
      onSelected(song)
    }, bg = AnsunaTheme.accent, fg = Color.WHITE, size = 32)
    playBtn.maximumSize = Dimension(32, 32)
    playBtn.preferredSize = Dimension(32, 32)

    add(infoPanel, java.awt.BorderLayout.CENTER)
    add(playBtn, java.awt.BorderLayout.EAST)

    addMouseListener(object : MouseAdapter() {
      override fun mouseEntered(e: MouseEvent) {
        hovered = true
        background = AnsunaTheme.surfaceHover
        repaint()
      }
      override fun mouseExited(e: MouseEvent) {
        hovered = false
        background = Color(0x121212)
        repaint()
      }
    })
  }

  override fun paintComponent(g: Graphics) {
    val g2 = g as Graphics2D
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

    // subtle divider at bottom
    g2.color = AnsunaTheme.divider
    g2.fillRect(0, height - 1, width, 1)

    super.paintComponent(g)
  }
}

