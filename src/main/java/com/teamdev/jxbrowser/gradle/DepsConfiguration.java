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

package com.teamdev.jxbrowser.gradle;

import org.gradle.api.Project;
import org.gradle.api.provider.Provider;

import static com.teamdev.jxbrowser.gradle.Environment.*;
import static com.teamdev.jxbrowser.gradle.Repository.US;
import static java.lang.String.format;

/**
 * Configures JxBrowser repository and provides dependency notations for the
 * artifacts.
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
     * Returns a dependency notation for the {@code jxbrowser-cross-platform}, an artifact that includes
     * binaries for all supported platforms.
     */
    public Provider<String> crossPlatform() {
        return artifact("cross-platform");
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
        throw new IllegalStateException("The current platform is not supported by JxBrowser.");
    }

    private Provider<String> artifact(String suffix) {
        return project.getProviders().provider(() -> format("%s:jxbrowser-%s:%s", GROUP, suffix, version));
    }

    void project(Project project) {
        this.project = project;
    }
}
