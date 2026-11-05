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
 * A song card — thumbnail artwork + title/artist, on a translucent surface
 * with 24dp rounded corners (mirrors Android card rule: surfaceVariant alpha
 * 0.3f, RoundedCornerShape(24.dp), flat elevation).
 */
open class SongCard(
  song: Song,
  player: PlayerViewModel,
  private val onSelected: (Song) -> Unit
) : RoundedPanel(bg = AnsunaTheme.cardSurface, radius = AnsunaTheme.cornerRadius) {
  init {
    layout = java.awt.BorderLayout()
    preferredSize = Dimension(220, 260)
    maximumSize = Dimension(220, 260)
    cursor = Cursor(Cursor.HAND_CURSOR)

    // thumbnail placeholder (rounded square where album art would go)
    val thumb = object : JPanel() {
      override fun paintComponent(g: Graphics) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = AnsunaTheme.surfaceElevated
        g2.fillRoundRect(0, 0, width, height, 16, 16)
        super.paintComponent(g)
      }
    }
    thumb.background = AnsunaTheme.surfaceElevated
    thumb.preferredSize = Dimension(220, 160)
    thumb.maximumSize = Dimension(220, 160)
    thumb.isOpaque = false

    val titleLabel = javax.swing.JLabel(song.title)
    titleLabel.font = AnsunaTheme.font(bold = true, size = 14)
    titleLabel.foreground = AnsunaTheme.textOnSurface

    val artistLabel = javax.swing.JLabel(song.artist)
    artistLabel.font = AnsunaTheme.font(size = 12)
    artistLabel.foreground = AnsunaTheme.textMuted

    val infoPanel = JPanel()
    infoPanel.layout = java.awt.BorderLayout()
    infoPanel.background = Color(0x00000000, true)
    infoPanel.isOpaque = false
    infoPanel.add(titleLabel, java.awt.BorderLayout.NORTH)
    infoPanel.add(artistLabel, java.awt.BorderLayout.SOUTH)

    add(thumb, java.awt.BorderLayout.NORTH)
    add(infoPanel, java.awt.BorderLayout.SOUTH)

    addMouseListener(object : MouseAdapter() {
      override fun mouseEntered(e: MouseEvent) {
        background = AnsunaTheme.surfaceHover
        repaint()
      }
      override fun mouseExited(e: MouseEvent) {
        background = AnsunaTheme.cardSurface
        repaint()
      }
      override fun mouseClicked(e: MouseEvent) {
        player.play(song)
        onSelected(song)
      }
    })
  }
}

