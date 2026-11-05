package com.ansuna.music.window.model

import kotlinx.serialization.Serializable

/**
 * A playable track. Mirrors the essential fields Ansuna surfaces from
 * YouTube Music so the Windows port keeps the same abilities: browse,
 * search, play, and library management.
 */
@Serializable
data class Song(
  /** YouTube videoId / track identifier. */
  val id: String,
  val title: String,
  val artist: String,
  val album: String = "",
  /** URL to the track artwork (may be empty). */
  val thumbnailUrl: String = "",
  /** Duration in milliseconds. */
  val durationMs: Long = 0,
  /** Direct stream URL resolved from YouTube's player response (short-lived). */
  val streamUrl: String? = null,
  /** Whether the user has liked this track. */
  val isLiked: Boolean = false
) {
  fun displayDuration(): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return if (durationMs > 0) "%d:%02d".format(minutes, seconds) else ""
  }
}
