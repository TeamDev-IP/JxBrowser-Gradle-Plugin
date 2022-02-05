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
 *         includePreviewBuilds = true
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
