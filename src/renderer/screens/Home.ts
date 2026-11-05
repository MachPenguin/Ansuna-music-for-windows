/**
 * Home screen - recommendations grid from YouTube Music home page
 */
import { Song } from '../theme';
import { SongCard } from '../components/SongCard';
import { TrackRow } from '../components/TrackRow';
import { SearchBar } from '../components/SearchBar';
import { AnsunaTheme } from '../theme';

export class HomeScreen {
  private element: HTMLElement;
  private contentContainer: HTMLElement;
  private searchBar: SearchBar;
  private onSongSelect: (song: Song) => void;
  private onSongPlay: (song: Song) => void;

  constructor(onSongSelect: (song: Song) => void, onSongPlay: (song: Song) => void) {
    this.onSongSelect = onSongSelect;
    this.onSongPlay = onSongPlay;
    this.element = document.createElement('div');
    this.element.style.flex = '1';
    this.element.style.overflowY = 'auto';
    this.element.style.padding = '0 24px 24px 24px';

    this.searchBar = new SearchBar((query) => {
      // Home screen delegates search to parent
      (window as any).__ansunaOnSearch?.(query);
    });

    this.contentContainer = document.createElement('div');
    this.contentContainer.style.display = 'flex';
    this.contentContainer.style.flexDirection = 'column';
    this.contentContainer.style.gap = '24px';

    this.element.appendChild(this.searchBar.getElement());
    this.element.appendChild(this.contentContainer);

    this.renderEmpty();
  }

  renderEmpty(): void {
    this.contentContainer.innerHTML = '';
    const header = this.createHeader('Recommendations');
    const empty = this.createEmptyState('No recommendations loaded.', 'Use Search to find music.');
    this.contentContainer.appendChild(header);
    this.contentContainer.appendChild(empty);
  }

  renderSongs(songs: Song[]): void {
    this.contentContainer.innerHTML = '';
    const header = this.createHeader('Recommendations');
    this.contentContainer.appendChild(header);

    const cardsGrid = document.createElement('div');
    cardsGrid.className = 'cards-grid';
    cardsGrid.style.display = 'grid';
    cardsGrid.style.gridTemplateColumns = 'repeat(auto-fill, 200px)';
    cardsGrid.style.gap = '20px';

    songs.forEach((song) => {
      const card = new SongCard(song, (s) => {
        this.onSongSelect(s);
        this.onSongPlay(s);
      });
      cardsGrid.appendChild(card.getElement());
    });

    this.contentContainer.appendChild(cardsGrid);
  }

  renderTrackRows(songs: Song[]): void {
    this.contentContainer.innerHTML = '';
    const header = this.createHeader('Recommendations');
    this.contentContainer.appendChild(header);

    const trackList = document.createElement('div');
    trackList.className = 'track-list';

    songs.forEach((song) => {
      const row = new TrackRow(song, (s) => this.onSongPlay(s), (s) => this.onSongSelect(s));
      trackList.appendChild(row.getElement());
    });

    this.contentContainer.appendChild(trackList);
  }

  private createHeader(title: string): HTMLElement {
    const header = document.createElement('div');
    header.className = 'screen-header';
    header.style.marginBottom = '24px';
    const titleEl = document.createElement('h1');
    titleEl.className = 'screen-title';
    titleEl.textContent = title;
    titleEl.style.fontSize = '24px';
    titleEl.style.fontWeight = '600';
    titleEl.style.color = AnsunaTheme.colors.textOnSurface;
    titleEl.style.letterSpacing = '-0.5px';
    header.appendChild(titleEl);
    return header;
  }

  private createEmptyState(main: string, sub: string): HTMLElement {
    const empty = document.createElement('div');
    empty.className = 'empty-state';
    empty.style.display = 'flex';
    empty.style.flexDirection = 'column';
    empty.style.alignItems = 'center';
    empty.style.justifyContent = 'center';
    empty.style.height = '300px';
    empty.style.color = AnsunaTheme.colors.textMuted;
    empty.style.textAlign = 'center';

    const h2 = document.createElement('h2');
    h2.textContent = main;
    h2.style.fontSize = '20px';
    h2.style.marginBottom = '8px';
    h2.style.color = AnsunaTheme.colors.textOnSurface;
    h2.style.fontWeight = '500';

    const p = document.createElement('p');
    p.textContent = sub;
    p.style.fontSize = '14px';

    empty.appendChild(h2);
    empty.appendChild(p);
    return empty;
  }

  getElement(): HTMLElement {
    return this.element;
  }

  getSearchBar(): SearchBar {
    return this.searchBar;
  }
}
