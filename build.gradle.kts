import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom") version "1.7-SNAPSHOT"
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    alias(libs.plugins.minotaur)
    `maven-publish`
}

group = project.properties["maven_group"]!!
version = project.properties["mod_version"]!!
base.archivesName.set(project.properties["archives_base_name"] as String)
description = "Generic Gun Mod #17"
val modid = project.properties["modid"]!! as String

repositories {
    mavenCentral()
    maven("https://maven.wiredtomato.net/releases/")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://api.modrinth.com/maven")
    maven("https://maven.quiltmc.org/repository/release/")
    maven("https://maven.isxander.dev/releases") {
        name = "Xander Maven"
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(variantOf(libs.quilt.mappings) { classifier("intermediary-v2") })
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.kt)

    modImplementation(libs.fabric.api)
    modCompileOnly(variantOf(libs.emi) { classifier("api") })
    modRuntimeOnly(libs.emi)
    modImplementation(libs.modmenu)
    modImplementation(include(libs.farrow.get())!!)
    modImplementation(include(libs.midnightlib.get())!!)
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create(modid) {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])
        }
    }
}

fabricApi.configureDataGeneration()

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    val targetJavaVersion = 21
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }

    withType<KotlinCompile>().all {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(targetJavaVersion).toString()))
        withSourcesJar()
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${project.base.archivesName.get()}"}
        }
    }
}

publishing {
    publications.create<MavenPublication>(modid) {
        groupId = project.group.toString()
        artifactId = project.name.lowercase()
        val projectVersion = project.version.toString()
        version = if (projectVersion.contains("beta")) "$projectVersion-SNAPSHOT" else projectVersion

        from(components["java"])
    }

    repositories {
        maven("https://maven.wiredtomato.net/snapshots") {
            name = "wtRepo"
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}
