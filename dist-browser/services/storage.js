/**
 * Simple localStorage-based persistence for settings and library
 */
const PREFIX = 'ansuna_';
export const Storage = {
    get(key, defaultValue) {
        try {
            const raw = localStorage.getItem(PREFIX + key);
            if (raw === null)
                return defaultValue;
            return JSON.parse(raw);
        }
        catch {
            return defaultValue;
        }
    },
    set(key, value) {
        try {
            localStorage.setItem(PREFIX + key, JSON.stringify(value));
        }
        catch {
            // storage quota exceeded or private mode
        }
    },
    remove(key) {
        localStorage.removeItem(PREFIX + key);
    },
    // Library helpers
    getLikedSongs() {
        return this.get('liked_songs', []);
    },
    addLikedSong(songId) {
        const liked = this.getLikedSongs();
        if (!liked.includes(songId)) {
            liked.push(songId);
            this.set('liked_songs', liked);
        }
    },
    removeLikedSong(songId) {
        const liked = this.getLikedSongs().filter((id) => id !== songId);
        this.set('liked_songs', liked);
    },
    isLiked(songId) {
        return this.getLikedSongs().includes(songId);
    },
    getThemeColor() {
        return this.get('theme_color', '#ed5564');
    },
    setThemeColor(color) {
        this.set('theme_color', color);
    },
};
