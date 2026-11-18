<<<<<<< HEAD
# LUST Agent — Phase 1 repaired build

## What was repaired
- Fixed the Kotlin string-literal compiler error in `ChatScreen.kt`.
- Reworked the chat layout to fit narrow phone screens.
- Added an always-visible Send button.
- Added a microphone/voice-input button with runtime `RECORD_AUDIO` permission.
- Added Free Hand Agent mode with an animated HUD.
- Added a local `PartnerRouter` that classifies action/tool requests vs language/reasoning requests.
- Added a cooperative model-worker scheduler so two local decoders do not intentionally run at the same time.
- Added safe build-time cloud configuration fields. The actual API key is **not** stored in source.
- Kept the existing Gradle/Compose project structure rather than rebuilding the project from scratch.

## Important runtime limitation
The supplied source bundle contains **no Cactus/Needle native library, Cactus Kotlin bindings, llama.cpp binary, Qwen model file, Vosk library, accessibility service, or cloud provider endpoint implementation**. Therefore this repaired bundle does not pretend those components are already integrated.

The router and worker interfaces are the integration seam for the real project:
- Needle/Cactus worker → device automation and structured tool calls.
- Qwen/llama.cpp worker → conversation, planning and language.
- Cloud worker → optional fallback when explicitly configured.

Cactus documents Android integration through a native `libcactus.so` plus Kotlin bindings, and its engine supports completion, tool calling and cloud handoff. Needle is documented as a small on-device tool-calling model. Those native artifacts must be added to this project before a real Needle runtime can execute.

## Cloud secret
Provide these only through CI/local Gradle configuration:
- `LUST_CLOUD_API_KEY`
- `LUST_CLOUD_ENDPOINT`

Never commit a real key to GitHub. The API key pasted in chat should be rotated/revoked.

## Build
The GitHub Actions workflow uses JDK 17 and Android SDK 35 and runs:
- debug APK build
- unit tests
- Android lint

The project still requires a normal Android/Gradle environment to perform the final build.
=======
# LUST — Android Phase 1

A clean, buildable Phase-1 foundation for a modular Android AI assistant.

### Included
- Jetpack Compose chat UI
- Provider abstraction
- Deterministic mock provider
- Basic calculator module
- Multi-module Gradle structure
- GitHub Actions Android build

### Build
```bash
./gradlew assembleDebug
```
If you are building on Android/Termux, use a Gradle distribution or Android build environment compatible with the installed JDK.

### Next phases
1. Local llama.cpp/LiteRT provider
2. Remote provider adapters
3. Agent planning/tool orchestration
4. Persistent memory/RAG
5. Voice/STT/TTS
6. Android integrations and accessibility
>>>>>>> origin/main
