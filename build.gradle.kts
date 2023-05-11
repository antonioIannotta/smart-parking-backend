import com.github.gmazzo.gradle.plugins.BuildConfigExtension
import org.danilopianini.gradle.mavencentral.DocStyle
import java.util.*

val projectGroup: String by project

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.ktor.plugin) apply false
    alias(libs.plugins.kotlin.plugin.serialization) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.publishOnCentral)
}

allprojects {
    group = projectGroup
    repositories {
        mavenCentral()
    }
    val version = rootProject.file("version.txt").readText().trim()
    this.version = version
}
subprojects {
    with(rootProject.libs.plugins) {
        apply(plugin = kotlin.jvm.get().pluginId)
        apply(plugin = buildconfig.get().pluginId)
        apply(plugin = publishOnCentral.get().pluginId)
        apply(plugin = dokka.get().pluginId)
    }


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

    publishOnCentral {
        projectLongName.set("Smart Parking Backend")
        projectDescription.set("The backend of smart parking application")
        licenseName.set("MIT License")
        licenseUrl.set("https://github.com/GZaccaroni/smart-parking-backend/blob/main/LICENSE")
        docStyle.set(DocStyle.JAVADOC)
    }
    publishing.publications.withType<MavenPublication>().configureEach {
        val artifactId: String by project
        groupId = "io.github.gzaccaroni.smartparking"
        this.version = version
        this.artifactId = artifactId
        pom {
            developers {
                developer {
                    name.set("Giulio Zaccaroni")
                    email.set("giulio.zaccaroni@studio.unibo.it")
                    url.set("https://www.giuliozaccaroni.me")
                    roles.set(mutableSetOf("developer"))
                }
            }
        }
    }
    signing {
        val signingKey = System.getenv("SIGNING_KEY")
        val signingPassword = System.getenv("SIGNING_PASSWORD")
        useInMemoryPgpKeys(signingKey, signingPassword)
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
}
