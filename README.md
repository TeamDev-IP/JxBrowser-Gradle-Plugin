# JxBrowser-Gradle-Plugin

This is a Gradle plug-in that provides convenience methods for adding JxBrowser dependencies to a project.

## Usage

```kotlin
plugins {
    id("com.teamdev.jxbrowser.gradle") version "0.0.1"
}

jxbrowser {
    // Specify JxBrowser version, a mandatory field.
    version = "7.21.2"

    // Or use your own repository. If set, this field overrides `repositoryLocation`.
    repositoryUrl = "https://my.custom.repository"

    // The location of JxBrowser repository to use. It's either US or Europe.
    // By default, it's the US.
    repositoryLocation = Repository.US

    // Adds JxBrowser EAP repository to the project.
    includePreviewBuilds()
}

dependencies {
    // Adds a dependency to the core JxBrowser API.
    implementation(jxbrowser.core())

    // Adds dependencies to the UI toolkit integrations:
    implementation(jxbrowser.swt())
    implementation(jxbrowser.swing())
    implementation(jxbrowser.javafx())

    // Detects the current platform and adds the corresponding Chromium binaries.
    implementation(jxbrowser.currentPlatform())

    // Adds a dependency to specific Chromium binaries.
    implementation(jxbrowser.mac())
    implementation(jxbrowser.macArm())
    implementation(jxbrowser.win32())
    implementation(jxbrowser.win64())
    implementation(jxbrowser.linux64())
    implementation(jxbrowser.linuxArm())
}
```
