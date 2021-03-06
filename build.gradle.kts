import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.util.Properties

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

val gprProps = Properties()
val gprPropFile = file("gpr.properties")
if (gprPropFile.exists()) {
    gprPropFile.inputStream().use {
        gprProps.load(it)
    }
}

repositories {
    maven {
        url = uri("https://jitpack.io")
    }
    mavenCentral()
    maven {
        name = "GitHub Packages"
        url = uri("https://maven.pkg.github.com/JellyBrick/alsong-kt")
        credentials {
            username = gprProps.getProperty("gpr.user") ?: System.getenv("USERNAME")
            password = gprProps.getProperty("gpr.key") ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-afterburner", version = "2.12.3")
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = "2.12.3")

    implementation(group = "com.github.kittinunf.fuel", name = "fuel", version = "2.3.1")
    implementation(group = "com.github.kittinunf.fuel", name = "fuel-jackson", version = "2.3.1")

    implementation(group = "be.zvz.alsong", name = "alsong-kt", version = "1.0.2")
    implementation(group = "com.github.rubenlagus", name = "TelegramBots", version = "5.2.0")
    implementation(group = "net.sf.javamusictag", name = "jid3lib", version = "0.5.4")
}

group = "pe.chalk.telegram.alyricbot"
version = "1.1"
description = "ALyricBot"

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to "pe.chalk.telegram.alyricbot.ALyricBot",
                "Implementation-Title" to description,
                "Implementation-Version" to archiveVersion
            )
        )
    }
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("app")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "pe.chalk.telegram.alyricbot.ALyricBot"))
        }
    }
}
