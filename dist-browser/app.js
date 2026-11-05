import { AudioPlayer } from './services/audioPlayer';
import { NavBar } from './components/NavBar';
import { MiniPlayer } from './components/MiniPlayer';
import { HomeScreen } from './screens/Home';
import { SearchScreen } from './screens/Search';
import { LibraryScreen } from './screens/Library';
import { PlayerScreen } from './screens/Player';
import './standalone';
class App {
    navBar;
    miniPlayer;
    audioPlayer;
    screens = new Map();
    screenInstances = new Map();
    currentScreenId = 'home';
    container;
    constructor() {
        this.audioPlayer = new AudioPlayer();
        this.navBar = new NavBar((screenId) => this.switchScreen(screenId));
        this.miniPlayer = new MiniPlayer(this.audioPlayer);
        this.container = document.getElementById('screen-container');
        this.setupWindowControls();
        this.setupScreens();
        this.setupMiniPlayer();
        // Show home by default
        this.switchScreen('home');
    }
    setupWindowControls() {
        const btnMin = document.getElementById('btn-minimize');
        const btnMax = document.getElementById('btn-maximize');
        const btnClose = document.getElementById('btn-close');
        btnMin.addEventListener('click', async () => {
            await window.ansuna.window.minimize();
        });
        btnMax.addEventListener('click', async () => {
            await window.ansuna.window.maximize();
        });
        btnClose.addEventListener('click', async () => {
            await window.ansuna.window.close();
        });
    }
    setupScreens() {
        // Insert nav bar into sidebar
        const sidebar = document.getElementById('sidebar');
        sidebar.appendChild(this.navBar.getElement());
        // Create screen instances
        const home = new HomeScreen((song) => this.selectSong(song), (song) => this.playSong(song));
        const search = new SearchScreen((song) => this.selectSong(song), (song) => this.playSong(song));
        const library = new LibraryScreen();
        const player = new PlayerScreen(this.audioPlayer);
        this.screenInstances.set('home', home);
        this.screenInstances.set('search', search);
        this.screenInstances.set('library', library);
        this.screenInstances.set('player', player);
        this.screens.set('home', home.getElement());
        this.screens.set('search', search.getElement());
        this.screens.set('library', library.getElement());
        this.screens.set('player', player.getElement());
        // Wire up search from home
        window.__ansunaOnSearch = (query) => {
            this.switchScreen('search');
            const searchScreen = this.screenInstances.get('search');
            searchScreen.performSearch(query);
        };
    }
    setupMiniPlayer() {
        const miniPlayerContainer = document.getElementById('mini-player');
        miniPlayerContainer.appendChild(this.miniPlayer.getElement());
        // Audio end -> clear mini player
        this.audioPlayer.onEnd(() => {
            this.miniPlayer.setSong(null);
        });
    }
    switchScreen(screenId) {
        // Hide all screens
        this.screens.forEach((el) => {
            el.classList.add('hidden');
        });
        // Show selected
        const screen = this.screens.get(screenId);
        if (screen) {
            screen.classList.remove('hidden');
            this.container.appendChild(screen);
        }
        this.currentScreenId = screenId;
        this.navBar.setActive(screenId);
    }
    selectSong(song) {
        // Just select, don't play yet (user can click play)
        console.log('Selected:', song.title);
    }
    playSong(song) {
        if (!song.streamUrl) {
            // Resolve stream URL first
            this.resolveAndPlay(song);
        }
        else {
            this.audioPlayer.play(song).then(() => {
                this.miniPlayer.setSong(song);
            });
        }
    }
    async resolveAndPlay(song) {
        try {
            const resolved = await window.ansuna.yt.player(song.id);
            if (resolved && resolved.streamUrl) {
                song.streamUrl = resolved.streamUrl;
                await this.audioPlayer.play(song);
                this.miniPlayer.setSong(song);
            }
            else {
                console.error('No stream URL available');
            }
        }
        catch (err) {
            console.error('Failed to resolve stream:', err);
        }
    }
    // Expose for search screen
    getAudioPlayer() {
        return this.audioPlayer;
    }
}
// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    new App();
});
