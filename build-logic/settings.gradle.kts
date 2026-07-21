enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "build-logic"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("io.gitlab.arturbosch.detekt") version "1.23.8"
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

/**
 * renamed from 'gradle' to prevent IDE resolution conflict:
 * usages of "typesafe project accessors", e.g. `projects.gradle.someProject` was red in IDE
 * build was fine however
 */
include("gradle-ext")
include(":develop-properties")
include(":checks")
println("BUILD LOGIC DIR = ${rootDir}")
println("CATALOG EXISTS = ${file("../gradle/libs.versions.toml").exists()}")
