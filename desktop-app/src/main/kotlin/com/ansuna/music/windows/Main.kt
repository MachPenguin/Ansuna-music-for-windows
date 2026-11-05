package com.ansuna.music.window

import javax.swing.SwingUtilities
import javax.swing.UIManager

/**
 * Entry point for Ansuna Music on Windows (Swing).
 *
 * Launches the native desktop UI on the EDT. Swing is part of the JDK
 * (java.desktop), so no external GUI toolkit is required.
 */
fun main() = SwingUtilities.invokeLater {
  try {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
  } catch (_: Exception) {
    // fall back to the default look and feel
  }
  MainWindow().isVisible = true
}
