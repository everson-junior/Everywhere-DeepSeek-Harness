# Eclipse Marketplace Feature Implementation Plan

> **For agentic workers:** Execute inline in the current session. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Publish the Eclipse Marketplace-requested `com.deepseek.everywhere.feature.group` IU in the existing p2 update site.

**Architecture:** Add a Tycho `eclipse-feature` module with Maven artifact ID and feature ID `com.deepseek.everywhere`, version `1.0.1.qualifier`; include the existing plugin, whose version remains `1.0.0.qualifier`. Tycho requires the Maven artifact ID to match the feature ID. The distinct versions keep the feature and bundle coordinates separate and produce the exact `com.deepseek.everywhere.feature.group` IU. Add the module to the reactor, make the repository project depend on it, and categorize the feature instead of the bundle. Keep the existing p2 URL and all IDE runtime code unchanged.

**Tech Stack:** Maven Tycho 4.0.8, Eclipse p2, Java 21, PowerShell.

---

### Task 1: Add the installable Eclipse feature

**Files:**
- Create: `eclipse/feature/pom.xml`
- Create: `eclipse/feature/feature.xml`
- Create: `eclipse/feature/build.properties`
- Modify: `eclipse/reactor/pom.xml`

- [ ] Create `eclipse/feature/pom.xml` using the reactor parent and Tycho feature packaging:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.deepseek.everywhere</groupId>
        <artifactId>everywhere-eclipse-build</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../reactor/pom.xml</relativePath>
    </parent>

    <artifactId>com.deepseek.everywhere</artifactId>
    <version>1.0.1-SNAPSHOT</version>
    <packaging>eclipse-feature</packaging>
    <name>EveryWhere Code Eclipse Feature</name>

    <build>
        <plugins>
            <plugin>
                <groupId>org.eclipse.tycho</groupId>
                <artifactId>tycho-maven-plugin</artifactId>
                <version>${tycho.version}</version>
                <extensions>true</extensions>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] Create `eclipse/feature/feature.xml` with `id="com.deepseek.everywhere"`, `version="1.0.1.qualifier"`, and one required plugin entry for `com.deepseek.everywhere` at `version="0.0.0"`.
- [ ] Create `eclipse/feature/build.properties` containing `bin.includes = feature.xml`.
- [ ] Add `<module>../feature</module>` to the reactor modules, before `../update-site` so the feature is built before the repository.

### Task 2: Publish the feature from the existing update site

**Files:**
- Modify: `eclipse/update-site/pom.xml`
- Modify: `eclipse/update-site/category.xml`

- [ ] Add a dependency on feature artifact `com.deepseek.everywhere`, version `1.0.1-SNAPSHOT`, with type `eclipse-feature` to the repository project's dependencies.
- [ ] Replace the direct `<bundle id="com.deepseek.everywhere">` category entry with `<feature id="com.deepseek.everywhere" version="0.0.0">` and retain the existing `everywhere` category definition.

### Task 3: Validate the generated repository without touching local build outputs

**Files:**
- Modify: `.github/workflows/release.yml`
- Verify: temporary Eclipse build output only; do not modify workspace build outputs.

- [ ] Copy the `eclipse` tree to a unique temporary directory and build that copy with `mvnw.cmd -f <temp>\eclipse\reactor\pom.xml -DskipTests package`; do not run `clean` and do not build in the workspace because it contains existing generated files.
- [ ] Inspect the temporary `eclipse/target/repository/content.jar` (or its generated `content.xml`) and assert that it contains an IU named `com.deepseek.everywhere.feature.group` that requires `com.deepseek.everywhere`.
- [ ] Before upload and deploy, make the workflow inspect `content.jar` and fail if it lacks the exact Marketplace group IU.
- [ ] Run `py -3 scripts/validate-release.py` from the workspace and verify it exits successfully.
- [ ] Run `git diff --check` and confirm only the intended source/docs files changed; leave existing skill edits and generated files untouched.

### Task 4: Prepare the corrected release

**Files:**
- Release only after Tasks 1-3 pass.

- [ ] Follow `.agents/skills/ewh-multi-ide-release-validator/SKILL.md` and `.agents/skills/verification-before-completion/SKILL.md` before release actions.
- [ ] Stage only the feature module, reactor/update-site configuration, release workflow guard, and the two new specification/plan documents; do not stage pre-existing skill edits or generated build artifacts.
- [ ] Commit the validated correction, create tag `v1.3.2`, and push the commit and tag so the existing release workflow builds and publishes the updated p2 site.
- [ ] Verify the release workflow's Eclipse job succeeds and that the public `content.jar`, `artifacts.jar`, and `p2.index` return HTTP 200 after deployment.
