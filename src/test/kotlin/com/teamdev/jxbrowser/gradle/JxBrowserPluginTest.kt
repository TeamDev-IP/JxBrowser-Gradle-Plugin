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

import com.google.common.truth.Truth.assertThat
import com.teamdev.jxbrowser.gradle.JxBrowserPlugin.Companion.EAP_REPOSITORY_URL
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import kotlin.test.BeforeTest
import kotlin.test.Test

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
        assertThat(extension.includePreviewBuilds).isFalse()
        assertThat(extension.repository).isEqualTo(Repository.NORTH_AMERICA)
    }

    @Test
    fun `dependency notations are resolved properly`() {
        val version = "1.0.0"
        extension.version = version

        assertThat(extension.core.get()).isEqualTo("com.teamdev.jxbrowser:jxbrowser:$version")
        assertThat(extension.javafx.get()).isEqualTo("com.teamdev.jxbrowser:jxbrowser-javafx:$version")
        assertThat(extension.swing.get()).isEqualTo("com.teamdev.jxbrowser:jxbrowser-swing:$version")
        assertThat(extension.swt.get()).isEqualTo("com.teamdev.jxbrowser:jxbrowser-swt:$version")
        assertThat(extension.win64.get()).isEqualTo("com.teamdev.jxbrowser:jxbrowser-win64:$version")
        assertThat(extension.win32.get()).isEqualTo("com.teamdev.jxbrowser:jxbrowser-win32:$version")
        assertThat(extension.linux64.get()).isEqualTo("com.teamdev.jxbrowser:jxbrowser-linux64:$version")
        assertThat(extension.linuxArm.get()).isEqualTo("com.teamdev.jxbrowser:jxbrowser-linux64-arm:$version")
        assertThat(extension.mac.get()).isEqualTo("com.teamdev.jxbrowser:jxbrowser-mac:$version")
        assertThat(extension.macArm.get()).isEqualTo("com.teamdev.jxbrowser:jxbrowser-mac-arm:$version")
        assertThat(extension.crossPlatform.get()).isEqualTo("com.teamdev.jxbrowser:jxbrowser-cross-platform:$version")
    }

    @Test
    fun `maven repository is set correctly`() {
        val customRepository = "https://my.custom.repository"
        extension.repository = customRepository
        assertThat(mavenRepositoryUrls()).contains(customRepository)

        extension.includePreviewBuilds()
        project.evaluationDependsOn(":")
        assertThat(mavenRepositoryUrls()).contains(EAP_REPOSITORY_URL)
    }

    private fun mavenRepositoryUrls() =
        project.repositories
            .filterIsInstance(MavenArtifactRepository::class.java)
            .map { it.url.toString() }
}
