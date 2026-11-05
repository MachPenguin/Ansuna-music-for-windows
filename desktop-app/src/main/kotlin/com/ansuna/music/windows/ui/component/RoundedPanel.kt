package com.ansuna.music.window.ui.component

import com.ansuna.music.window.ui.theme.AnsunaTheme
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

/**
 * A JPanel with rounded corners and a solid fill, mirroring Ansuna's custom
 * cards (surfaceVariant with alpha, RoundedCornerShape(24.dp), flat elevation).
 */
open class RoundedPanel(
  private val bg: Color = AnsunaTheme.surface,
  private val radius: Int = AnsunaTheme.cornerRadius,
  private val border: Color? = null,
  private val borderThickness: Float = 1f
) : JPanel() {
  init {
    isOpaque = false
    setBorder(EmptyBorder(AnsunaTheme.paddingCard, AnsunaTheme.paddingCard,
      AnsunaTheme.paddingCard, AnsunaTheme.paddingCard))
    background = bg
    preferredSize = Dimension(Int.MAX_VALUE, Int.MAX_VALUE)
  }

  override fun paintComponent(g: Graphics) {
    val g2 = g as Graphics2D
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

    g2.color = bg
    g2.fillRoundRect(0, 0, width, height, radius, radius)

    if (border != null) {
      g2.color = border
      g2.stroke = java.awt.BasicStroke(borderThickness)
      g2.drawRoundRect(0, 0, width - 1, height - 1, radius, radius)
    }

    super.paintComponent(g)
  }
}

