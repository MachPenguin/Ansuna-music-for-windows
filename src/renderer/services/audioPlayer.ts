/**
 * Audio player service using HTML5 Audio API
 */
export class AudioPlayer {
  private audio: HTMLAudioElement;
  private currentSongId: string | null = null;
  private onEndCallback: (() => void) | null = null;

  constructor() {
    this.audio = new Audio();
    this.audio.preload = 'none';

    this.audio.addEventListener('ended', () => {
      this.currentSongId = null;
      this.onEndCallback?.();
    });

    this.audio.addEventListener('error', (e) => {
      console.error('Audio error:', e);
      this.currentSongId = null;
      this.onEndCallback?.();
    });
  }

  get isPlaying(): boolean {
    return !this.audio.paused && !this.audio.ended && this.audio.readyState > 2;
  }

  get currentTrackId(): string | null {
    return this.currentSongId;
  }

  async play(song: { id: string; title: string; streamUrl?: string }): Promise<void> {
    this.stop();
    this.currentSongId = song.id;

    if (!song.streamUrl) {
      // No stream URL available; cannot play
      return;
    }

    this.audio.src = song.streamUrl;
    this.audio.volume = 1.0;
    await this.audio.play();
  }

  pause(): void {
    this.audio.pause();
  }

  resume(): void {
    this.audio.play();
  }

  stop(): void {
    this.audio.pause();
    this.audio.src = '';
    this.currentSongId = null;
  }

  toggle(): void {
    if (this.isPlaying) {
      this.pause();
    } else {
      this.resume();
    }
  }

  setVolume(volume: number): void {
    this.audio.volume = Math.max(0, Math.min(1, volume));
  }

  seek(time: number): void {
    this.audio.currentTime = time;
  }

  get currentTime(): number {
    return this.audio.currentTime;
  }

  get duration(): number {
    return this.audio.duration;
  }

  onEnd(callback: () => void): void {
    this.onEndCallback = callback;
  }

  destroy(): void {
    this.stop();
    this.audio.removeEventListener('ended', () => {});
    this.audio.removeEventListener('error', () => {});
  }
}
