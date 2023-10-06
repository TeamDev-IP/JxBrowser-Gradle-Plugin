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

import org.gradle.api.Project
import org.gradle.api.provider.Provider

/**
 * A plugin that configures JxBrowser repository and helps with dependencies.
 *
 * Usage example:
 *
 * ```kotlin
 * dependencies {
 *     implementation(jxbrowser.swing())
 *     implementation(jxbrowser.currentPlatform())
 * }
 *
 * jxbrowser {
 *     version = "7.35.1"
 *
 *     // Use JxBrowser repository at specific location. It's North America by default.
 *     repository = Repository.NORTH_AMERICA
 *
 *     // Or use your custom repository.
 *     // repository = "https://my.custom.repository"
 *
 *     // Include JxBrowser EAP repository.
 *     includePreviewBuilds()
 * }
 * ```
 */
open class JxBrowserExtension(private val project: Project) {

    /**
     * A version of the JxBrowser.
     *
     * This is a mandatory field.
     */
    lateinit var version: String

    /**
     * The preferred location of the JxBrowser repository.
     */
    var repository = Repository.NORTH_AMERICA

    /**
     * Indicates whether the repository with the preview builds
     * should be added to the project.
     */
    var includePreviewBuilds = false

    /**
     * Adds a JxBrowser EAP repository to the project.
     */
    fun includePreviewBuilds() {
        includePreviewBuilds = true
    }

    /**
     * Returns a dependency notation for the `jxbrowser`,
     * an artifact with the core API of the library.
     */
    fun core(): Provider<String> = project.providers.provider { "$GROUP:jxbrowser:$version" }

    /**
     * Returns a dependency notation for the `jxbrowser-javafx`,
     * an artifact with JavaFX integration.
     */
    fun javafx(): Provider<String> = artifact("javafx")

    /**
     * Returns a dependency notation for the `jxbrowser-swing`,
     * an artifact with Swing integration.
     */
    fun swing(): Provider<String> = artifact("swing")

    /**
     * Returns a dependency notation for the `jxbrowser-swt`,
     * an artifact with SWT integration.
     */
    fun swt(): Provider<String> = artifact("swt")

    /**
     * Returns a dependency notation for the `jxbrowser-win64`,
     * an artifact with Windows 64-bit binaries.
     */
    fun win64(): Provider<String> = artifact("win64")

    /**
     * Returns a dependency notation for the `jxbrowser-win32`,
     * an artifact with Windows 32-bit binaries.
     */
    fun win32(): Provider<String> = artifact("win32")

    /**
     * Returns a dependency notation for the `jxbrowser-linux64`,
     * an artifact with Linux 64-bit binaries.
     */
    fun linux64(): Provider<String> = artifact("linux64")

    /**
     * Returns a dependency notation for the `jxbrowser-linux64-arm`,
     * an artifact with Linux 64-bit ARM binaries.
     */
    fun linuxArm(): Provider<String> = artifact("linux64-arm")

    /**
     * Returns a dependency notation for the `jxbrowser-mac`,
     * an artifact with macOS binaries.
     */
    fun mac(): Provider<String> = artifact("mac")

    /**
     * Returns a dependency notation for the `jxbrowser-mac-arm`,
     * an artifact with macOS ARM binaries.
     */
    fun macArm(): Provider<String> = artifact("mac-arm")

    /**
     * Returns a dependency notation for the `jxbrowser-cross-platform`,
     * an artifact that includes binaries for all supported platforms.
     */
    fun crossPlatform(): Provider<String> = artifact("cross-platform")

    /**
     * Detects the current platform and returns a dependency notation
     * for the corresponding module with binaries.
     */
    fun currentPlatform(): Provider<String> {
        if (Environment.isX64Bit()) {
            if (Environment.isWindows()) {
                return win64()
            }
            if (Environment.isLinux()) {
                return linux64()
            }
            if (Environment.isMac()) {
                return mac()
            }
        }
        if (Environment.is32Bit() && Environment.isWindows()) {
            return win32()
        }
        if (Environment.isArm()) {
            if (Environment.isLinux()) {
                return linuxArm()
            }
            if (Environment.isMac()) {
                return macArm()
            }
        }
        throw IllegalStateException("The current platform is not supported by JxBrowser.")
    }

    private fun artifact(suffix: String) = project.providers.provider { "$GROUP:jxbrowser-$suffix:$version" }

    companion object {
        private const val GROUP = "com.teamdev.jxbrowser"
    }
}
