# JxBrowser Gradle Plugin

A Gradle plug-in that provides convenience methods for adding JxBrowser dependencies into a project.

## Usage

```kotlin
import com.teamdev.jxbrowser.gradle.Repository

plugins {
    id("com.teamdev.jxbrowser") version "1.2.1"
}

jxbrowser {
    // The JxBrowser version. A mandatory field.
    // Obtain the latest release version number at https://teamdev.com/jxbrowser/.
    version = "8.1.0"

    // The location of JxBrowser repository to use. It's either North America or Europe.
    // If not specified, the location is set to North America.
    repository = Repository.EUROPE
    // repository = Repository.NORTH_AMERICA

    // Alternatively, it may point to a custom repo via its URL, as follows:
    // repository = "https://my.custom.repository"
}

dependencies {
    // Adds a dependency to the platform-specific Chromium binaries.
    implementation(jxbrowser.mac)      // Mac Intel
    implementation(jxbrowser.macArm)   // Mac Apple silicon
    implementation(jxbrowser.win32)    // Windows 32-bit
    implementation(jxbrowser.win64)    // Windows 64-bit
    implementation(jxbrowser.winArm)   // Windows 64-bit ARM
    implementation(jxbrowser.linux64)  // Linux 64-bit
    implementation(jxbrowser.linuxArm) // Linux 64-bit ARM

    // Detects the current platform and adds the corresponding Chromium binaries.
    implementation(jxbrowser.currentPlatform)

    // Adds dependencies to the UI toolkit integrations.
    implementation(jxbrowser.swt)
    implementation(jxbrowser.swing)
    implementation(jxbrowser.javafx)
    implementation(jxbrowser.compose)
}
```

---

The information in this repository is provided on the following terms: https://www.teamdev.com/terms-and-privacy
