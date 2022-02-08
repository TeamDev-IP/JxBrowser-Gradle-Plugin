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

/**
 * Provides the details about the current operating system and JVM architecture.
 */
final class Environment {

    /**
     * Returns {@code true} if the current operating system is Windows.
     */
    public static boolean isWindows() {
        return osName().startsWith("Windows");
    }

    /**
     * Returns {@code true} if the current operating system is macOS.
     */
    public static boolean isMac() {
        return osName().startsWith("Mac");
    }

    /**
     * Returns {@code true} if the current operating system is Linux.
     */
    public static boolean isLinux() {
        return osName().startsWith("Linux");
    }

    /**
     * Indicates whether the current JVM's architecture is 64-bit.
     */
    static boolean isX64Bit() {
        String arch = jvmArch();
        return "amd64".equals(arch) || "x86_64".equals(arch);
    }

    /**
     * Indicates whether the current JVM's architecture is 32-bit.
     */
    static boolean is32Bit() {
        String arch = jvmArch();
        return "x86".equals(arch) || "i386".equals(arch);
    }

    /**
     * Indicates whether the current JVM's architecture is ARM.
     */
    static boolean isArm() {
        String arch = jvmArch();
        return "aarch64".equals(arch) || "arm".equals(arch);
    }

    /**
     * Prevents instantiation of this utility class.
     */
    private Environment() {
    }

    /**
     * Obtains the value of the property via {@link System}.
     */
    private static String property(String name) {
        return System.getProperty(name);
    }

    private static String osName() {
        return property("os.name");
    }

    private static String jvmArch() {
        return property("os.arch");
    }
}
