package com.ansuna.music.window.ui.component

import com.ansuna.music.window.model.Song
import com.ansuna.music.window.player.PlayerViewModel
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.Color
import java.awt.Dimension
import java.awt.Font

/**
 * A single track row: title, artist/album, and a play button.
 *
 * Colors mirror Ansuna's Android dark theme (#121212 surface, #ED5564 accent).
 */
class TrackRow(
  song: Song,
  player: PlayerViewModel,
  onSelected: (Song) -> Unit
) : JPanel() {
  init {
    layout = BoxLayout(this, BoxLayout.X_AXIS)
    background = Color(0x121212)
    border = BorderFactory.createCompoundBorder(
      BorderFactory.createMatteBorder(0, 0, 1, 0, Color(0x2A2A2A)),
      BorderFactory.createEmptyBorder(10, 12, 10, 12)
    )
    maximumSize = Dimension(Integer.MAX_VALUE, 60)

    val info = JPanel()
    info.layout = BoxLayout(info, BoxLayout.Y_AXIS)
    info.background = Color(0x121212)
    info.alignmentX = 0f

    val titleLabel = JLabel(song.title)
    titleLabel.font = Font("Segoe UI", Font.BOLD, 14)
    titleLabel.foreground = Color.WHITE

    val subLabel = JLabel("${song.artist}${if (song.album.isNotBlank()) " · ${song.album}" else ""}")
    subLabel.font = Font("Segoe UI", Font.PLAIN, 12)
    subLabel.foreground = Color(0xA0A0A0)

    info.add(titleLabel)
    info.add(subLabel)

    val playBtn = JButton("▶")
    playBtn.font = Font("Segoe UI", Font.PLAIN, 14)
    playBtn.background = Color(0xED5564)
    playBtn.foreground = Color.WHITE
    playBtn.border = BorderFactory.createEmptyBorder(6, 14, 6, 14)
    playBtn.maximumSize = Dimension(46, 32)
    playBtn.alignmentY = 0.5f
    playBtn.addActionListener {
      player.play(song)
      onSelected(song)
    }

    add(info)
    add(javax.swing.Box.createHorizontalGlue())
    add(playBtn)
  }
}
