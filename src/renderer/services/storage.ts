/**
 * Simple localStorage-based persistence for settings and library
 */
const PREFIX = 'ansuna_';

export const Storage = {
  get<T>(key: string, defaultValue: T): T {
    try {
      const raw = localStorage.getItem(PREFIX + key);
      if (raw === null) return defaultValue;
      return JSON.parse(raw) as T;
    } catch {
      return defaultValue;
    }
  },

  set<T>(key: string, value: T): void {
    try {
      localStorage.setItem(PREFIX + key, JSON.stringify(value));
    } catch {
      // storage quota exceeded or private mode
    }
  },

  remove(key: string): void {
    localStorage.removeItem(PREFIX + key);
  },

  // Library helpers
  getLikedSongs(): string[] {
    return this.get<string[]>('liked_songs', []);
  },

  addLikedSong(songId: string): void {
    const liked = this.getLikedSongs();
    if (!liked.includes(songId)) {
      liked.push(songId);
      this.set('liked_songs', liked);
    }
  },

  removeLikedSong(songId: string): void {
    const liked = this.getLikedSongs().filter((id) => id !== songId);
    this.set('liked_songs', liked);
  },

  isLiked(songId: string): boolean {
    return this.getLikedSongs().includes(songId);
  },

  getThemeColor(): string {
    return this.get<string>('theme_color', '#ed5564');
  },

  setThemeColor(color: string): void {
    this.set('theme_color', color);
  },
};
