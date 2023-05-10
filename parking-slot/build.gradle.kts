plugins {
    alias(libs.plugins.kotlin.plugin.serialization)
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.mongodb.driver.sync)
    implementation(libs.logback.classic)

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.junit)
}
