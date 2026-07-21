plugins {
    `kotlin-dsl`
    id("io.gitlab.arturbosch.detekt")
}

group = "ru.practicum.android.buildlogic"


dependencies {
    implementation(projects.gradleExt)

    implementation(libs.detekt.gradle.plugin)

    detektPlugins(libs.detekt.formatting)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
