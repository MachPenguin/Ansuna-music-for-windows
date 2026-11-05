/**
 * Standalone mode - mocks the Electron API so the app runs in any browser
 * for testing. When packaged with Electron, the real preload API is used.
 * This file only runs if window.ansuna is not already defined.
 */
if (!window.ansuna) {
    window.ansuna = {
        window: {
            minimize: () => Promise.resolve(),
            maximize: () => Promise.resolve(),
            close: () => Promise.resolve(),
            isMaximized: () => Promise.resolve(false),
        },
        yt: {
            search: async (query) => {
                return [
                    {
                        id: 'mock1',
                        title: `Results for "${query}"`,
                        artist: 'YouTube Music',
                        album: 'Mock',
                        thumbnailUrl: '',
                        durationMs: 0,
                        streamUrl: '',
                    },
                    {
                        id: 'mock2',
                        title: 'Another Track',
                        artist: 'Mock Artist',
                        album: 'Demo',
                        thumbnailUrl: '',
                        durationMs: 0,
                        streamUrl: '',
                    },
                ];
            },
            home: async () => {
                return [
                    {
                        id: 'home1',
                        title: 'Recommended Track 1',
                        artist: 'Artist One',
                        album: 'Album One',
                        thumbnailUrl: '',
                        durationMs: 0,
                        streamUrl: '',
                    },
                    {
                        id: 'home2',
                        title: 'Recommended Track 2',
                        artist: 'Artist Two',
                        album: 'Album Two',
                        thumbnailUrl: '',
                        durationMs: 0,
                        streamUrl: '',
                    },
                    {
                        id: 'home3',
                        title: 'Recommended Track 3',
                        artist: 'Artist Three',
                        album: 'Album Three',
                        thumbnailUrl: '',
                        durationMs: 0,
                        streamUrl: '',
                    },
                ];
            },
            player: async (_videoId) => {
                return null;
            },
        },
        shell: {
            open: async (url) => {
                window.open(url);
            },
        },
    };
}
export {};
