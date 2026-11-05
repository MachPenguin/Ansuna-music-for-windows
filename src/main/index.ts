import { app, BrowserWindow, ipcMain, shell } from 'electron';
import path from 'path';
import { YouTubeMusicAPI } from './api';

// Keep a global reference of the window object
let mainWindow: BrowserWindow | null = null;

const ytMusic = new YouTubeMusicAPI();

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1100,
    height: 760,
    minWidth: 900,
    minHeight: 600,
    title: 'Ansuna Music',
    frame: false,
    transparent: false,
    backgroundColor: '#0a0a0a',
    webPreferences: {
      nodeIntegration: false,
      contextIsolation: true,
      preload: path.join(__dirname, 'preload.js'),
    },
    show: false,
  });

  mainWindow.loadFile(path.join(__dirname, '../renderer/index.html'));

  mainWindow.once('ready-to-show', () => {
    mainWindow?.show();
  });

  mainWindow.on('closed', () => {
    mainWindow = null;
  });
}

// Window controls
ipcMain.handle('window:minimize', () => mainWindow?.minimize());
ipcMain.handle('window:maximize', () => {
  if (!mainWindow) return;
  if (mainWindow.isMaximized()) {
    mainWindow.unmaximize();
  } else {
    mainWindow.maximize();
  }
});
ipcMain.handle('window:close', () => mainWindow?.close());
ipcMain.handle('window:isMaximized', () => mainWindow?.isMaximized() ?? false);

// YouTube Music API - Search
ipcMain.handle('yt:search', async (_event, query: string) => {
  return ytMusic.search(query);
});

// YouTube Music API - Home / Recommendations
ipcMain.handle('yt:home', async () => {
  return ytMusic.home();
});

// YouTube Music API - Player / Stream URL
ipcMain.handle('yt:player', async (_event, videoId: string) => {
  return ytMusic.player(videoId);
});

// Open external links
ipcMain.handle('shell:open', async (_event, url: string) => {
  return shell.openExternal(url);
});

app.whenReady().then(createWindow);

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('activate', () => {
  if (BrowserWindow.getAllWindows().length === 0) {
    createWindow();
  }
});
