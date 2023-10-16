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
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import kotlin.test.BeforeTest
import kotlin.test.Test

class JxBrowserPluginTest {

    private val jxBrowserVersion = System.getProperty("JXBROWSER_VERSION")
    private val pluginId = "com.teamdev.jxbrowser"
    private lateinit var project: Project
    private lateinit var extension: JxBrowserExtension

    @BeforeTest
    fun setUp() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply(pluginId)
        extension = project.extensions.getByType(JxBrowserExtension::class.java)
        extension.version = jxBrowserVersion
    }

    @Test
    fun `plugin extension is created properly`() {
        assertDoesNotThrow { project.extensions.getByName("jxbrowser") }
        extension.includePreviewBuilds shouldBe false
        extension.repository shouldBe Repository.NORTH_AMERICA
    }

    @Test
    fun `dependency notations are resolved properly`() {
        with(extension) {
            val group = "com.teamdev.jxbrowser"

            swt.get() shouldBe "$group:jxbrowser-swt:$jxBrowserVersion"
            mac.get() shouldBe "$group:jxbrowser-mac:$jxBrowserVersion"
            core.get() shouldBe "$group:jxbrowser:$jxBrowserVersion"
            swing.get() shouldBe "$group:jxbrowser-swing:$jxBrowserVersion"
            win32.get() shouldBe "$group:jxbrowser-win32:$jxBrowserVersion"
            win64.get() shouldBe "$group:jxbrowser-win64:$jxBrowserVersion"
            javafx.get() shouldBe "$group:jxbrowser-javafx:$jxBrowserVersion"
            macArm.get() shouldBe "$group:jxbrowser-mac-arm:$jxBrowserVersion"
            linux64.get() shouldBe "$group:jxbrowser-linux64:$jxBrowserVersion"
            linuxArm.get() shouldBe "$group:jxbrowser-linux64-arm:$jxBrowserVersion"
            crossPlatform.get() shouldBe "$group:jxbrowser-cross-platform:$jxBrowserVersion"
        }
    }

    @Test
    fun `maven repository is set correctly`() {
        val customRepository = "https://my.custom.repository"
        extension.repository = customRepository
        mavenRepositoryUrls() shouldContain customRepository

        extension.includePreviewBuilds()
        project.evaluationDependsOn(":")
        mavenRepositoryUrls() shouldContain EAP_REPOSITORY_URL
    }

    private fun mavenRepositoryUrls() =
        project.repositories
            .filterIsInstance(MavenArtifactRepository::class.java)
            .map { it.url.toString() }
}
