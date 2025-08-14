plugins {
    java
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("io.github.patrick.remapper") version "1.4.2"
    id("com.gradleup.shadow") version "8.3.3"
}

group = "it.jakegblp"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.destroystokyo.com/repository/maven-public/")
    maven("https://repo.skriptlang.org/releases")
}

dependencies {
    subprojects.forEach {
        if (it.path.startsWith(":nms:")) {
            runtimeOnly(project(it.path))
        } else if (it.path != ":nms") {
            implementation(project(it.path))
        }
    }
    compileOnly("com.github.SkriptLang:Skript:2.12.1")
    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:10.1.2")
    implementation("com.github.zafarkhaja:java-semver:0.10.2")
    implementation("org.jspecify:jspecify:1.0.0")
    compileOnly("org.jetbrains:annotations:20.1.0")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    relocate("dev.jorel.commandapi", "it.jakegblp.nms.libraries.commandapi")
    relocate("com.github.zafarkhaja.semver", "it.jakegblp.nms.libraries.semver")
}

tasks.named("build") {
    dependsOn("shadowJar")
}
tasks {
    runServer {
        minecraftVersion("1.21.4")
        downloadPlugins {
            url("https://github.com/SkriptLang/Skript/releases/download/2.12.1/Skript-2.12.1.jar")
        }
    }
}

subprojects {
    apply(plugin = "java")

    val specialCompileOnly by configurations.creating {
        isCanBeConsumed = false
        isCanBeResolved = true // so we can use it for compilation
        extendsFrom(configurations.compileOnly.get()) // inherits compileOnly behavior
    }

    val versionRegex = Regex("""\d+\.\d+(?:\.\d+)?""")
    val versionMatch = versionRegex.find(name)
    val mcVersion = versionMatch?.value ?: "1.21.8"
    var isNmsModule = false
    if (project.path.startsWith(":nms:") && project.name != "build") {
        isNmsModule = true
        if (!mcVersion.contains("1.17")) {
            apply(plugin = "io.papermc.paperweight.userdev")
            dependencies {
                paperweight.paperDevBundle("$mcVersion-R0.1-SNAPSHOT")
            }
        } else {
            apply(plugin = "io.github.patrick.remapper")
            dependencies {
                specialCompileOnly("org.spigotmc:spigot:$mcVersion-R0.1-SNAPSHOT")
                specialCompileOnly("io.papermc.paper:paper-api:$mcVersion-R0.1-SNAPSHOT")
            }
            project.tasks.named<io.github.patrick.gradle.remapper.RemapTask>("remap") {
                this.version = mcVersion
            }
            project.tasks.named("build") {
                dependsOn("remap")
            }
        }
    } else {
        dependencies {
            compileOnly("io.papermc.paper:paper-api:$mcVersion-R0.1-SNAPSHOT")
        }
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.destroystokyo.com/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    dependencies {
        if (name == "skript") {
            compileOnly("com.github.SkriptLang:Skript:2.12.1")
            compileOnly(project(":common"))
        }

        if (name == "factory" || isNmsModule) {
            compileOnly(project(":common"))
        }
        if (name == "factory") {
            rootProject.subprojects.forEach {
                if (it.path.startsWith(":nms:")) {
                    specialCompileOnly(project(it.path))
                }
            }
        }
        compileOnly("com.github.zafarkhaja:java-semver:0.10.2")
        compileOnly("io.netty:netty-all:4.1.87.Final")
        compileOnly("it.unimi.dsi:fastutil:8.5.15")
        compileOnly("org.jspecify:jspecify:1.0.0")
        compileOnly("org.jetbrains:annotations:20.1.0")
        compileOnly("org.projectlombok:lombok:1.18.36")
        annotationProcessor("org.projectlombok:lombok:1.18.36")
        testCompileOnly("org.projectlombok:lombok:1.18.36")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
    }

    tasks.compileJava {
        // Add special compile-only configuration to *only* the compile classpath
        classpath += specialCompileOnly
    }

    tasks.compileTestJava {
        // If you want it available for tests too
        classpath += specialCompileOnly
    }
}
