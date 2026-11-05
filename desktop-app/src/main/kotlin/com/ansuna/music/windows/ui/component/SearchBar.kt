package com.ansuna.music.window.ui.component

import com.ansuna.music.window.ui.theme.AnsunaTheme
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.border.EmptyBorder
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/**
 * Ansuna's top search bar: a transparent text field inside a surface bar with
 * rounded corners — mirrors the Android TopSearch / SearchBarInputField.
 */
class SearchBar(
  private val onSearch: (String) -> Unit
) : JPanel() {
  private val field = JTextField()

  init {
    layout = null
    background = Color(0x0A0A0A)
    setBorder(EmptyBorder(16, 24, 16, 24))
    preferredSize = Dimension(Int.MAX_VALUE, 80)

    field.font = AnsunaTheme.font(size = 15)
    field.foreground = AnsunaTheme.textOnSurface
    field.background = Color(0x00000000, true) // transparent
    field.border = EmptyBorder(0, 16, 0, 16)
    field.caretColor = AnsunaTheme.accent
    field.maximumSize = Dimension(Int.MAX_VALUE, AnsunaTheme.searchHeight)
    field.preferredSize = Dimension(Int.MAX_VALUE, AnsunaTheme.searchHeight)

    field.addActionListener { e -> onSearch(field.text.trim()) }
    field.getDocument().addDocumentListener(object : DocumentListener {
      override fun insertUpdate(e: DocumentEvent) = check()
      override fun removeUpdate(e: DocumentEvent) = check()
      override fun changedUpdate(e: DocumentEvent) = check()
      private fun check() {
        if (field.text.trim().isNotEmpty() && field.text.last() == '\n') {
          onSearch(field.text.trim())
        }
      }
    })
    field.addKeyListener(object : KeyAdapter() {
      override fun keyPressed(e: KeyEvent) {
        if (e.keyCode == KeyEvent.VK_ENTER) {
          onSearch(field.text.trim())
          e.consume()
        }
      }
    })

    add(field)

    val self = this
    java.awt.EventQueue.invokeLater {
      self.layout = java.awt.BorderLayout()
      val wrap = JPanel()
      wrap.layout = java.awt.BorderLayout()
      wrap.background = AnsunaTheme.surface
      wrap.border = javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0)
      wrap.preferredSize = Dimension(Int.MAX_VALUE, AnsunaTheme.searchHeight)
      wrap.add(field, java.awt.BorderLayout.CENTER)
      self.add(wrap, java.awt.BorderLayout.CENTER)
    }
  }

  override fun requestFocus() { field.requestFocus() }
  fun setQuery(text: String) { field.text = text }
}

