@file:Suppress("LocalVariableName", "PropertyName", "SpellCheckingInspection")

architectury {
    common(mod.enabled_platforms)
}

loom {
    val accessWidenerFile: File = file("src/main/resources/${mod.id}.accesswidener")
    if (accessWidenerFile.exists()) {
        accessWidenerPath.set(accessWidenerFile)
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    modImplementation("net.fabricmc:fabric-loader:${mod.prop("fabric_loader")}")
}

tasks.test {
    useJUnitPlatform()
}
