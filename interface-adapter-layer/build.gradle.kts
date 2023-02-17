val projectGroup: String by project
val projectVersion: String by project

plugins {
    kotlin("jvm") version "1.8.0"
}

group = projectGroup
version = projectVersion

repositories {
    mavenCentral()
}
