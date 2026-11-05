import { AnsunaTheme } from '../theme';
export class PlayerScreen {
    element;
    audioPlayer;
    currentSong = null;
    constructor(audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.element = document.createElement('div');
        this.element.style.flex = '1';
        this.element.style.overflowY = 'auto';
        this.element.style.padding = '40px 24px';
        this.element.style.display = 'flex';
        this.element.style.flexDirection = 'column';
        this.element.style.alignItems = 'center';
        this.renderEmpty();
    }
    setSong(song) {
        this.currentSong = song;
        this.element.innerHTML = '';
        if (!song) {
            this.renderEmpty();
            return;
        }
        // Artwork
        const artwork = document.createElement('div');
        artwork.style.width = '260px';
        artwork.style.height = '260px';
        artwork.style.background = AnsunaTheme.colors.surfaceElevated;
        artwork.style.borderRadius = '24px';
        artwork.style.marginBottom = '24px';
        artwork.style.position = 'relative';
        artwork.style.overflow = 'hidden';
        if (song.thumbnailUrl) {
            artwork.style.backgroundImage = `url(${song.thumbnailUrl})`;
            artwork.style.backgroundSize = 'cover';
            artwork.style.backgroundPosition = 'center';
        }
        else {
            artwork.style.display = 'flex';
            artwork.style.alignItems = 'center';
            artwork.style.justifyContent = 'center';
            artwork.style.fontSize = '80px';
            artwork.textContent = '🎵';
        }
        this.element.appendChild(artwork);
        // Title
        const title = document.createElement('h1');
        title.textContent = song.title;
        title.style.fontSize = '32px';
        title.style.fontWeight = '600';
        title.style.color = AnsunaTheme.colors.textOnSurface;
        title.style.marginBottom = '8px';
        this.element.appendChild(title);
        // Artist
        const artist = document.createElement('div');
        artist.textContent = song.artist || 'Unknown Artist';
        artist.style.fontSize = '18px';
        artist.style.color = AnsunaTheme.colors.textMuted;
        artist.style.marginBottom = '32px';
        this.element.appendChild(artist);
        // Controls
        const controls = document.createElement('div');
        controls.style.display = 'flex';
        controls.style.alignItems = 'center';
        controls.style.gap = '20px';
        controls.style.marginBottom = '40px';
        const prevBtn = document.createElement('button');
        prevBtn.textContent = '⏮';
        prevBtn.style.background = 'transparent';
        prevBtn.style.border = 'none';
        prevBtn.style.color = AnsunaTheme.colors.textMuted;
        prevBtn.style.fontSize = '24px';
        prevBtn.style.cursor = 'pointer';
        prevBtn.style.width = '48px';
        prevBtn.style.height = '48px';
        prevBtn.style.borderRadius = '50%';
        prevBtn.style.display = 'flex';
        prevBtn.style.alignItems = 'center';
        prevBtn.style.justifyContent = 'center';
        prevBtn.addEventListener('mouseenter', () => { prevBtn.style.color = AnsunaTheme.colors.textOnSurface; });
        prevBtn.addEventListener('mouseleave', () => { prevBtn.style.color = AnsunaTheme.colors.textMuted; });
        const playBtn = document.createElement('button');
        playBtn.textContent = this.audioPlayer.isPlaying ? '⏸' : '▶';
        playBtn.style.background = AnsunaTheme.colors.accent;
        playBtn.style.color = 'white';
        playBtn.style.border = 'none';
        playBtn.style.borderRadius = '50%';
        playBtn.style.width = '56px';
        playBtn.style.height = '56px';
        playBtn.style.cursor = 'pointer';
        playBtn.style.fontSize = '24px';
        playBtn.style.display = 'flex';
        playBtn.style.alignItems = 'center';
        playBtn.style.justifyContent = 'center';
        playBtn.addEventListener('click', () => {
            this.audioPlayer.toggle();
            playBtn.textContent = this.audioPlayer.isPlaying ? '⏸' : '▶';
        });
        const nextBtn = document.createElement('button');
        nextBtn.textContent = '⏭';
        nextBtn.style.background = 'transparent';
        nextBtn.style.border = 'none';
        nextBtn.style.color = AnsunaTheme.colors.textMuted;
        nextBtn.style.fontSize = '24px';
        nextBtn.style.cursor = 'pointer';
        nextBtn.style.width = '48px';
        nextBtn.style.height = '48px';
        nextBtn.style.borderRadius = '50%';
        nextBtn.style.display = 'flex';
        nextBtn.style.alignItems = 'center';
        nextBtn.style.justifyContent = 'center';
        nextBtn.addEventListener('mouseenter', () => { nextBtn.style.color = AnsunaTheme.colors.textOnSurface; });
        nextBtn.addEventListener('mouseleave', () => { nextBtn.style.color = AnsunaTheme.colors.textMuted; });
        controls.appendChild(prevBtn);
        controls.appendChild(playBtn);
        controls.appendChild(nextBtn);
        this.element.appendChild(controls);
        // Progress bar
        const progressContainer = document.createElement('div');
        progressContainer.style.width = '100%';
        progressContainer.style.maxWidth = '500px';
        progressContainer.style.height = '4px';
        progressContainer.style.background = '#333';
        progressContainer.style.borderRadius = '2px';
        progressContainer.style.cursor = 'pointer';
        progressContainer.style.marginBottom = '16px';
        const progressFill = document.createElement('div');
        progressFill.style.width = '0%';
        progressFill.style.height = '100%';
        progressFill.style.background = AnsunaTheme.colors.accent;
        progressFill.style.borderRadius = '2px';
        progressContainer.appendChild(progressFill);
        progressContainer.addEventListener('click', (e) => {
            const rect = progressContainer.getBoundingClientRect();
            const clickX = e.clientX - rect.left;
            const percentage = clickX / rect.width;
            this.audioPlayer.seek(percentage * this.audioPlayer.duration);
        });
        this.element.appendChild(progressContainer);
        // Time labels
        const times = document.createElement('div');
        times.style.display = 'flex';
        times.style.justifyContent = 'space-between';
        times.style.maxWidth = '500px';
        times.style.fontSize = '12px';
        times.style.color = AnsunaTheme.colors.textMuted;
        times.style.marginBottom = '24px';
        const currentTime = document.createElement('span');
        currentTime.textContent = '0:00';
        const totalTime = document.createElement('span');
        totalTime.textContent = '0:00';
        times.appendChild(currentTime);
        times.appendChild(totalTime);
        this.element.appendChild(times);
        // Update progress
        const updateProgress = () => {
            if (this.audioPlayer.duration > 0) {
                const pct = (this.audioPlayer.currentTime / this.audioPlayer.duration) * 100;
                progressFill.style.width = `${pct}%`;
                currentTime.textContent = this.formatTime(this.audioPlayer.currentTime);
                totalTime.textContent = this.formatTime(this.audioPlayer.duration);
            }
        };
        const progressInterval = setInterval(updateProgress, 500);
        // Cleanup when screen changes
        this.element.__progressInterval = progressInterval;
    }
    renderEmpty() {
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
        h2.textContent = 'Now Playing';
        h2.style.fontSize = '20px';
        h2.style.marginBottom = '8px';
        h2.style.color = AnsunaTheme.colors.textOnSurface;
        h2.style.fontWeight = '500';
        const p = document.createElement('p');
        p.textContent = 'Select a song to start playing.';
        p.style.fontSize = '14px';
        empty.appendChild(h2);
        empty.appendChild(p);
        this.element.appendChild(empty);
    }
    formatTime(seconds) {
        if (isNaN(seconds) || seconds < 0)
            return '0:00';
        const m = Math.floor(seconds / 60);
        const s = Math.floor(seconds % 60).toString().padStart(2, '0');
        return `${m}:${s}`;
    }
    getElement() {
        return this.element;
    }
}
