@file:Suppress("PropertyName", "UnstableApiUsage", "SpellCheckingInspection")

plugins {
    id("com.github.johnrengelman.shadow")
    id("com.modrinth.minotaur").version("2.+")
    id("net.darkhax.curseforgegradle").version("1.+")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

val common: Configuration by configurations.creating
val shadowBundle: Configuration by configurations.creating
val developmentNeoForge: Configuration by configurations.getting

configurations {
    compileOnly.configure { extendsFrom(common) }
    runtimeOnly.configure { extendsFrom(common) }
    developmentNeoForge.extendsFrom(common)

    shadowBundle.isCanBeResolved = true
    shadowBundle.isCanBeConsumed = false
}

repositories {
    maven("https://maven.neoforged.net/releases") {
        name = "NeoForged"
    }
}

dependencies {
    neoForge("net.neoforged:neoforge:${mod.prop("neoforge_version")}")

    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowBundle(project(path = ":common", configuration = "transformProductionNeoForge"))
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/neoforge.mods.toml") {
            expand("version" to project.version)
        }
        from(rootProject.file("assets/logo.png")) {
            rename { "${mod.id}-logo.png" }
        }
    }

    shadowJar {
        exclude("architectury.common.json")

        configurations = listOf(shadowBundle)
        archiveClassifier.set("dev-shadow")

        mergeServiceFiles()
    }

    remapJar {
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        archiveAppendix.set(mod.minecraft_version)
        archiveVersion.set(mod.version)
        dependsOn(shadowJar)
    }
}
