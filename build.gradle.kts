import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import pl.allegro.tech.build.axion.release.domain.TagNameSerializationConfig

plugins {
    kotlin("jvm") version "1.3.41"
    signing
    idea
    java

    id("pl.allegro.tech.build.axion-release") version ("1.10.2")
    id("io.codearte.nexus-staging") version "0.20.0"
    id("de.marcphilipp.nexus-publish") version "0.2.0"
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

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    dependsOn.add(tasks.javadoc)
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

artifacts {
    archives(sourcesJar)
    archives(javadocJar)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)
            pom {
                name.set(project.name)
                description.set("Judge-D contract publisher core toolkit")
                url.set("https://github.com/HLTech/judge-d-contract-publisher-core")
                inceptionYear.set("2019")
                scm {
                    connection.set("scm:git:https://github.com/HLTech/judge-d-contract-publisher-core.git")
                    developerConnection.set("scm:git:git@github.com:HLTech/judge-d-contract-publisher-core.git")
                    url.set("https://github.com/HLTech/judge-d-contract-publisher-core.git")
                }
                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("matek2305")
                        name.set("Mateusz Urbański")
                        email.set("matek2305@gmail.com")
                    }
                }
            }
        }
    }
}

if (project.hasProperty("signing.keyId")) {
    signing {
        sign(publishing.publications["mavenJava"])
    }
}

nexusStaging {
    username = System.getenv("SONATYPE_USER")
    password = System.getenv("SONATYPE_PASSWORD")
    stagingProfileId = "8932a92dff8c84"
    numberOfRetries = 5
    delayBetweenRetriesInMillis = 60000
}