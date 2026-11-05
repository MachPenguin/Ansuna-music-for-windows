import { contextBridge, ipcRenderer } from 'electron';

// Expose protected methods to the renderer
contextBridge.exposeInMainWorld('ansuna', {
  // Window controls
  window: {
    minimize: () => ipcRenderer.invoke('window:minimize'),
    maximize: () => ipcRenderer.invoke('window:maximize'),
    close: () => ipcRenderer.invoke('window:close'),
    isMaximized: () => ipcRenderer.invoke('window:isMaximized'),
  },

  // YouTube Music API
  yt: {
    search: (query: string) => ipcRenderer.invoke('yt:search', query),
    home: () => ipcRenderer.invoke('yt:home'),
    player: (videoId: string) => ipcRenderer.invoke('yt:player', videoId),
  },

  // Shell
  shell: {
    open: (url: string) => ipcRenderer.invoke('shell:open', url),
  },
});

declare global {
  interface Window {
    ansuna: {
      window: {
        minimize: () => Promise<void>;
        maximize: () => Promise<void>;
        close: () => Promise<void>;
        isMaximized: () => Promise<boolean>;
      };
      yt: {
        search: (query: string) => Promise<any>;
        home: () => Promise<any>;
        player: (videoId: string) => Promise<any>;
      };
      shell: {
        open: (url: string) => Promise<void>;
      };
    };
  }
}
