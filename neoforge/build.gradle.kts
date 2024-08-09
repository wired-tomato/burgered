plugins {
    id("com.github.johnrengelman.shadow")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

val generatedResources = project(":common").file("src/main/generated/resources")

loom {
    runs {
        val data by creating {
            data()
            programArgs("--all", "--mod", rootProject.property("mod_id").toString())
            programArgs("--output", generatedResources.absolutePath)
            programArgs("--existing", project(":common").file("src/main/resources").absolutePath)
        }
    }
}

val common: Configuration by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}
val shadowCommon: Configuration by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}
val developmentNeoForge: Configuration by configurations.getting

configurations {
    compileOnly { extendsFrom(common) }
    runtimeOnly { extendsFrom(common) }
    developmentNeoForge.extendsFrom(common)
}

repositories {
    maven("https://maven.neoforged.net/releases/")
    maven("https://thedarkcolour.github.io/KotlinForForge/") {
        content { includeGroup("thedarkcolour") }
    }
}

dependencies {
    neoForge("net.neoforged:neoforge:${rootProject.property("neoforge_version")}")
    implementation("thedarkcolour:kotlinforforge-neoforge:${rootProject.property("kotlin_for_forge_version")}") {
        exclude("net.neoforged.fancymodloader")
    }
    modImplementation("dev.architectury:architectury-neoforge:${rootProject.property("architectury_version")}")
    modApi("me.shedaniel.cloth:cloth-config-neoforge:${rootProject.property("cloth_config_version")}")
    modCompileOnly("dev.emi:emi-neoforge:${rootProject.property("emi_version")}:api")
    modRuntimeOnly("dev.emi:emi-neoforge:${rootProject.property("emi_version")}")

    common(project(":common", "namedElements")) {
        isTransitive = false
    }

    shadowCommon(project(":common", "transformProductionNeoForge")){
        isTransitive = false
    }

    implementation("com.akuleshov7:ktoml-core-jvm:0.5.2")
    shadowCommon("com.akuleshov7:ktoml-core-jvm:0.5.2") {
        isTransitive = false
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("mod_id", project.version)

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(mapOf(
                "mod_version" to project.version,
                "mod_id" to project.property("mod_id").toString(),
                "minecraft_version" to project.property("minecraft_version").toString(),
                "architectury_version" to project.property("architectury_version").toString(),
                "kotlin_for_forge_version" to project.property("kotlin_for_forge_version").toString()
            ))
        }
    }

    shadowJar {
        relocate("com.akuleshov7.ktoml", "${rootProject.property("maven_group")}.burgered.shaded.com.akuleshov7.ktoml")

        configurations = listOf(shadowCommon)
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile = shadowJar.get().archiveFile
        dependsOn(shadowJar)
        archiveClassifier.set(null as String?)
    }

    sourcesJar {
        val commonSources = project(":common").tasks.getByName<Jar>("sourcesJar")
        dependsOn(commonSources)
        from(commonSources.archiveFile.map { zipTree(it) })
    }
}

components.getByName("java") {
    this as AdhocComponentWithVariants
    this.withVariantsFromConfiguration(project.configurations["shadowRuntimeElements"]) {
        skip()
    }
}
