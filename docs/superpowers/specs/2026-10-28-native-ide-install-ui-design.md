# Native IDE Install UI Design

## Goal

Make the Eclipse and IntelliJ install experience behave like the VS Code missing-runtime view: clearly show installation only when the EveryWhere runtime is unavailable, and provide visible feedback while installation runs.

## Behavior

- Runtime availability is true when the configured custom executable exists or the `ewh`/`dsh` executable can be resolved from known install locations or PATH.
- Availability checks run off the UI thread.
- When the runtime is missing, the native overlay shows the install action and hides start-only controls that cannot succeed.
- When the runtime is available, the install CTA is hidden and normal start controls remain available.
- Installation stays user-initiated. While npm runs, the install action is disabled and the UI shows progress. On success, availability is checked again; on failure or a missing executable, the UI shows the error and the manual command `npm install -g @everywhere-harness/ewh`.
- Eclipse uses SWT controls and console output; IntelliJ uses Swing controls, the existing service notification group, and log buffer.

## Scope

Update the runtime service and main view in Eclipse and IntelliJ only. Keep the VS Code webview as the behavior reference. Preserve existing extension IDs, runtime commands, and start/stop behavior when the runtime is installed.

## Verification

Build the Eclipse Tycho package and IntelliJ plugin ZIP with JDK 17. Check the absent-runtime and installed-runtime branches using the availability result and confirm install controls change visibility and state after completion.