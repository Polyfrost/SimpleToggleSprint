/*
 * PolySprint - Toggle sprint and sneak with a keybind.
 *  Copyright (C) 2023  Polyfrost
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

@file:Suppress("PropertyName")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.polyfrost.org/releases") // Adds the Polyfrost maven repository to get Polyfrost Gradle Toolkit
    }
    plugins {
        val pgtVersion = "0.6.5" // Sets the default versions for Polyfrost Gradle Toolkit
        id("org.polyfrost.multi-version.root") version pgtVersion
    }
}

val mod_name: String by settings

// Configures the root project Gradle name based on the value in `gradle.properties`
rootProject.name = mod_name
rootProject.buildFileName = "root.gradle.kts"

// Adds all of our build target versions to the classpath if we need to add version-specific code.
listOf(
    "1.8.9-forge", // Update this if you want to remove/add a version, along with `build.gradle.kts` and `root.gradle.kts`.
    "1.12.2-forge"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}