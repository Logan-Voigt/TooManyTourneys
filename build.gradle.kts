plugins {
    java
}

group = "dev.shadow"
version = "0.0.1"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.register<Copy>("deployToTest") {
    dependsOn(tasks.jar)
    from(tasks.jar.flatMap { it.archiveFile })
    into(layout.projectDirectory.dir("test-server/plugins"))
}