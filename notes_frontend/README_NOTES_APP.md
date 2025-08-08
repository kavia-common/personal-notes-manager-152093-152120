# Notes App (Kotlin Jetpack Compose)

A minimalistic light-themed notes application for Android, built with Jetpack Compose.

## Features

- Create, edit, delete, and search notes.
- Clean, minimal UI with top bar, floating action button, and note editor.
- In-memory storage for notes (can be replaced with a local DB).
- Color palette: Primary #1976D2, Accent #FFC107, Secondary #424242.
- Responsive to modern Android design guidelines.

## Building/Running

From this directory:

```shell
./gradlew build
:app:installDebug
```

Then launch the app on your Android device or emulator.

## Architecture

- All UI in Jetpack Compose.
- Main code: `app/src/main/kotlin/org/example/app/MainActivity.kt`
- No custom environment variables required.
