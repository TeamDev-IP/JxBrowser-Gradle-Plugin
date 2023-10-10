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

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JxBrowserPluginFunctionalTest {

    @TempDir
    lateinit var testProjectDir: File
    private lateinit var buildFile: File

    @BeforeTest
    fun setUp() {
        val settingsFile = File(testProjectDir, "settings.gradle.kts")
        settingsFile.writeText("rootProject.name = \"test-project\"")
        buildFile = File(testProjectDir, "build.gradle.kts")
    }

    @Test
    @Throws(IOException::class)
    fun `project successfully built with the plugin`() {
        buildFile.writeText(
            """ 
            plugins {
                java
                id("com.teamdev.jxbrowser")
            }
            
            jxbrowser {
                version = "+"
            }
            
            dependencies {
                implementation(jxbrowser.core)
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withPluginClasspath()
            .withArguments("build")
            .build()
        assertEquals(SUCCESS, result.task(":build")!!.outcome)
    }

    @Test
    @Throws(IOException::class)
    fun `JxBrowser dependencies are downloaded correctly`() {
        val version = "7.35.2"
        val taskName = "downloadJars"

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
        assertEquals(SUCCESS, result.task(":$taskName")!!.outcome)

        val libsFolder = File(testProjectDir, "libs")
        assertTrue(libsFolder.exists())
        assertTrue(libsFolder.containsFile("jxbrowser-$version.jar"))
        assertTrue(libsFolder.containsFile("jxbrowser-javafx-$version.jar"))
        assertTrue(libsFolder.containsFile("jxbrowser-swing-$version.jar"))
        assertTrue(libsFolder.containsFile("jxbrowser-swt-$version.jar"))
        assertTrue(libsFolder.containsFile("jxbrowser-win64-$version.jar"))
        assertTrue(libsFolder.containsFile("jxbrowser-win32-$version.jar"))
        assertTrue(libsFolder.containsFile("jxbrowser-linux64-$version.jar"))
        assertTrue(libsFolder.containsFile("jxbrowser-linux64-arm-$version.jar"))
        assertTrue(libsFolder.containsFile("jxbrowser-mac-$version.jar"))
        assertTrue(libsFolder.containsFile("jxbrowser-mac-arm-$version.jar"))
    }

    private fun File.containsFile(name: String) = this.listFiles()!!.any { it.name == name }
}
