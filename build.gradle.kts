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

import org.gradle.api.JavaVersion.toVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.9.10"
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.2.1"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}

group = projectProperty("GROUP")
version = projectProperty("VERSION")

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(kotlin("test"))
    testImplementation(gradleTestKit())
    testImplementation("io.kotest:kotest-assertions-core:5.7.2")
}

java {
    sourceCompatibility = toVersion(projectProperty("JAVA_VERSION"))
    targetCompatibility = toVersion(projectProperty("JAVA_VERSION"))
}

gradlePlugin {
    plugins {
        create(projectProperty("NAME")) {
            id = projectProperty("ID")
            displayName = projectProperty("DISPLAY_NAME")
            description = projectProperty("DESCRIPTION")
            implementationClass = projectProperty("IMPLEMENTATION_CLASS")
            tags = listOf("jxbrowser")
        }
    }
    website = projectProperty("WEBSITE")
    vcsUrl = projectProperty("VCS_URL")
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

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = projectProperty("JAVA_VERSION")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("JXBROWSER_VERSION", projectProperty("JXBROWSER_VERSION"))
}

fun projectProperty(name: String) = project.property(name).toString()
