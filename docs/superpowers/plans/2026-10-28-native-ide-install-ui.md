# Native IDE Install UI Implementation Plan

> **For agentic workers:** Execute inline in the current session. Steps use checkbox syntax.

**Goal:** Add runtime validation and clear install/progress/error UI to Eclipse and IntelliJ.

**Architecture:** Each IDE manager exposes an asynchronous availability check based on a valid custom executable or a resolved `ewh`/`dsh` command. The manager publishes install progress and completion to its existing UI listener path; the SWT and Swing views render the install action only when needed.

**Tech Stack:** Java 21, Eclipse SWT/JFace/Tycho, Kotlin/JVM, IntelliJ Platform SDK, Gradle.

---

### Task 1: Eclipse runtime state and view

**Files:**
- Modify `eclipse/src/com/deepseek/everywhere/service/DshManager.java`
- Modify `eclipse/src/com/deepseek/everywhere/model/HarnessStatus.java`
- Modify `eclipse/src/com/deepseek/everywhere/ui/EverywhereView.java`

- [x] Add an availability check that treats a valid custom executable or a resolved `ewh`/`dsh` as installed, and run it outside the SWT UI thread.
- [x] Add explicit installing and missing-runtime states to the existing status listener flow.
- [x] In the overlay, hide the install button when available, show progress and disable the button during npm, and present an actionable error if installation fails or validation remains false.
- [x] After installation completes, re-check availability and update the visible controls on the SWT display thread.
- [x] Run `./eclipse/mvnw.cmd -f eclipse/pom.xml -DskipTests package` and confirm Tycho succeeds.

### Task 2: IntelliJ runtime state and tool window

**Files:**
- Modify `intellij/src/main/kotlin/com/deepseek/everywhere/service/DshManagerService.kt`
- Modify `intellij/src/main/kotlin/com/deepseek/everywhere/ui/EverywhereToolWindowPanel.kt`
- Modify `intellij/src/main/kotlin/com/deepseek/everywhere/action/Actions.kt`

- [x] Add an asynchronous availability check for the configured custom executable or resolved `ewh`/`dsh` command.
- [x] Notify the panel of installing, installed, and missing states through the service listener path.
- [x] Show the install CTA only for a missing runtime; disable it and show progress while npm runs; after completion revalidate and show a success or actionable error state.
- [x] Keep install UI consistent with the missing-runtime state and retain the panel action as the retry path.
- [x] Run `intellij/gradlew.bat -p intellij buildPlugin` with JDK 17 and verify the ZIP appears in `intellij/build/distributions`.

### Task 3: Cross-IDE verification

**Files:**
- Verify only; do not modify unrelated files.

- [x] Run `git diff --check` on the source changes.
- [x] Verify missing-runtime and installed-runtime branches do not show contradictory controls.
- [x] Confirm install failure presents `npm install -g @everywhere-harness/ewh` and a retry path in both IDEs.
