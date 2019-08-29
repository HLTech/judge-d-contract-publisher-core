import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import pl.allegro.tech.build.axion.release.domain.TagNameSerializationConfig

plugins {
    kotlin("jvm") version "1.3.41"
    maven
    idea

    id("pl.allegro.tech.build.axion-release") version ("1.10.2")
}

scmVersion {
    tag(closureOf<TagNameSerializationConfig> {
        versionSeparator = ""
        prefix = "v"
    })
}

group = "com.hltech"
version = scmVersion.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.41")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")
    implementation("com.hltech:vaunt-core:1.0.24")
    implementation("io.github.openfeign:feign-core:9.7.0")
    implementation("io.github.openfeign:feign-jackson:9.7.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}