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

import org.gradle.api.Plugin
import org.gradle.api.Project

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
class JxBrowserPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create(EXTENSION_NAME, JxBrowserExtension::class.java, project)
        project.addMavenRepositoryLocation(project.providers.provider { extension.repository })
        project.afterEvaluate {
            if (extension.includePreviewBuilds) {
                project.addMavenRepositoryLocation(EAP_REPOSITORY_URL)
            }
        }
    }

    private fun Project.addMavenRepositoryLocation(url: Any) = this.repositories.maven { it.setUrl(url) }

    companion object {
        const val EXTENSION_NAME = "jxbrowser"
        const val EAP_REPOSITORY_URL = "https://europe-maven.pkg.dev/jxbrowser/eaps"
    }
}
