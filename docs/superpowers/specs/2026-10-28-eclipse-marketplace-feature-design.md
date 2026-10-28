# Eclipse Marketplace Feature Design

## Goal

Make the Eclipse Marketplace installation resolve the requested p2 installable unit `com.deepseek.everywhere.feature.group` from the published update site.

## Design

Add a Tycho `eclipse-feature` project at `eclipse/feature` with Maven artifact ID and feature ID `com.deepseek.everywhere`, at version `1.0.1.qualifier`. The feature includes the existing `com.deepseek.everywhere` plugin, whose version remains `1.0.0.qualifier`; the separate Maven and p2 versions distinguish the feature from the bundle. Add the feature project to the Maven reactor, make the update-site project depend on it, and replace the update site's direct bundle category entry with a feature entry. Tycho will then publish the required `com.deepseek.everywhere.feature.group` IU while retaining the existing plugin bundle and update-site URL.

## Scope

Only the Eclipse packaging, p2 category, and release-time p2 validation are changed. Keep the Marketplace URL, plugin ID, runtime behavior, and other IDE packages unchanged. Preserve current user and generated files outside the implementation.

## Verification

Build the Eclipse Tycho reactor, inspect the generated p2 metadata to confirm `com.deepseek.everywhere.feature.group` is present and requires `com.deepseek.everywhere`, and run `py -3 scripts/validate-release.py`. The release workflow must fail before upload/deploy if that feature group is absent. Verify the published metadata endpoints after release deployment. The corrected package is prepared for a new release after local validation.
