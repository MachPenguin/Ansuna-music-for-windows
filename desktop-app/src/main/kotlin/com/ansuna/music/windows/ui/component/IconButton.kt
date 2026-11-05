package com.ansuna.music.window.ui.component

import com.ansuna.music.window.ui.theme.AnsunaTheme
import java.awt.BasicStroke
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
 * An icon-only action button (▶, ⏸, ❤, etc.) on a circular/surface background,
 * matching Ansuna's icon-only 48dp touch targets.
 */
class IconButton(
  private var glyph: String,
  private val onClick: () -> Unit = {},
  private val bg: Color = AnsunaTheme.surface,
  private val fg: Color = AnsunaTheme.textOnSurface,
  private val size: Int = 48
) : AbstractButton() {
  private var hovered = false
  private var pressed = false

  init {
    font = AnsunaTheme.font(bold = true, size = 18)
    foreground = fg
    setFocusPainted(false)
    setBorderPainted(false)
    setContentAreaFilled(false)
    cursor = Cursor(Cursor.HAND_CURSOR)
    preferredSize = Dimension(size, size)
    setBorder(EmptyBorder(4, 4, 4, 4))

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

  fun setGlyph(newGlyph: String) {
    glyph = newGlyph
    repaint()
  }

  override fun paintComponent(g: Graphics) {
    val g2 = g as Graphics2D
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

    val fill = when {
      pressed -> AnsunaTheme.withAlpha(bg, 0.6f)
      hovered -> AnsunaTheme.surfaceHover
      else -> bg
    }
    g2.color = fill
    g2.fillRoundRect(0, 0, width, height, AnsunaTheme.pillRadius, AnsunaTheme.pillRadius)

    val fm = g2.fontMetrics
    val glyphWidth = fm.stringWidth(glyph)
    val glyphHeight = fm.getAscent()
    g2.color = fg
    g2.drawString(glyph, (width - glyphWidth) / 2, (height / 2) + glyphHeight / 2 - 2)

    super.paintComponent(g)
  }

  override fun getPreferredSize(): Dimension = Dimension(size, size)
}

