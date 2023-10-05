# JxBrowser Gradle Plugin

A Gradle plug-in that provides convenience methods for adding JxBrowser dependencies into a project.

## Usage

```kotlin
plugins {
    id("com.teamdev.jxbrowser") version "0.0.4"
}

jxbrowser {
    // The JxBrowser version. A mandatory field.
    version = "7.35.1"

    // The location of JxBrowser repository to use. It's either North America or Europe.
    // By default, it's North America.
    repository = Repository.NORTH_AMERICA

    // Alternatively, it may point to a custom repo via its URL, as follows:
    // repository = "https://my.custom.repository"

    // Adds JxBrowser EAP repository to the project.
    includePreviewBuilds()
}

dependencies {
    // Adds a dependency to the core JxBrowser API.
    implementation(jxbrowser.core())

    // Adds dependencies to the UI toolkit integrations.
    implementation(jxbrowser.swt())
    implementation(jxbrowser.swing())
    implementation(jxbrowser.javafx())

    // Detects the current platform and adds the corresponding Chromium binaries.
    implementation(jxbrowser.currentPlatform())
    
    // Adds dependencies with binaries for all supported platforms.
    implementation(jxbrowser.crossPlatform())

    // Adds a dependency to specific Chromium binaries.
    implementation(jxbrowser.mac())
    implementation(jxbrowser.macArm())
    implementation(jxbrowser.win32())
    implementation(jxbrowser.win64())
    implementation(jxbrowser.linux64())
    implementation(jxbrowser.linuxArm())
}
```

---

The information in this repository is provided on the following terms: https://www.teamdev.com/terms-and-privacy
