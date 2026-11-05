/**
 * Docked bottom mini-player - now playing bar
 */
import { Song, AnsunaTheme } from '../theme';
import { AudioPlayer } from '../services/audioPlayer';

export class MiniPlayer {
  private element: HTMLElement;
  private titleEl: HTMLElement;
  private artistEl: HTMLElement;
  private playBtn: HTMLElement;
  private artworkEl: HTMLElement;
  private audioPlayer: AudioPlayer;
  private currentSong: Song | null = null;

  constructor(audioPlayer: AudioPlayer) {
    this.audioPlayer = audioPlayer;
    this.element = this.createElement();
    this.titleEl = this.element.querySelector('#mini-title')!;
    this.artistEl = this.element.querySelector('#mini-artist')!;
    this.playBtn = this.element.querySelector('#mini-play-pause')!;
    this.artworkEl = this.element.querySelector('#mini-artwork')!;

    this.playBtn.addEventListener('click', () => {
      this.audioPlayer.toggle();
      this.updatePlayPause();
    });

    this.element.addEventListener('click', () => {
      // Could open full player
    });

    // Listen for audio state changes
    setInterval(() => this.updatePlayPause(), 1000);
  }

  private createElement(): HTMLElement {
    const container = document.createElement('div');
    container.id = 'mini-player';
    container.style.height = `${AnsunaTheme.miniPlayerHeight}px`;
    container.style.background = AnsunaTheme.colors.surfaceElevated;
    container.style.borderTop = `1px solid ${AnsunaTheme.colors.divider}`;
    container.style.display = 'flex';
    container.style.alignItems = 'center';
    container.style.padding = '0 20px';
    container.style.flexShrink = '0';
    container.style.zIndex = '100';

    const inner = document.createElement('div');
    inner.id = 'mini-player-inner';
    inner.style.display = 'flex';
    inner.style.alignItems = 'center';
    inner.style.gap = '16px';
    inner.style.width = '100%';

    const artwork = document.createElement('div');
    artwork.id = 'mini-artwork';
    artwork.style.width = '44px';
    artwork.style.height = '44px';
    artwork.style.background = AnsunaTheme.colors.surface;
    artwork.style.borderRadius = '10px';
    artwork.style.flexShrink = '0';

    const info = document.createElement('div');
    info.id = 'mini-info';
    info.style.flex = '1';
    info.style.minWidth = '0';

    const title = document.createElement('div');
    title.id = 'mini-title';
    title.textContent = 'No track playing';
    title.style.fontSize = '13px';
    title.style.fontWeight = '600';
    title.style.color = AnsunaTheme.colors.textOnSurface;
    title.style.whiteSpace = 'nowrap';
    title.style.overflow = 'hidden';
    title.style.textOverflow = 'ellipsis';

    const artist = document.createElement('div');
    artist.id = 'mini-artist';
    artist.textContent = '';
    artist.style.fontSize = '11px';
    artist.style.color = AnsunaTheme.colors.textMuted;
    artist.style.whiteSpace = 'nowrap';
    artist.style.overflow = 'hidden';
    artist.style.textOverflow = 'ellipsis';

    info.appendChild(title);
    info.appendChild(artist);

    const controls = document.createElement('div');
    controls.id = 'mini-controls';
    controls.style.display = 'flex';
    controls.style.alignItems = 'center';
    controls.style.gap = '8px';

    const playBtn = document.createElement('button');
    playBtn.id = 'mini-play-pause';
    playBtn.className = 'icon-btn';
    playBtn.textContent = '▶';
    playBtn.style.background = AnsunaTheme.colors.accent;
    playBtn.style.color = 'white';
    playBtn.style.border = 'none';
    playBtn.style.borderRadius = '50%';
    playBtn.style.width = '36px';
    playBtn.style.height = '36px';
    playBtn.style.cursor = 'pointer';
    playBtn.style.fontSize = '16px';
    playBtn.style.display = 'flex';
    playBtn.style.alignItems = 'center';
    playBtn.style.justifyContent = 'center';

    controls.appendChild(playBtn);

    inner.appendChild(artwork);
    inner.appendChild(info);
    inner.appendChild(controls);
    container.appendChild(inner);

    return container;
  }

  setSong(song: Song | null): void {
    this.currentSong = song;
    if (song) {
      this.titleEl.textContent = song.title;
      this.artistEl.textContent = song.artist || '';
      if (song.thumbnailUrl) {
        (this.artworkEl as HTMLDivElement).style.backgroundImage = `url(${song.thumbnailUrl})`;
        (this.artworkEl as HTMLDivElement).style.backgroundSize = 'cover';
        (this.artworkEl as HTMLDivElement).style.backgroundPosition = 'center';
        (this.artworkEl as HTMLDivElement).textContent = '';
      } else {
        this.artworkEl.style.backgroundImage = 'none';
        this.artworkEl.style.backgroundColor = AnsunaTheme.colors.surface;
        this.artworkEl.textContent = '🎵';
        this.artworkEl.style.display = 'flex';
        this.artworkEl.style.alignItems = 'center';
        this.artworkEl.style.justifyContent = 'center';
        this.artworkEl.style.fontSize = '20px';
      }
    } else {
      this.titleEl.textContent = 'No track playing';
      this.artistEl.textContent = '';
      this.artworkEl.style.backgroundImage = 'none';
      this.artworkEl.style.backgroundColor = AnsunaTheme.colors.surface;
      this.artworkEl.textContent = '🎵';
      this.artworkEl.style.display = 'flex';
      this.artworkEl.style.alignItems = 'center';
      this.artworkEl.style.justifyContent = 'center';
      this.artworkEl.style.fontSize = '20px';
    }
    this.updatePlayPause();
  }

  updatePlayPause(): void {
    const isPlaying = this.audioPlayer.isPlaying;
    this.playBtn.textContent = isPlaying ? '⏸' : '▶';
    this.playBtn.style.background = AnsunaTheme.colors.accent;
    this.playBtn.style.color = 'white';
  }

  getElement(): HTMLElement {
    return this.element;
  }
}
