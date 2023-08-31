plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "ir.syrent.playground"
version = findProperty("version")!!

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")

    maven("https://oss.sonatype.org/content/groups/public/")

    // Cloud SNAPSHOT (Dev repository)
    maven("https://repo.masmc05.dev/repository/maven-snapshots/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("cloud.commandframework:cloud-paper:tooltips-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    runServer {
        minecraftVersion("1.20.1")
        serverJar(file("run/paper-1.16.5-794.jar"))
        setJvmArgs(listOf("-DPaper.IgnoreJavaVersion=true"))
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}_${project.version}.jar")
        exclude("META-INF/**")
        from("LICENSE")
        minimize()
    }

    build {
        dependsOn(shadowJar)
    }

    processResources {
        filesMatching(listOf("**plugin.yml", "**plugin.json")) {
            expand(
                "version" to project.version as String,
                "name" to rootProject.name,
                "description" to project.description
            )
        }
    }
}