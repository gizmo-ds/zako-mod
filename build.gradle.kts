@file:Suppress("PropertyName", "SpellCheckingInspection", "UnstableApiUsage")

import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    java
    id("architectury-plugin").version("3.4-SNAPSHOT")
    id("dev.architectury.loom").version("1.10-SNAPSHOT").apply(false)
    id("com.github.johnrengelman.shadow").version("8.1.1").apply(false)
    id("co.uzzu.dotenv.gradle").version("4.0.0")
    id("maven-publish")
}

architectury {
    minecraft = mod.minecraft_version
}

allprojects {
    group = mod.group
    version = mod.version
}

subprojects {
    apply(plugin = "architectury-plugin")
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "maven-publish")

    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")

    loom.silentMojangMappingsLicense()

    base.archivesName.set("${mod.id}-${project.name}")

    repositories {
        maven("https://cursemaven.com") {
            content { includeGroup("curse.maven") }
        }
        maven("https://api.modrinth.com/maven") {
            content { includeGroup("maven.modrinth") }
        }
        maven("https://maven.aika.dev/releases") {
            name = "AIKA Repository"
        }
    }

    dependencies {
        "minecraft"("net.minecraft:minecraft:${mod.minecraft_version}")
        "mappings"(loom.layered {
            officialMojangMappings()
        })

        compileOnly("org.projectlombok:lombok:1.18.38")
        annotationProcessor("org.projectlombok:lombok:1.18.38")
    }

    java {
        withSourcesJar()

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    tasks.processResources {
        from(rootProject.file("LICENSE"))
        from(rootProject.file("third-party-licenses")) { into("third-party-licenses") }
    }

    publishing {
        publications {
            create<MavenPublication>("AIKA") {
                artifactId = base.archivesName.get()
                version = "${mod.minecraft_version}-${mod.version}"

                from(components["java"])
            }
        }

        repositories {
            val mavenUsername: String = env.fetch("MAVEN_USERNAME", "").trim()
            val mavenToken: String = env.fetch("MAVEN_TOKEN", "").trim()
            if (mavenUsername.isNotEmpty() && mavenToken.isNotEmpty()) {
                maven {
                    name = "AIKA"
                    url = if (mod.version.endsWith("-SNAPSHOT"))
                        uri("https://maven.aika.dev/snapshots")
                    else uri("https://maven.aika.dev/releases")

                    credentials(PasswordCredentials::class) {
                        username = mavenUsername
                        password = mavenToken
                    }
                    authentication { create<BasicAuthentication>("basic") }
                }
            }
        }
    }
}