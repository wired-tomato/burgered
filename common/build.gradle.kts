plugins {
    kotlin("plugin.serialization")
}

architectury {
    common(rootProject.property("enabled_platforms").toString().split(","))
}

loom {
    //accessWidenerPath.set(file("src/main/resources/burgered.accesswidener"))
}

sourceSets {
    main {
        resources {
            srcDir("src/main/generated/resources")
        }
    }
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric_loader_version")}")
    modApi("dev.architectury:architectury:${rootProject.property("architectury_version")}")
    modApi("me.shedaniel.cloth:cloth-config:15.0.128")
}
