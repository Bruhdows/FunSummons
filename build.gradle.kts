plugins {
    id("java")
    id("io.freefair.lombok") version "9.0.0"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

group = "com.bruhdows"
version = "1.0"

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    runServer {
        minecraftVersion("1.21.8")

        downloadPlugins {
            modrinth("viaversion", "5.7.1")
            modrinth("viabackwards", "5.7.1")
            modrinth("fastasyncworldedit", "2.15.0")
        }

        jvmArgs("-Dcom.mojang.eula.agree=true")
    }
}