/*
 * Copyright (c) 2000-2022 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
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
