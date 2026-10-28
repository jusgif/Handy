# Handy for Android (work in progress)

This directory contains a native Android implementation of Handy's cross-app, offline voice-typing workflow. It is intentionally separate from the existing Tauri desktop application.

## Product requirements

- Movable floating microphone bubble over other apps, enabled only after the user grants Android's overlay permission.
- On-device speech recognition using a Whisper-compatible native engine. No cloud transcription.
- Insert recognized text at the active cursor using Android's input-method APIs where available; offer an explicit copy fallback only when insertion is not possible.
- No recording history, transcript history, database, analytics, or content logs.
- Audio and transcript data are transient. Delete temporary audio on success, failure, cancellation, and service teardown.
- Model files may persist because they are model assets, not user recordings or transcripts.

## Android constraints

Android does not expose a universal API for an overlay to inspect every other app's text field or reliably inject text into arbitrary apps. The robust path is an Android input-method companion, with the floating bubble as the primary interaction. Some protected/custom fields may not support insertion.

Microphone foreground services must follow the Android version's permission and launch rules. On recent Android versions, a microphone service generally needs to be started while the app is visible after microphone permission is granted.

## Build

Open this directory in Android Studio with a current Android SDK installed, or from this directory run:

    gradle assembleDebug

The native transcription engine and end-to-end cross-app insertion are not yet wired in this initial scaffold. Do not treat this scaffold as a finished voice-typing release or as a verified APK.

## Planned milestones

1. Native app shell, permission flow, and floating overlay.
2. Audio capture lifecycle with strict temporary-data cleanup.
3. Build and integrate whisper.cpp for Android ARM64; model download and checksum verification.
4. Input method and insertion flow; clipboard fallback with explicit user consent.
5. Device testing, accessibility review, and release APK validation.
