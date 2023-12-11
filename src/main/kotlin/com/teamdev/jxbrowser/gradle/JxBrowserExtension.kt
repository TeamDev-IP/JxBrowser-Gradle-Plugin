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

import com.teamdev.jxbrowser.gradle.Environment.is32Bit
import com.teamdev.jxbrowser.gradle.Environment.isArm
import com.teamdev.jxbrowser.gradle.Environment.isLinux
import com.teamdev.jxbrowser.gradle.Environment.isMac
import com.teamdev.jxbrowser.gradle.Environment.isWindows
import com.teamdev.jxbrowser.gradle.Environment.isX64Bit
import com.teamdev.jxbrowser.gradle.Environment.jvmArch
import com.teamdev.jxbrowser.gradle.Environment.osName
import org.gradle.api.Project
import org.gradle.api.provider.Provider

/**
 * A Gradle plugin extension used for configuring and managing
 * JxBrowser dependencies within your Gradle project.
 *
 * It provides options for specifying the JxBrowser version, repository location,
 * and various JxBrowser dependencies based on your project's needs.
 */
public open class JxBrowserExtension(private val project: Project) {
    /**
     * A version of the JxBrowser.
     *
     * This is a mandatory field.
     */
    public var version: String = ""

    /**
     * The preferred location of the JxBrowser repository.
     */
    public var repository: String = Repository.NORTH_AMERICA

    /**
     * Indicates whether the repository with the preview builds
     * should be added to the project.
     */
    public var includePreviewBuilds: Boolean = false

    /**
     * Adds a repository with JxBrowser preview builds to the project.
     */
    public fun includePreviewBuilds() {
        includePreviewBuilds = true
    }

    /**
     * Returns a dependency notation for the `jxbrowser`,
     * an artifact with the core API of the library.
     */
    public val core: Provider<String> = artifact("core")

    /**
     * Returns a dependency notation for the `jxbrowser-javafx`,
     * an artifact with JavaFX integration.
     */
    public val javafx: Provider<String> = artifact("javafx")

    /**
     * Returns a dependency notation for the `jxbrowser-swing`,
     * an artifact with Swing integration.
     */
    public val swing: Provider<String> = artifact("swing")

    /**
     * Returns a dependency notation for the `jxbrowser-swt`,
     * an artifact with SWT integration.
     */
    public val swt: Provider<String> = artifact("swt")

    /**
     * Returns a dependency notation for the `jxbrowser-win64`,
     * an artifact with Chromium Windows 64-bit binaries.
     */
    public val win64: Provider<String> = artifact("win64")

    /**
     * Returns a dependency notation for the `jxbrowser-win32`,
     * an artifact with Chromium Windows 32-bit binaries.
     */
    public val win32: Provider<String> = artifact("win32")

    /**
     * Returns a dependency notation for the `jxbrowser-linux64`,
     * an artifact with Chromium Linux 64-bit binaries.
     */
    public val linux64: Provider<String> = artifact("linux64")

    /**
     * Returns a dependency notation for the `jxbrowser-linux64-arm`,
     * an artifact with Chromium Linux 64-bit ARM binaries.
     */
    public val linuxArm: Provider<String> = artifact("linux64-arm")

    /**
     * Returns a dependency notation for the `jxbrowser-mac`,
     * an artifact with Chromium macOS (Intel) binaries.
     */
    public val mac: Provider<String> = artifact("mac")

    /**
     * Returns a dependency notation for the `jxbrowser-mac-arm`,
     * an artifact with Chromium macOS ARM (Apple Silicon) binaries.
     */
    public val macArm: Provider<String> = artifact("mac-arm")

    /**
     * Returns a dependency notation for the `jxbrowser-cross-platform`,
     * an artifact that includes binaries for all supported platforms.
     */
    public val crossPlatform: Provider<String> = artifact("cross-platform")

    /**
     * Detects the current platform and returns a dependency notation
     * for the corresponding module with binaries.
     */
    public val currentPlatform: Provider<String> = currentPlatform()

    private fun currentPlatform(): Provider<String> {
        val platformMap =
            mapOf(
                Pair({ isX64Bit() && isWindows() }, win64),
                Pair({ isX64Bit() && isLinux() }, linux64),
                Pair({ isX64Bit() && isMac() }, mac),
                Pair({ is32Bit() && isWindows() }, win32),
                Pair({ isArm() && isLinux() }, linuxArm),
                Pair({ isArm() && isMac() }, macArm),
            )
        return platformMap.entries.firstOrNull { it.key() }?.value
            ?: project.providers.provider {
                val currentPlatform = "${osName()} ${jvmArch()}"
                val errorMessage = "The current $currentPlatform platform is not supported by JxBrowser $version."
                throw IllegalStateException(errorMessage)
            }
    }

    private fun artifact(shortName: String) =
        project.providers.provider {
            check(version.isNotBlank()) { "JxBrowser version is not specified." }
            if (shortName == "core") {
                "$GROUP:jxbrowser:$version"
            } else {
                "$GROUP:jxbrowser-$shortName:$version"
            }
        }

    private companion object {
        private const val GROUP = "com.teamdev.jxbrowser"
    }
}
