import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    id("maven-publish")
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}

group = "com.clauscode"
version = "1.0-SNAPSHOT"

val minestomVersion = "e713cf62a7"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Minestom:Minestom:$minestomVersion")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
}

publishing {
    publications {
    }
    repositories {
        maven {
            name = "JustGUI" //  optional target repository name
            url = URI("https://jitpack.io")
            credentials {
                username = "ClausCode"
                password = "my-password"
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}