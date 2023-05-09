val projectGroup: String by project
val projectVersion: String by project

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktor.plugin) apply false
    alias(libs.plugins.kotlin.plugin.serialization) apply false
}
subprojects {
    repositories {
        mavenCentral()
    }
    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)
    extensions.getByType<JavaPluginExtension>().apply {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }


    // TEST
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    // PUBLISHING
    group = projectGroup
    version = projectVersion

}
