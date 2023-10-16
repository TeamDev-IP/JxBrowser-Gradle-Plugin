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

package com.teamdev.jxbrowser.gradle

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test

class JxBrowserPluginFunctionalTest {

    private val version = System.getProperty("JXBROWSER_VERSION")

    @TempDir
    lateinit var testProjectDir: File
    private lateinit var buildFile: File
    private lateinit var libsFolder: File

    @BeforeTest
    fun setUp() {
        val settingsFile = File(testProjectDir, "settings.gradle.kts")
        settingsFile.writeText("rootProject.name = \"test-project\"")
        buildFile = File(testProjectDir, "build.gradle.kts")
        libsFolder = File(testProjectDir, "libs")
    }

    @Test
    fun `project successfully built with the plugin`() {
        buildFile.writeText(
            """ 
            plugins {
                java
                id("com.teamdev.jxbrowser")
            }
            
            jxbrowser {
                version = "$version"
            }
            
            dependencies {
                implementation(jxbrowser.core)
                implementation(jxbrowser.currentPlatform)
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("build")
            .build()

        result.outcome(":build") shouldBe SUCCESS
    }

    @Test
    fun `JxBrowser jars are downloaded correctly`() {
        val taskName = "downloadJars"
        val filesToCheck = listOf(
            "jxbrowser-$version.jar",
            "jxbrowser-javafx-$version.jar",
            "jxbrowser-swing-$version.jar",
            "jxbrowser-swt-$version.jar",
            "jxbrowser-win64-$version.jar",
            "jxbrowser-win32-$version.jar",
            "jxbrowser-linux64-$version.jar",
            "jxbrowser-linux64-arm-$version.jar",
            "jxbrowser-mac-$version.jar",
            "jxbrowser-mac-arm-$version.jar"
        )

        buildFile.writeText(
            """ 
            plugins {
                base
                id("com.teamdev.jxbrowser")
            }
            
            jxbrowser {
                version = "$version"
            }
            
            configurations {
                create("toCopy")
            }
            
            dependencies {
                "toCopy"(jxbrowser.core)
                "toCopy"(jxbrowser.swt)
                "toCopy"(jxbrowser.swing)
                "toCopy"(jxbrowser.javafx)
                "toCopy"(jxbrowser.mac)
                "toCopy"(jxbrowser.macArm)
                "toCopy"(jxbrowser.win32)
                "toCopy"(jxbrowser.win64)
                "toCopy"(jxbrowser.linux64)
                "toCopy"(jxbrowser.linuxArm)
            }
            
            tasks.register<Copy>("$taskName") {
                from(configurations.getByName("toCopy"))
                into("libs")
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments(taskName)
            .build()

        result.outcome(":$taskName") shouldBe SUCCESS
        libsFolder.files() shouldContainExactlyInAnyOrder filesToCheck
    }

    private fun BuildResult.outcome(taskName: String) = this.task(taskName)!!.outcome
    private fun File.files() = this.listFiles()!!.map { it.name }
}
