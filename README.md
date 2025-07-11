# Playlite

Playlite is a modern Android video player and file explorer app. It allows users to browse files on their device and play video files with a sleek, fullscreen ExoPlayer-based interface. The app is designed to provide a smooth experience with features like orientation-resilient playback, modern file navigation, and a user-friendly UI.

## Features

- **File Explorer**: Browse device storage with a modern, Material-themed file browser.
- **Video Playback**: Play videos in fullscreen using ExoPlayer (AndroidX Media3).
- **Orientation Handling**: Video playback resumes from the last position when the device is rotated.
- **Modern Controls**: Player controls are styled for a modern look and feel.
- **Double Back to Exit**: Prevents accidental app exits from the file browser by requiring a double back press.
- **Permissions**: Handles storage permissions for different Android versions.

## Screenshots

*(Add screenshots here)*

## Getting Started

### Prerequisites
- Android Studio (latest recommended)
- Android device or emulator (API 23+)

### Building and Running
1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on your device or emulator.

### Permissions
- The app requests `READ_EXTERNAL_STORAGE` and, for Android 11+, `MANAGE_EXTERNAL_STORAGE` to access files.

## Project Structure

- `HomeActivity.kt`: Entry point, hosts the file browser fragment.
- `FileBrowserFragment.kt`: File navigation UI and logic.
- `FileListAdapter.kt`: RecyclerView adapter for file items.
- `MainActivity.kt`: Video player activity using ExoPlayer.
- `res/layout/`: Contains all UI XML layouts.
- `res/drawable/`: Contains vector and shape drawables for icons and backgrounds.

## Customization
- **Player Controls**: The player uses a custom control layout (`exo_player_control_view.xml`) for a modern look.
- **File Icons**: Directory and file icons can be customized in `FileListAdapter.kt` and `res/drawable/`.

## Known Issues
- Some features may require additional permissions on Android 11+.
- Only common video formats are supported by default.

## License

*(Specify your license here, e.g., MIT, Apache 2.0, etc.)*

## Credits
- [ExoPlayer (AndroidX Media3)](https://developer.android.com/guide/topics/media/exoplayer)
- [Material Components for Android](https://material.io/develop/android)

---

*This README was generated based on the current project files.*

