package com.teamdev.jxbrowser.gradle;

import org.gradle.api.Project;
import org.gradle.api.provider.Provider;

import static com.teamdev.jxbrowser.gradle.Environment.*;
import static com.teamdev.jxbrowser.gradle.Repository.US;
import static java.lang.String.format;

/**
 * Returns JxBrowser artifacts as dependency notation strings.
 *
 * <p>Usage example:
 * <pre>
 *     dependencies {
 *         implementation(jxbrowser.swing())
 *         implementation(jxbrowser.currentPlatform())
 *     }
 *
 *     jxbrowser {
 *         // JxBrowser version.
 *         version = "7.21.2"
 *
 *         // Use JxBrowser repository at specific location. It's the US by default.
 *         repositoryLocation = Repository.US
 *
 *         // Or use your custom repository. If set, this field is preferred.
 *         repositoryUrl = "https://my.custom.repository"
 *
 *         // Include JxBrowser EAP repository.
 *         includePreviewBuilds()
 *     }
 * </pre>
 */
public abstract class DepsConfiguration {

    private final static String GROUP = "com.teamdev.jxbrowser";

    /**
     * A version of the JxBrowser.
     *
     * <p>This is a mandatory field.
     */
    public String version;

    /**
     * The preferred location of the JxBrowser repository.
     */
    public Repository repositoryLocation = US;

    /**
     * The URL of the repository.
     *
     * <p>If set, this property overrides {@link #repositoryLocation}.
     */
    public String repositoryUrl;

    /**
     * Indicates whether the repository with the preview builds should be added to the project.
     */
    boolean includePreviewBuilds;

    private Project project;

    /**
     * Adds a JxBrowser EAP repository to the project.
     */
    public void includePreviewBuilds() {
        includePreviewBuilds = true;
    }

    /**
     * Returns a dependency notation for the {@code jxbrowser}, an artifact with the core API of the library.
     */
    public Provider<String> core() {
        return project.getProviders().provider(() -> format("%s:jxbrowser:%s", GROUP, version));
    }

    /**
     * Returns a dependency notation for the {@code jxbrowser-javafx}, an artifact with JavaFX integration.
     */
    public Provider<String> javafx() {
        return artifact("javafx");
    }

    /**
     * Returns a dependency notation for the {@code jxbrowser-swing}, an artifact with Swing integration.
     */
    public Provider<String> swing() {
        return artifact("swing");
    }

    /**
     * Returns a dependency notation for the {@code jxbrowser-swt}, an artifact with SWT integration.
     */
    public Provider<String> swt() {
        return artifact("swt");
    }

    /**
     * Returns a dependency notation for the {@code jxbrowser-win64}, an artifact with Windows 64-bit binaries.
     */
    public Provider<String> win64() {
        return artifact("win64");
    }

    /**
     * Returns a dependency notation for the {@code jxbrowser-win32}, an artifact with Windows 32-bit binaries.
     */
    public Provider<String> win32() {
        return artifact("win32");
    }

    /**
     * Returns a dependency notation for the {@code jxbrowser-linux64}, an artifact with Linux 64-bit binaries.
     */
    public Provider<String> linux64() {
        return artifact("linux64");
    }

    /**
     * Returns a dependency notation for the {@code jxbrowser-linux64-arm}, an artifact with Linux 64-bit ARM binaries.
     */
    public Provider<String> linuxArm() {
        return artifact("linux64-arm");
    }

    /**
     * Returns a dependency notation for the {@code jxbrowser-mac}, an artifact with macOS binaries.
     */
    public Provider<String> mac() {
        return artifact("mac");
    }

    /**
     * Returns a dependency notation for the {@code jxbrowser-mac-arm}, an artifact with macOS ARM binaries.
     */
    public Provider<String> macArm() {
        return artifact("mac-arm");
    }

    /**
     * Detects the current platform and returns a dependency notation for the corresponding module with binaries.
     */
    public Provider<String> currentPlatform() {
        if (isX64Bit()) {
            if (isWindows()) {
                return win64();
            }
            if (isLinux()) {
                return linux64();
            }
            if (isMac()) {
                return mac();
            }
        }
        if (is32Bit() && isWindows()) {
            return win32();
        }
        if (isArm()) {
            if (isLinux()) {
                return linuxArm();
            }
            if (isMac()) {
                return macArm();
            }
        }
        throw new IllegalStateException("Failed to detect current platform.");
    }

    private Provider<String> artifact(String suffix) {
        return project.getProviders().provider(() -> format("%s:jxbrowser-%s:%s", GROUP, suffix, version));
    }

    void project(Project project) {
        this.project = project;
    }
}
