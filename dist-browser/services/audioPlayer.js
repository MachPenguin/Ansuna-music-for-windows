/**
 * Audio player service using HTML5 Audio API
 */
export class AudioPlayer {
    audio;
    currentSongId = null;
    onEndCallback = null;
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
    get isPlaying() {
        return !this.audio.paused && !this.audio.ended && this.audio.readyState > 2;
    }
    get currentTrackId() {
        return this.currentSongId;
    }
    async play(song) {
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
    pause() {
        this.audio.pause();
    }
    resume() {
        this.audio.play();
    }
    stop() {
        this.audio.pause();
        this.audio.src = '';
        this.currentSongId = null;
    }
    toggle() {
        if (this.isPlaying) {
            this.pause();
        }
        else {
            this.resume();
        }
    }
    setVolume(volume) {
        this.audio.volume = Math.max(0, Math.min(1, volume));
    }
    seek(time) {
        this.audio.currentTime = time;
    }
    get currentTime() {
        return this.audio.currentTime;
    }
    get duration() {
        return this.audio.duration;
    }
    onEnd(callback) {
        this.onEndCallback = callback;
    }
    destroy() {
        this.stop();
        this.audio.removeEventListener('ended', () => { });
        this.audio.removeEventListener('error', () => { });
    }
}
