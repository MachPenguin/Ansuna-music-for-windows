package com.ansuna.music.window.ui.component

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
import javax.swing.AbstractButton
import javax.swing.border.EmptyBorder

/**
 * A filled accent button (#ED5564) with hover/pressed states and rounded
 * corners — matches Ansuna's primary action buttons.
 */
class AccentButton(
  label: String,
  private val onClick: () -> Unit = {}
) : AbstractButton() {
  private var hovered = false
  private var pressed = false

  init {
    text = label
    font = AnsunaTheme.font(bold = true, size = 14)
    foreground = Color.WHITE
    setFocusPainted(false)
    setBorderPainted(false)
    setContentAreaFilled(false)
    cursor = Cursor(Cursor.HAND_CURSOR)
    preferredSize = Dimension(120, 36)
    setBorder(EmptyBorder(6, 16, 6, 16))

    addMouseListener(object : MouseAdapter() {
      override fun mouseEntered(e: MouseEvent) { hovered = true; repaint() }
      override fun mouseExited(e: MouseEvent) { hovered = false; repaint() }
      override fun mousePressed(e: MouseEvent) { pressed = true; repaint() }
      override fun mouseReleased(e: MouseEvent) {
        pressed = false
        if (contains(e.point)) onClick()
        repaint()
      }
    })
  }

  override fun paintComponent(g: Graphics) {
    val g2 = g as Graphics2D
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

    val fill = when {
      pressed -> AnsunaTheme.accentPressed
      hovered -> AnsunaTheme.accentHover
      else -> AnsunaTheme.accent
    }
    g2.color = fill
    g2.fillRoundRect(0, 0, width, height, AnsunaTheme.cornerRadius, AnsunaTheme.cornerRadius)

    // center the text
    val fm = g2.fontMetrics
    val textWidth = fm.stringWidth(text)
    val textHeight = fm.getAscent()
    g2.color = foreground
    g2.drawString(text, (width - textWidth) / 2, (height / 2) + textHeight / 2 - 2)

    super.paintComponent(g)
  }

  override fun getPreferredSize(): Dimension {
    val fm = getFontMetrics(font)
    return Dimension(fm.stringWidth(text) + 32, 36)
  }
}

