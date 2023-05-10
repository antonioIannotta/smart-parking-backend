plugins {
    alias(libs.plugins.kotlin.plugin.serialization)
}

dependencies {
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.mongodb.driver.sync)
    implementation(libs.logback.classic)

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.junit)
    testImplementation(libs.ktor.server.test.host)
}
