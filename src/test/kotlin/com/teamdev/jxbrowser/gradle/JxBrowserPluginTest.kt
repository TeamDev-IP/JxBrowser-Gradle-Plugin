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

import com.teamdev.jxbrowser.gradle.JxBrowserPlugin.Companion.EAP_REPOSITORY_URL
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JxBrowserPluginTest {

    private val pluginId = "com.teamdev.jxbrowser"
    private lateinit var project: Project
    private lateinit var extension: JxBrowserExtension

    @BeforeTest
    fun setUp() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply(pluginId)
        extension = project.extensions.getByType(JxBrowserExtension::class.java)
    }

    @Test
    fun `plugin extension is created properly`() {
        assertDoesNotThrow { project.extensions.getByName("jxbrowser") }
        assertFalse(extension.includePreviewBuilds)
        assertEquals(Repository.NORTH_AMERICA, extension.repository)
    }

    @Test
    fun `dependency notations are resolved properly`() {
        val version = "1.0.0"
        extension.version = version

        assertEquals("com.teamdev.jxbrowser:jxbrowser:$version", extension.core.get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-javafx:$version", extension.javafx.get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-swing:$version", extension.swing.get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-swt:$version", extension.swt.get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-win64:$version", extension.win64.get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-win32:$version", extension.win32.get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-linux64:$version", extension.linux64.get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-linux64-arm:$version", extension.linuxArm.get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-mac:$version", extension.mac.get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-mac-arm:$version", extension.macArm.get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-cross-platform:$version", extension.crossPlatform.get())
    }

    @Test
    fun `maven repository is set correctly`() {
        val customRepository = "https://my.custom.repository"
        extension.repository = customRepository
        assertTrue(mavenRepositories().contains(customRepository))

        extension.includePreviewBuilds()
        project.evaluationDependsOn(":")
        assertTrue(mavenRepositories().contains(EAP_REPOSITORY_URL))
    }

    private fun mavenRepositories() = project.repositories.filterIsInstance(MavenArtifactRepository::class.java)
    private fun List<MavenArtifactRepository>.contains(url: String) = this.any { it.url.toString() == url }
}
