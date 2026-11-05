/**
 * Top search bar - mirrors Android TopSearch / SearchBarInputField
 */
import { AnsunaTheme } from '../theme';

export class SearchBar {
  private element: HTMLElement;
  private input: HTMLInputElement;
  private onSearch: (query: string) => void;

  constructor(onSearch: (query: string) => void) {
    this.onSearch = onSearch;
    this.element = this.createElement();
    this.input = this.element.querySelector('.search-input')!;
  }

  private createElement(): HTMLElement {
    const container = document.createElement('div');
    container.className = 'search-bar';
    container.style.position = 'sticky';
    container.style.top = '0';
    container.style.background = AnsunaTheme.colors.background;
    container.style.padding = '16px 24px 8px 24px';
    container.style.zIndex = '10';
    container.style.display = 'flex';
    container.style.justifyContent = 'center';

    const wrapper = document.createElement('div');
    wrapper.className = 'search-wrapper';
    wrapper.style.background = AnsunaTheme.colors.surface;
    wrapper.style.borderRadius = `${AnsunaTheme.radiusSm}px`;
    wrapper.style.height = `${AnsunaTheme.searchHeight}px`;
    wrapper.style.display = 'flex';
    wrapper.style.alignItems = 'center';
    wrapper.style.overflow = 'hidden';
    wrapper.style.maxWidth = '720px';
    wrapper.style.width = '100%';
    wrapper.style.border = '1px solid transparent';

    const input = document.createElement('input');
    input.type = 'text';
    input.className = 'search-input';
    input.placeholder = 'Search artists, songs, albums…';
    input.style.flex = '1';
    input.style.background = 'transparent';
    input.style.border = 'none';
    input.style.color = AnsunaTheme.colors.textOnSurface;
    input.style.fontSize = '15px';
    input.style.padding = '0 16px';
    input.style.outline = 'none';

    const btn = document.createElement('button');
    btn.className = 'search-btn';
    btn.textContent = '🔍';
    btn.style.background = AnsunaTheme.colors.accent;
    btn.style.border = 'none';
    btn.style.color = 'white';
    btn.style.width = '48px';
    btn.style.height = '100%';
    btn.style.cursor = 'pointer';
    btn.style.fontSize = '16px';
    btn.style.display = 'flex';
    btn.style.alignItems = 'center';
    btn.style.justifyContent = 'center';

    input.addEventListener('keypress', (e) => {
      if (e.key === 'Enter') {
        this.onSearch(input.value.trim());
      }
    });
    btn.addEventListener('click', () => {
      this.onSearch(input.value.trim());
    });

    wrapper.appendChild(input);
    wrapper.appendChild(btn);
    container.appendChild(wrapper);

    return container;
  }

  focus(): void {
    this.input.focus();
  }

  getElement(): HTMLElement {
    return this.element;
  }
}
