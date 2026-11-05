/**
 * Single track row - matches Android list item design
 */
import { Song, AnsunaTheme } from '../theme';

export class TrackRow {
  private element: HTMLElement;
  private song: Song;
  private onPlay: (song: Song) => void;
  private onSelect: (song: Song) => void;

  constructor(song: Song, onPlay: (song: Song) => void, onSelect?: (song: Song) => void) {
    this.song = song;
    this.onPlay = onPlay;
    this.onSelect = onSelect ?? onPlay;
    this.element = this.createElement();
  }

  private createElement(): HTMLElement {
    const row = document.createElement('div');
    row.className = 'track-row';
    row.style.display = 'flex';
    row.style.alignItems = 'center';
    row.style.gap = '16px';
    row.style.padding = '12px 16px';
    row.style.background = AnsunaTheme.colors.surface;
    row.style.borderBottom = `1px solid ${AnsunaTheme.colors.divider}`;
    row.style.cursor = 'pointer';
    row.style.transition = 'background 0.2s';

    row.addEventListener('mouseenter', () => {
      row.style.background = AnsunaTheme.colors.surfaceHover;
    });
    row.addEventListener('mouseleave', () => {
      row.style.background = AnsunaTheme.colors.surface;
    });

    const info = document.createElement('div');
    info.className = 'track-info';
    info.style.flex = '1';
    info.style.minWidth = '0';

    const title = document.createElement('div');
    title.className = 'track-title';
    title.textContent = this.song.title;
    title.style.fontSize = '14px';
    title.style.fontWeight = '600';
    title.style.color = AnsunaTheme.colors.textOnSurface;
    title.style.whiteSpace = 'nowrap';
    title.style.overflow = 'hidden';
    title.style.textOverflow = 'ellipsis';

    const subtitle = document.createElement('div');
    subtitle.className = 'track-subtitle';
    const subtitleText = this.song.album
      ? `${this.song.artist} · ${this.song.album}`
      : this.song.artist;
    subtitle.textContent = subtitleText;
    subtitle.style.fontSize = '12px';
    subtitle.style.color = AnsunaTheme.colors.textMuted;
    subtitle.style.whiteSpace = 'nowrap';
    subtitle.style.overflow = 'hidden';
    subtitle.style.textOverflow = 'ellipsis';
    subtitle.style.marginTop = '2px';

    info.appendChild(title);
    info.appendChild(subtitle);

    const playBtn = document.createElement('button');
    playBtn.className = 'track-play-btn';
    playBtn.textContent = '▶';
    playBtn.style.background = AnsunaTheme.colors.accent;
    playBtn.style.color = 'white';
    playBtn.style.border = 'none';
    playBtn.style.borderRadius = '20px';
    playBtn.style.width = '32px';
    playBtn.style.height = '32px';
    playBtn.style.cursor = 'pointer';
    playBtn.style.fontSize = '14px';
    playBtn.style.display = 'flex';
    playBtn.style.alignItems = 'center';
    playBtn.style.justifyContent = 'center';
    playBtn.style.flexShrink = '0';

    playBtn.addEventListener('click', (e) => {
      e.stopPropagation();
      this.onPlay(this.song);
    });

    row.addEventListener('click', () => {
      this.onSelect(this.song);
    });

    row.appendChild(info);
    row.appendChild(playBtn);

    return row;
  }

  getElement(): HTMLElement {
    return this.element;
  }

  getSong(): Song {
    return this.song;
  }
}
