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

/**
 * Provides the details about the current operating system and JVM architecture.
 */
internal object Environment {

    private val ARCH_X64 = setOf("amd64", "x86_64")
    private val ARCH_X86 = setOf("x86", "i386")
    private val ARCH_ARM = setOf("aarch64", "arm")

    /**
     * Returns `true` if the current operating system is Windows.
     */
    fun isWindows() = osName().startsWith("Windows")

    /**
     * Returns `true` if the current operating system is macOS.
     */
    fun isMac() = osName().startsWith("Mac")

    /**
     * Returns `true` if the current operating system is Linux.
     */
    fun isLinux() = osName().startsWith("Linux")

    /**
     * Indicates whether the current JVM's architecture is 64-bit.
     */
    fun isX64Bit() = jvmArch() in ARCH_X64

    /**
     * Indicates whether the current JVM's architecture is 32-bit.
     */
    fun is32Bit() = jvmArch() in ARCH_X86

    /**
     * Indicates whether the current JVM's architecture is ARM.
     */
    fun isArm() = jvmArch() in ARCH_ARM

    /**
     * Returns the name of the current operating system.
     */
    fun osName() = System.getProperty("os.name")

    /**
     * Returns the current JVM architecture.
     */
    fun jvmArch() = System.getProperty("os.arch")
}
