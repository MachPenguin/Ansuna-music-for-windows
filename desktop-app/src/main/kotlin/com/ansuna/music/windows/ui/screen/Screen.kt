package com.ansuna.music.window.ui.screen

import javax.swing.JComponent

/** A navigable screen in Ansuna Music for Windows. */
interface Screen {
  /** The display name shown in the nav. */
  val title: String

  /** The root Swing component for this screen. */
  val component: JComponent

  /** Called when the screen becomes visible. */
  fun onShow() = Unit

  /** Called when the screen is hidden. */
  fun onHide() = Unit
}
