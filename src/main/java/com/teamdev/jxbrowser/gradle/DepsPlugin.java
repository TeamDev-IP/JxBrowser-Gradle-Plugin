/*
 *  Copyright 2022, TeamDev. All rights reserved.
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

package com.teamdev.jxbrowser.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * A plugin that configures JxBrowser repository and helps with dependencies.
 *
 * <p>Usage example:
 * <pre>
 *     dependencies {
 *         implementation(jxbrowser.swing())
 *         implementation(jxbrowser.currentPlatform())
 *     }
 *
 *     jxbrowser {
 *         version = "7.21.2"
 *         // Use JxBrowser repository at specific location. It's the US by default.
 *         repositoryLocation = JxBrowserRepository.US
 *
 *         // Or use your custom repository. If set, this field is preferred.
 *         repositoryUrl = "https://my.custom.repository"
 *
 *         // Include JxBrowser EAP repository.
 *         includePreviewBuilds()
 *     }
 * </pre>
 */
public class DepsPlugin implements Plugin<Project> {

    public static final String EAP_REPOSITORY = "https://europe-maven.pkg.dev/jxbrowser/eaps";

    @Override
    public void apply(Project project) {
        DepsConfiguration extension = project.getExtensions().create("jxbrowser", DepsConfiguration.class);
        extension.project(project);

        project.getRepositories().maven(repository -> {
            repository.setUrl(project.getProviders().provider(() -> {
                if (extension.repositoryUrl != null) {
                    return extension.repositoryUrl;
                } else {
                    return extension.repositoryLocation.url();
                }
            }));
        });

        project.afterEvaluate(ignored -> {
            if (extension.includePreviewBuilds) {
                project.getRepositories().maven(repository -> {
                    repository.setUrl(EAP_REPOSITORY);
                });
            }
        });
    }
}
