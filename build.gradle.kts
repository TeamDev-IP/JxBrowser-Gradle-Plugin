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
            displayName = "Add JxBrowser repository and dependencies to the project"
            description = """
                This plug-in adds JxBrowser repository to the project and provides convenience methods for applying
                JxBrowser dependencies.
            """.trimIndent()
            implementationClass = "com.teamdev.jxbrowser.gradle.DepsPlugin"
        }
    }
}

pluginBundle {
    website = "https://teamdev.com/jxbrowser"
    tags = listOf("jxbrowser")
}

// Used only for testing.
publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../local-plugin-repository")
        }
    }
}
