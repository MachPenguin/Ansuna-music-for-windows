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
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

/**
 * Bottom navigation item — circular icon (56dp) + label below, matching
 * Android NavigationTile. Selected state shows accent tint.
 */
class NavItem(
  private val glyph: String,
  private val label: String,
  private var selected: Boolean,
  private val onClick: () -> Unit
) : JPanel() {
  private var hovered = false

  init {
    layout = java.awt.BorderLayout()
    background = Color(0x00000000, true)
    preferredSize = Dimension(72, 92)
    cursor = Cursor(Cursor.HAND_CURSOR)
    setBorder(EmptyBorder(4, 4, 4, 4))

    val icon = object : JPanel() {
      override fun paintComponent(g: Graphics) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = if (this@NavItem.selected) AnsunaTheme.surfaceSelected
          else AnsunaTheme.withAlpha(Color.WHITE, 0.06f)
        g2.fillRoundRect(0, 0, 56, 56, 28, 28)
        g2.font = AnsunaTheme.font(bold = true, size = 20)
        g2.color = if (this@NavItem.selected) AnsunaTheme.accent else AnsunaTheme.textMuted
        val fm = g2.fontMetrics
        val w = fm.stringWidth(this@NavItem.glyph)
        val h = fm.getAscent()
        g2.drawString(this@NavItem.glyph, (56 - w) / 2, (56 / 2) + h / 2 - 2)
        super.paintComponent(g)
      }
    }
    icon.background = Color(0x00000000, true)
    icon.preferredSize = Dimension(56, 56)
    icon.isOpaque = false

    val lbl = javax.swing.JLabel(label)
    lbl.font = AnsunaTheme.font(size = 11)
    lbl.foreground = if (selected) AnsunaTheme.textOnSurface else AnsunaTheme.textMuted
    lbl.horizontalAlignment = javax.swing.SwingConstants.CENTER

    add(icon, java.awt.BorderLayout.NORTH)
    add(lbl, java.awt.BorderLayout.SOUTH)

    addMouseListener(object : MouseAdapter() {
      override fun mouseEntered(e: MouseEvent) { hovered = true; repaint() }
      override fun mouseExited(e: MouseEvent) { hovered = false; repaint() }
      override fun mouseClicked(e: MouseEvent) { onClick() }
    })
  }

  fun setSelected(selected: Boolean) {
    this.selected = selected
    repaint()
  }

  override fun paintComponent(g: Graphics) {
    super.paintComponent(g)
    if (hovered && !selected) {
      val g2 = g as Graphics2D
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      g2.color = AnsunaTheme.withAlpha(Color.WHITE, 0.1f)
      g2.fillRoundRect(4, 4, width - 8, height - 8, 20, 20)
    }
  }
}

