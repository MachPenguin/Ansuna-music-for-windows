import { TrackRow } from '../components/TrackRow';
import { SearchBar } from '../components/SearchBar';
import { AnsunaTheme } from '../theme';
export class SearchScreen {
    element;
    contentContainer;
    searchBar;
    statusEl;
    onSongSelect;
    onSongPlay;
    constructor(onSongSelect, onSongPlay) {
        this.onSongSelect = onSongSelect;
        this.onSongPlay = onSongPlay;
        this.element = document.createElement('div');
        this.element.style.flex = '1';
        this.element.style.overflowY = 'auto';
        this.element.style.padding = '0 24px 24px 24px';
        this.searchBar = new SearchBar((query) => {
            if (query)
                this.performSearch(query);
        });
        this.contentContainer = document.createElement('div');
        this.contentContainer.style.display = 'flex';
        this.contentContainer.style.flexDirection = 'column';
        this.contentContainer.style.gap = '16px';
        this.statusEl = document.createElement('div');
        this.statusEl.style.alignmentBaseline = 'center';
        this.statusEl.style.color = AnsunaTheme.colors.textMuted;
        this.statusEl.style.marginBottom = '8px';
        this.element.appendChild(this.searchBar.getElement());
        this.element.appendChild(this.statusEl);
        this.element.appendChild(this.contentContainer);
        this.renderEmpty();
    }
    performSearch(query) {
        this.statusEl.textContent = `Searching for "${query}"…`;
        this.contentContainer.innerHTML = '';
        if (window.ansuna?.yt?.search) {
            window.ansuna.yt.search(query).then((songs) => {
                this.renderResults(songs, query);
            }).catch((err) => {
                console.error('Search failed:', err);
                this.statusEl.textContent = 'Search failed. Try again.';
                this.renderEmpty();
            });
        }
        else {
            this.statusEl.textContent = 'API not available.';
            this.renderEmpty();
        }
    }
    renderResults(songs, query) {
        this.statusEl.textContent = songs.length === 0
            ? `No results for "${query}".`
            : `${query} — ${songs.length} result(s)`;
        this.statusEl.style.marginBottom = '8px';
        this.contentContainer.innerHTML = '';
        const trackList = document.createElement('div');
        trackList.className = 'track-list';
        songs.forEach((song) => {
            const row = new TrackRow(song, (s) => this.onSongPlay(s), (s) => this.onSongSelect(s));
            trackList.appendChild(row.getElement());
        });
        this.contentContainer.appendChild(trackList);
    }
    renderEmpty() {
        this.contentContainer.innerHTML = '';
        this.statusEl.textContent = 'Search for music...';
    }
    focusSearch() {
        this.searchBar.focus();
    }
    getElement() {
        return this.element;
    }
}
