package com.ansuna.music.window.player

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine
import com.ansuna.music.window.model.Song
import javax.sound.sampled.FloatControl
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener
import javax.sound.sampled.UnsupportedAudioFileException
import java.io.InputStream
import java.net.URL
import kotlin.concurrent.thread

/**
 * Simple audio player backed by JDK [javax.sound.sampled.Clip].
 *
 * Plays a stream URL resolved from YouTube Music's player response. Suitable
 * for previewing tracks; for long-form streaming, a buffered PCM decoder can
 * be layered on top of the same Clip API.
 */
class AudioPlayer {
  private var clip: Clip? = null
  private var currentSongId: String? = null

  /** Whether a track is currently playing. */
  val isPlaying: Boolean
    get() = clip?.isRunning ?: false

  /** The id of the track currently loaded/playing. */
  val currentTrackId: String?
    get() = currentSongId

  private var onTrackEnd: (() -> Unit)? = null

  /**
   * Play [song] using its [streamUrl]. Replaces any currently playing track.
   */
  fun play(song: Song, onEnd: (() -> Unit)? = null) {
    stop()
    currentSongId = song.id
    onTrackEnd = onEnd

    val url = song.streamUrl ?: run {
      throw IllegalStateException("No stream URL for ${song.title}")
    }

    thread(isDaemon = true, name = "ansuna-audio-${song.id}") {
      try {
        val audioURL = URL(url)
        val audioStream = AudioSystem.getAudioInputStream(audioURL)
        val format = audioStream.format
        val info = DataLine.Info(Clip::class.java, format)
        clip = AudioSystem.getLine(info) as Clip
        clip!!.addLineListener(object : LineListener {
          override fun update(event: LineEvent) {
            if (event.type == LineEvent.Type.STOP && clip?.isOpen == true) {
              clip!!.close()
              currentSongId = null
              onTrackEnd?.invoke()
            }
          }
        })
        clip!!.open(audioStream)
        clip!!.start()
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  fun pause() {
    clip?.stop()
  }

  fun resume() {
    clip?.start()
  }

  fun stop() {
    clip?.let {
      it.stop()
      it.close()
    }
    clip = null
    currentSongId = null
  }

  fun seekTo(frame: Long) {
    clip?.setMicrosecondPosition(frame * 1000)
  }

  fun volume(percent: Float) {
    val gain = (percent / 100f).coerceIn(0f, 1f)
    clip?.let { c ->
      if (c.isControlSupported(javax.sound.sampled.FloatControl.Type.MASTER_GAIN)) {
        val control = c.getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN) as FloatControl
        val db = (20 * Math.log10(gain.toDouble())).toFloat()
        control.value = db.coerceIn(control.minimum, control.maximum)
      }
    }
  }

  fun close() {
    stop()
  }
}
