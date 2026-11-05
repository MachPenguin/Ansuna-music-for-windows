import axios from 'axios';

const YOUTUBE_MUSIC_BASE = 'https://music.youtube.com';
const INNER_TUBE_API_KEY = 'AIzaSyC9XL3ZjWddXya6X74dJoCTL-WEYFDNX3';

const http = axios.create({
  baseURL: YOUTUBE_MUSIC_BASE,
  headers: {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36',
    'Origin': YOUTUBE_MUSIC_BASE,
    'Referer': YOUTUBE_MUSIC_BASE + '/',
    'Accept-Language': 'en-US,en;q=0.9',
  },
  timeout: 30000,
});

interface SearchResult {
  id: string;
  title: string;
  artist: string;
  album: string;
  thumbnailUrl: string;
  durationMs: number;
}

interface Song extends SearchResult {
  streamUrl?: string;
}

interface RequestBodyParams {
  query?: string;
  browseId?: string;
  videoId?: string;
}

function buildRequestBody(params: RequestBodyParams): string {
  const context = `{
    "context": {
      "client": {
        "clientName": "WEB_REMIX",
        "clientVersion": "1.20251105.01.00"
      }
    }
  }`;

  const { query, browseId, videoId } = params;
  const extra = query
    ? `, "query": "${query}"`
    : browseId
    ? `, "browseId": "${browseId}"`
    : videoId
    ? `, "videoId": "${videoId}"`
    : '';

  return `{${context.slice(0, -1)}${extra}}`;
}

function extractText(node: any): string | null {
  if (!node) return null;
  if (node.simpleText) return node.simpleText;
  if (node.runs && Array.isArray(node.runs)) {
    return node.runs
      .map((r: any) => r.text || '')
      .join('');
  }
  return null;
}

function getThumbnail(node: any): string {
  if (!node) return '';
  const thumbnails = node.thumbnails;
  if (thumbnails && Array.isArray(thumbnails) && thumbnails.length > 0) {
    return thumbnails[thumbnails.length - 1].url || '';
  }
  return '';
}

function walkRenderers(element: any, onRenderer: (r: any) => void): void {
  if (!element) return;
  if (Array.isArray(element)) {
    element.forEach((item) => walkRenderers(item, onRenderer));
    return;
  }
  if (typeof element === 'object') {
    const rendererKey = Object.keys(element).find((k) => k.endsWith('Renderer'));
    if (rendererKey) {
      const renderer = element[rendererKey];
      if (renderer && typeof renderer === 'object') {
        onRenderer(renderer);
      }
    }
    Object.values(element).forEach((v) => walkRenderers(v, onRenderer));
  }
}

function songFromRenderer(r: any): SearchResult | null {
  const title = extractText(r.title);
  const videoId = r.videoId || (r.navigationEndpoint?.watchEndpoint?.videoId) || null;
  if (!title || !videoId) return null;

  const artist =
    extractText(r.artist) ||
    extractText(r.subtitle) ||
    'Unknown Artist';
  const album = extractText(r.album) || '';
  const thumbnail = getThumbnail(r.thumbnail);

  return {
    id: videoId,
    title,
    artist,
    album,
    thumbnailUrl: thumbnail,
    durationMs: 0,
  };
}

function parseSearchResponse(element: any): SearchResult[] {
  const songs: SearchResult[] = [];
  walkRenderers(element, (renderer) => {
    const song = songFromRenderer(renderer);
    if (song) songs.push(song);
  });
  return songs;
}

export class YouTubeMusicAPI {
  async search(query: string): Promise<SearchResult[]> {
    const body = buildRequestBody({ query });
    const response = await http.post(
      `/youtubei/v1/search?key=${INNER_TUBE_API_KEY}`,
      body
    );
    return parseSearchResponse(response.data);
  }

  async home(): Promise<SearchResult[]> {
    const body = buildRequestBody({ browseId: 'FEmusic_home' });
    const response = await http.post(
      `/youtubei/v1/browse?key=${INNER_TUBE_API_KEY}`,
      body
    );
    return parseSearchResponse(response.data);
  }

  async player(videoId: string): Promise<Song | null> {
    const body = buildRequestBody({ videoId });
    const response = await http.post(
      `/youtubei/v1/player?key=${INNER_TUBE_API_KEY}`,
      body
    );

    const streamingData = response.data.streamingData;
    if (!streamingData) return null;

    const adaptiveFormats = streamingData.adaptiveFormats;
    if (!adaptiveFormats || !Array.isArray(adaptiveFormats)) return null;

    let best: Song | null = null;
    let bestScore = 0;

    for (const fmt of adaptiveFormats) {
      const mimeType = fmt.mimeType || '';
      const width = fmt.width;
      const height = fmt.height;
      const isAudio = width == null && height == null;
      if (!isAudio) continue;

      const url = fmt.url || '';
      if (!url) continue;

      const title = fmt.title || videoId;
      const score = mimeType.includes('webm') ? 2 : mimeType.includes('mp4') ? 1 : 0;

      if (!best || score > bestScore) {
        best = {
          id: videoId,
          title,
          artist: 'YouTube Music',
          album: '',
          thumbnailUrl: '',
          durationMs: 0,
          streamUrl: url,
        };
        bestScore = score;
      }
    }

    return best;
  }
}
