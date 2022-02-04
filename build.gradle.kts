plugins {
    `java-gradle-plugin`
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.16.0"
}

group = "com.teamdev.jxbrowser"
version = "0.0.1"

gradlePlugin {
    plugins {
        create("jxbrowser-deps") {
            id = "com.teamdev.jxbrowser.gradle"
            displayName = "Provides dependencies for JxBrowser"
            displayName = "This plug-in allows to specify JxBrowser dependencies in a human-friendly manner."
            implementationClass = "com.teamdev.jxbrowser.gradle.JxBrowserDepsPlugin"
        }
    }
}

pluginBundle {
    website = "https://teamdev.com/jxbrowser"
    tags = listOf("jxbrowser")
}

publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../local-plugin-repository")
        }
    }
}