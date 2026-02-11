plugins {
    `java-library`
    `application`
    id("com.gradleup.shadow") version "8.3.9"
}

application {
    mainClass.set("com.wrlus.jadx.McpServerMain")
}

dependencies {
	implementation("io.github.skylot:jadx-core:1.5.3")
	implementation("io.github.skylot:jadx-dex-input:1.5.3")
	implementation("io.github.skylot:jadx-java-input:1.5.3")
	implementation("io.github.skylot:jadx-java-convert:1.5.3")
	implementation("io.github.skylot:jadx-smali-input:1.5.3")
	implementation("com.google.code.gson:gson:2.13.2")
	implementation("io.javalin:javalin:6.7.0")
	implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("org.ow2.asm:asm:9.9")
}

configurations.configureEach {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.ow2.asm") {
            useVersion("9.9")
            because("ASM 9.9 is required to parse Java 25 class files")
        }
    }
}

repositories {
    mavenCentral()
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
    google()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

version = System.getenv("VERSION") ?: "dev"

tasks {
    withType(Test::class) {
        useJUnitPlatform()
    }
}
