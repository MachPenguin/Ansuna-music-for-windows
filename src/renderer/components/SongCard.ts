/**
 * Song card - thumbnail + title/artist on translucent surface with 24dp corners
 */
import { Song, AnsunaTheme } from '../theme';

export class SongCard {
  private element: HTMLElement;
  private song: Song;
  private onSelect: (song: Song) => void;

  constructor(song: Song, onSelect: (song: Song) => void) {
    this.song = song;
    this.onSelect = onSelect;
    this.element = this.createElement();
  }

  private createElement(): HTMLElement {
    const card = document.createElement('div');
    card.className = 'song-card';
    card.style.background = AnsunaTheme.colors.cardSurface;
    card.style.borderRadius = `${AnsunaTheme.radius}px`;
    card.style.overflow = 'hidden';
    card.style.cursor = 'pointer';
    card.style.transition = 'background 0.2s, transform 0.2s';
    card.style.height = '280px';
    card.style.display = 'flex';
    card.style.flexDirection = 'column';

    card.addEventListener('mouseenter', () => {
      card.style.background = AnsunaTheme.colors.surfaceHover;
      card.style.transform = 'translateY(-4px)';
    });
    card.addEventListener('mouseleave', () => {
      card.style.background = AnsunaTheme.colors.cardSurface;
      card.style.transform = 'translateY(0)';
    });

    // Thumbnail
    const thumb = document.createElement('div');
    thumb.className = 'card-thumbnail';
    thumb.style.width = '100%';
    thumb.style.height = '160px';
    thumb.style.background = AnsunaTheme.colors.surfaceElevated;
    thumb.style.borderRadius = '16px';
    thumb.style.position = 'relative';
    thumb.style.overflow = 'hidden';

    const thumbImg = document.createElement('img');
    thumbImg.className = 'card-thumbnail-img';
    thumbImg.src = this.song.thumbnailUrl || '';
    thumbImg.alt = this.song.title;
    thumbImg.style.width = '100%';
    thumbImg.style.height = '100%';
    thumbImg.style.objectFit = 'cover';
    thumbImg.onerror = () => {
      thumbImg.style.display = 'none';
      // Show a colored placeholder
      thumb.style.display = 'flex';
      thumb.style.alignItems = 'center';
      thumb.style.justifyContent = 'center';
      thumb.style.fontSize = '48px';
      thumb.textContent = '🎵';
    };

    thumb.appendChild(thumbImg);

    // Info
    const info = document.createElement('div');
    info.className = 'card-info';
    info.style.padding = '12px 16px 16px 16px';
    info.style.display = 'flex';
    info.style.flexDirection = 'column';
    info.style.gap = '6px';

    const title = document.createElement('div');
    title.className = 'card-title';
    title.textContent = this.song.title;
    title.style.fontSize = '14px';
    title.style.fontWeight = '600';
    title.style.color = AnsunaTheme.colors.textOnSurface;
    title.style.whiteSpace = 'nowrap';
    title.style.overflow = 'hidden';
    title.style.textOverflow = 'ellipsis';

    const artist = document.createElement('div');
    artist.className = 'card-artist';
    artist.textContent = this.song.artist;
    artist.style.fontSize = '12px';
    artist.style.color = AnsunaTheme.colors.textMuted;
    artist.style.whiteSpace = 'nowrap';
    artist.style.overflow = 'hidden';
    artist.style.textOverflow = 'ellipsis';

    info.appendChild(title);
    info.appendChild(artist);

    card.appendChild(thumb);
    card.appendChild(info);

    card.addEventListener('click', () => {
      this.onSelect(this.song);
    });

    return card;
  }

  getElement(): HTMLElement {
    return this.element;
  }

  getSong(): Song {
    return this.song;
  }
}
