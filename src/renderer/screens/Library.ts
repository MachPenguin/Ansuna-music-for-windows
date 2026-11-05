/**
 * Library screen - placeholder for local media / playlists
 */
import { AnsunaTheme } from '../theme';

export class LibraryScreen {
  private element: HTMLElement;

  constructor() {
    this.element = document.createElement('div');
    this.element.style.flex = '1';
    this.element.style.overflowY = 'auto';
    this.element.style.padding = '24px';

    const container = document.createElement('div');
    container.style.display = 'flex';
    container.style.flexDirection = 'column';
    container.style.gap = '16px';

    const header = document.createElement('h1');
    header.textContent = 'Library';
    header.style.fontSize = '24px';
    header.style.fontWeight = '600';
    header.style.color = AnsunaTheme.colors.textOnSurface;
    header.style.letterSpacing = '-0.5px';

    const subtitle = document.createElement('p');
    subtitle.textContent = 'Your playlists, albums, and artists appear here.';
    subtitle.style.color = AnsunaTheme.colors.textMuted;
    subtitle.style.fontSize = '14px';

    const empty = document.createElement('div');
    empty.className = 'empty-state';
    empty.style.display = 'flex';
    empty.style.flexDirection = 'column';
    empty.style.alignItems = 'center';
    empty.style.justifyContent = 'center';
    empty.style.height = '250px';
    empty.style.color = AnsunaTheme.colors.textMuted;
    empty.style.textAlign = 'center';

    const h2 = document.createElement('h2');
    h2.textContent = 'Your Library';
    h2.style.fontSize = '20px';
    h2.style.marginBottom = '8px';
    h2.style.color = AnsunaTheme.colors.textOnSurface;
    h2.style.fontWeight = '500';

    const p = document.createElement('p');
    p.textContent = 'Saved songs and playlists will appear here.';
    p.style.fontSize = '14px';

    empty.appendChild(h2);
    empty.appendChild(p);

    container.appendChild(header);
    container.appendChild(subtitle);
    container.appendChild(empty);
    this.element.appendChild(container);
  }

  getElement(): HTMLElement {
    return this.element;
  }
}
