/*
 *  Copyright 2023, TeamDev. All rights reserved.
 *
 *  Redistribution and use in source and/or binary forms, with or without
 *  modification, must retain the above copyright notice and the following
 *  disclaimer.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object BuildSettings {
    const val GROUP = "com.teamdev.jxbrowser"
    const val VERSION = "1.0.1"
    const val JXBROWSER_VERSION = "7.36"
    val javaVersion = JavaVersion.VERSION_1_8
}

object PluginProperties {
    const val ID = "com.teamdev.jxbrowser"
    const val NAME = "jxbrowser-deps"
    const val DISPLAY_NAME = "Adds JxBrowser repository and dependencies to the project"
    const val WEBSITE = "https://github.com/TeamDev-IP/JxBrowser-Gradle-Plugin"
    const val VCS_URL = "https://github.com/TeamDev-IP/JxBrowser-Gradle-Plugin"
    const val IMPLEMENTATION_CLASS = "com.teamdev.jxbrowser.gradle.JxBrowserPlugin"
    const val DESCRIPTION =
        "Adds JxBrowser repository to the project and provides convenience methods for applying JxBrowser dependencies."
    val TAGS = listOf("jxbrowser")
}

plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.9.23"
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.2.1"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

group = BuildSettings.GROUP
version = BuildSettings.VERSION

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(kotlin("test"))
    testImplementation(gradleTestKit())
    testImplementation("io.kotest:kotest-assertions-core:5.8.1")
}

java {
    with(BuildSettings) {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

kotlin {
    explicitApi()
}

gradlePlugin {
    with(PluginProperties) {
        plugins {
            create(NAME) {
                id = ID
                displayName = DISPLAY_NAME
                description = DESCRIPTION
                implementationClass = IMPLEMENTATION_CLASS
                tags = TAGS
            }
        }
        website = WEBSITE
        vcsUrl = VCS_URL
    }
}

publishing {
    description = "Publishes the plugin to the local repository. Used for testing only."
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../local-plugin-repository")
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = BuildSettings.javaVersion.toString()
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("JXBROWSER_VERSION", BuildSettings.JXBROWSER_VERSION)
}
