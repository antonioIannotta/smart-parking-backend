import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import java.util.*

val projectGroup: String by project
val projectVersion: String by project

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.ktor.plugin) apply false
    alias(libs.plugins.kotlin.plugin.serialization) apply false
}

subprojects {
    repositories {
        mavenCentral()
    }
    apply(plugin = rootProject.libs.plugins.kotlin.jvm.get().pluginId)
    apply(plugin = rootProject.libs.plugins.buildconfig.get().pluginId)
    extensions.getByType<JavaPluginExtension>().apply {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    // CONFIG
    extensions.getByType<BuildConfigExtension>().apply {
        val applicationProperties = Properties().apply {
            load(file("${rootDir.path}/application.properties").inputStream())
        }
        val userDbConnectionString: String by applicationProperties
        buildConfigField("String", "USER_DB_CONNECTION_STRING", "\"$userDbConnectionString\"")
        val parkingSlotDbConnectionString: String by applicationProperties
        buildConfigField("String", "PARKING_SLOT_DB_CONNECTION_STRING", "\"$parkingSlotDbConnectionString\"")
        val hashingSecret: String by applicationProperties
        buildConfigField("String", "HASHING_SECRET", "\"$hashingSecret\"")
    }

    // TEST
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    // PUBLISHING
    group = projectGroup
    version = projectVersion

}
