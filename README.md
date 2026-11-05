# Ansuna Music for Windows

A native Windows music client inspired by Ansuna Music (Android). Streams from YouTube Music's catalog ad-free, with search, synced lyrics, playlists, and library management.

> **Port in progress.** Desktop port of the Android Ansuna Music app, built with Kotlin + JavaFX.

## Tech stack

- **Language:** Kotlin (JVM 21)
- **UI:** JavaFX (CSS-styled dark theme)
- **Networking:** Ktor client
- **JSON:** kotlinx.serialization
- **Build:** Gradle 9.3.1

## Build & run

```bash
git clone https://github.com/MachPenguin/Ansuna-music-for-windows.git
cd Ansuna-music-for-windows

./gradlew :desktop-app:run        # run the app
./gradlew :desktop-app:jar        # build jar
./gradlew :desktop-app:distZip    # build a distributable zip
```

## License

GPL-3.0 — educational / personal use only. See the original Ansuna Music project.
