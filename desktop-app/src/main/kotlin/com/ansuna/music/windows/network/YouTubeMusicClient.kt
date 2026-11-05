package com.ansuna.music.window.network

import com.ansuna.music.window.model.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

const val YOUTUBE_MUSIC_BASE = "https://music.youtube.com"
const val INNER_TUBE_API_KEY = "AIzaSyC9XL3ZjWddXya6X74dJoCTL-WEYFDNX3"

/**
 * Minimal YouTube Music InnerTube client.
 *
 * Calls the same youtubei endpoints the Android app uses (search, browse,
 * player) so the Windows port streams from the same catalog. JSON responses
 * are walked to extract track renderers.
 */
class YouTubeMusicClient private constructor() {
  private val http = HttpClient.newBuilder()
    .followRedirects(HttpClient.Redirect.NORMAL)
    .connectTimeout(java.time.Duration.ofSeconds(30))
    .build()

  private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

  private val io: CoroutineDispatcher = Dispatchers.IO

  companion object {
    val instance = YouTubeMusicClient()
  }

  /** Search YouTube Music for tracks matching [query]. */
  suspend fun search(query: String): List<Song> = withContext(io) {
    val body = buildRequestBody(query = query)
    val response = post("$YOUTUBE_MUSIC_BASE/youtubei/v1/search?key=$INNER_TUBE_API_KEY", body)
    parseSearchResponse(response)
  }

  /** Home page / recommended tracks. */
  suspend fun home(): List<Song> = withContext(io) {
    val body = buildRequestBody(browseId = "FEmusic_home")
    val response = post("$YOUTUBE_MUSIC_BASE/youtubei/v1/browse?key=$INNER_TUBE_API_KEY", body)
    parseBrowseResponse(response)
  }

  /** Resolve a playable stream URL for [videoId]. */
  suspend fun player(videoId: String): Song? = withContext(io) {
    val body = buildRequestBody(videoId = videoId)
    val response = post("$YOUTUBE_MUSIC_BASE/youtubei/v1/player?key=$INNER_TUBE_API_KEY", body)
    parsePlayerResponse(response, videoId)
  }

  private suspend fun post(url: String, body: String): JsonElement = withContext(io) {
    val request = HttpRequest.newBuilder()
      .uri(URI.create(url))
      .header("Accept", "application/json")
      .header("Content-Type", "application/json")
      .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
      .header("Origin", YOUTUBE_MUSIC_BASE)
      .header("Referer", "$YOUTUBE_MUSIC_BASE/")
      .POST(BodyPublishers.ofString(body))
      .build()
    http.sendAsync(request, BodyHandlers.ofString()).handle { response, throwable ->
      if (throwable != null) throw throwable
      if (response.statusCode() !in 200..299) {
        throw RuntimeException("YouTube Music request failed: ${response.statusCode()}")
      }
      json.decodeFromString(JsonElement.serializer(), response.body())
    }.toCompletableFuture().get()
  }

  private fun buildRequestBody(
    query: String? = null,
    browseId: String? = null,
    videoId: String? = null
  ): String {
    val context = """
      "context": {
        "client": {
          "clientName": "WEB_REMIX",
          "clientVersion": "1.20251105.01.00"
        }
      }
    """.trimIndent()
    val extra = when {
      query != null -> """, "query": "$query""""
      browseId != null -> """, "browseId": "$browseId""""
      videoId != null -> """, "videoId": "$videoId""""
      else -> ""
    }
    return "{$context$extra}"
  }

  // ---------------- parsing ----------------

  private fun parseSearchResponse(element: JsonElement): List<Song> {
    val songs = mutableListOf<Song>()
    walkRenderers(element) { renderer ->
      val song = songFromRenderer(renderer)
      if (song != null) songs += song
    }
    return songs
  }

  private fun parseBrowseResponse(element: JsonElement): List<Song> {
    val songs = mutableListOf<Song>()
    walkRenderers(element) { renderer ->
      val song = songFromRenderer(renderer)
      if (song != null) songs += song
    }
    return songs
  }

  private fun parsePlayerResponse(element: JsonElement, videoId: String): Song? {
    val root = element as? JsonObject ?: return null
    val streamingData = root.get("streamingData") ?: return null
    val adaptiveFormats = (streamingData as? JsonObject)?.get("adaptiveFormats") ?: return null
    val formats = adaptiveFormats as? JsonArray ?: return null

    var best: Song? = null
    var bestScore = 0
    for (fmt in formats) {
      val fmtObj = fmt as? JsonObject ?: continue
      val mimeType = fmtObj.get("mimeType")?.jsonPrimitive?.content ?: ""
      val width = fmtObj.get("width")
      val height = fmtObj.get("height")
      val isAudio = width == null && height == null
      if (!isAudio) continue
      val url = fmtObj.get("url")?.jsonPrimitive?.content ?: ""
      if (url.isEmpty()) continue
      val title = fmtObj.get("title")?.jsonPrimitive?.content ?: videoId
      val score = if (mimeType.contains("webm")) 2 else if (mimeType.contains("mp4")) 1 else 0
      if (best == null || score > bestScore) {
        best = Song(id = videoId, title = title, artist = "YouTube Music", streamUrl = url)
        bestScore = score
      }
    }
    return best
  }

  /** Walk every renderer object in the JSON tree and call [onRenderer]. */
  private fun walkRenderers(element: JsonElement, onRenderer: (JsonObject) -> Unit) {
    when (element) {
      is JsonObject -> {
        val rendererKey = element.keys.firstOrNull { it.endsWith("Renderer") }
        if (rendererKey != null) {
          val renderer = element.get(rendererKey) as? JsonObject
          if (renderer != null) onRenderer(renderer)
        }
        element.keys.mapNotNull { element.get(it) }.forEach { walkRenderers(it, onRenderer) }
      }
      is JsonArray -> element.forEach { walkRenderers(it, onRenderer) }
      else -> Unit
    }
  }

  /** Extract a Song from a music renderer object. */
  private fun songFromRenderer(r: JsonObject): Song? {
    val title = textFromRuns(r.get("title")) ?: textFromSimpleText(r.get("title")) ?: return null
    val videoId =
      r.get("videoId")?.jsonPrimitive?.content ?: watchEndpointFrom(r.get("navigationEndpoint")) ?: return null
    val artist = textFromRuns(r.get("artist")) ?: textFromRuns(r.get("subtitle")) ?: "Unknown Artist"
    val album = textFromRuns(r.get("album")) ?: ""
    val thumbnail = thumbnailUrlFrom(r.get("thumbnail")) ?: ""
    return Song(id = videoId, title = title, artist = artist, album = album, thumbnailUrl = thumbnail)
  }

  private fun watchEndpointFrom(node: JsonElement?): String? {
    val obj = node as? JsonObject ?: return null
    val watchEndpoint = obj.get("watchEndpoint") as? JsonObject ?: return null
    return watchEndpoint.get("videoId")?.jsonPrimitive?.content
  }

  private fun textFromRuns(node: JsonElement?): String? {
    val runs = (node as? JsonObject)?.get("runs") as? JsonArray ?: return null
    val texts = runs.mapNotNull { (it as? JsonObject)?.get("text")?.jsonPrimitive?.content }
    return texts.joinToString("").takeIf { it.isNotEmpty() }
  }

  private fun textFromSimpleText(node: JsonElement?): String? =
    (node as? JsonObject)?.get("simpleText")?.jsonPrimitive?.content

  private fun thumbnailUrlFrom(node: JsonElement?): String? {
    val thumbnails = (node as? JsonObject)?.get("thumbnails") as? JsonArray ?: return null
    return thumbnails
      .mapNotNull { (it as? JsonObject)?.get("url")?.jsonPrimitive?.content }
      .firstOrNull()
  }
}
