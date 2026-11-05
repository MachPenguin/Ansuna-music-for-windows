package com.ansuna.music.window.player

import com.ansuna.music.window.model.Song
import com.ansuna.music.window.network.YouTubeMusicClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Playback state and controls shared across the app.
 *
 * Holds the current [AudioPlayer], the [nowPlaying] song, and resolves
 * stream URLs from YouTube Music on demand.
 */
class PlayerViewModel {
  val player = AudioPlayer()
  val scope = CoroutineScope(Dispatchers.Default)

  /** The song currently loaded or playing. */
  var nowPlaying: Song? = null
    private set

  /** Whether something is currently playing. */
  val isPlaying: Boolean
    get() = player.isPlaying

  /**
   * Load and play [song]. Resolves its stream URL from YouTube Music first if
   * the song has no [streamUrl].
   */
  fun play(song: Song, onEnd: (() -> Unit)? = null) {
    scope.launch {
      val songToPlay = if (song.streamUrl != null) song else resolve(song)
      if (songToPlay != null) {
        nowPlaying = songToPlay
        player.play(songToPlay, onEnd = { onEnd?.invoke(); nowPlaying = null })
      }
    }
  }

  /** Pause playback. */
  fun pause() {
    player.pause()
  }

  /** Resume playback. */
  fun resume() {
    player.resume()
  }

  /** Stop playback. */
  fun stop() {
    player.stop()
    nowPlaying = null
  }

  /** Toggle play/pause for the current track. */
  fun toggle() {
    if (player.isPlaying) pause() else resume()
  }

  private suspend fun resolve(song: Song): Song? {
    return withContext(Dispatchers.IO) {
      YouTubeMusicClient.instance.player(song.id)
    }
  }

  fun close() {
    player.close()
  }
}
