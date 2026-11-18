# LUST-Agent — Phase 1 source bundle

This is the first assembled Android Studio project created from the LUST code and requirements supplied in the conversation.

## Included now
- Android app using Kotlin and Jetpack Compose.
- Minimal chat interface with local conversation persistence.
- Deterministic mock responses (not a real LLM).
- Gradle project configuration and GitHub Actions debug-APK workflow.

## Important status
This is a **Phase 1 starter bundle**, not the completed autonomous Android agent. The earlier pasted source contained truncated UI and compile errors, so this archive uses a clean, minimal app foundation rather than copying broken fragments. Hermes, accessibility automation, notification reading, voice, task planning, and CapCut recipes are not integrated yet. They will be added after this baseline is inspected and built.

## Build
Open this folder in Android Studio (JDK 17; Android SDK 35), sync Gradle, then run `:app:assembleDebug`. The GitHub Actions workflow builds the debug APK on push or manual dispatch.

No API keys or model weights are included. The app makes no network requests.
