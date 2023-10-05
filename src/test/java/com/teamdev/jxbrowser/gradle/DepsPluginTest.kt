package com.teamdev.jxbrowser.gradle

import com.teamdev.jxbrowser.gradle.DepsPlugin.Companion.EAP_REPOSITORY_URL
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.*

class DepsPluginTest {

    private val pluginId = "com.teamdev.jxbrowser"
    private lateinit var project: Project
    private lateinit var extension: DepsExtension

    @BeforeTest
    fun setUp() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply(pluginId)
        extension = project.extensions.getByType(DepsExtension::class.java)
    }

    @Test
    fun `extension is created properly`() {
        assertFalse(extension.includePreviewBuilds)
        assertEquals(Repository.NORTH_AMERICA, extension.repository)
    }

    @Test
    fun `dependency notations are resolved properly`() {
        val version = "1.0.0"
        extension.version = version

        assertEquals("com.teamdev.jxbrowser:jxbrowser:$version", extension.core().get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-javafx:$version", extension.javafx().get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-swing:$version", extension.swing().get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-swt:$version", extension.swt().get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-win64:$version", extension.win64().get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-win32:$version", extension.win32().get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-linux64:$version", extension.linux64().get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-linux64-arm:$version", extension.linuxArm().get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-mac:$version", extension.mac().get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-mac-arm:$version", extension.macArm().get())
        assertEquals("com.teamdev.jxbrowser:jxbrowser-cross-platform:$version", extension.crossPlatform().get())
    }

    @Test
    fun `maven repository is set correctly`() {
        val customRepository = "https://my.custom.repository"
        extension.repository = customRepository
        assertTrue(mavenRepositories().contain(customRepository))

        extension.includePreviewBuilds()
        project.evaluationDependsOn(":")
        assertTrue(mavenRepositories().contain(EAP_REPOSITORY_URL))
    }

    private fun mavenRepositories() = project.repositories.filterIsInstance(MavenArtifactRepository::class.java)
    private fun List<MavenArtifactRepository>.contain(url: String) = this.any { it.url.toString() == url }
}