# JxBrowser Gradle Plugin

A Gradle plug-in that provides convenience methods for adding JxBrowser dependencies into a project.

## Usage

```kotlin
import com.teamdev.jxbrowser.gradle.Repository

plugins {
    id("com.teamdev.jxbrowser") version "1.1.0"
}

jxbrowser {
    // The JxBrowser version. A mandatory field.
    // Obtain the latest release version number at https://teamdev.com/jxbrowser/.
    version = "7.39.1"

    // The location of JxBrowser repository to use. It's either North America or Europe.
    // If not specified, the location is set to North America.
    repository = Repository.NORTH_AMERICA

    // Alternatively, it may point to a custom repo via its URL, as follows:
    // repository = "https://my.custom.repository"

    // Adds a repository with JxBrowser preview builds to the project.
    includePreviewBuilds()
}

dependencies {
    // Adds a dependency to the core JxBrowser API.
    implementation(jxbrowser.core)

    // Adds dependencies to the UI toolkit integrations.
    implementation(jxbrowser.swt)
    implementation(jxbrowser.swing)
    implementation(jxbrowser.javafx)
    implementation(jxbrowser.compose)

    // Detects the current platform and adds the corresponding Chromium binaries.
    implementation(jxbrowser.currentPlatform)

    // Adds dependencies to the Chromium binaries for all supported platforms.
    implementation(jxbrowser.crossPlatform)

    // Adds a dependency to the platform-specific Chromium binaries.
    implementation(jxbrowser.mac)
    implementation(jxbrowser.macArm)
    implementation(jxbrowser.win32)
    implementation(jxbrowser.win64)
    implementation(jxbrowser.winArm64)
    implementation(jxbrowser.linux64)
    implementation(jxbrowser.linuxArm)
}
```

---

The information in this repository is provided on the following terms: https://www.teamdev.com/terms-and-privacy
