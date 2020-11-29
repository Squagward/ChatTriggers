plugins {
    kotlin("jvm") version "1.3.72" apply false
    id("fabric-loom") version "0.4-SNAPSHOT" apply false
    id("com.replaymod.preprocess") version "f64d217"
}

group = "com.chattriggers.ctjs"

// Loom tries to find the active mixin version by recursing up to the root project and checking each project's
// compileClasspath and build script classpath (in that order). Since we've loom in our root project's classpath,
// loom will only find it after checking the root project's compileClasspath (which doesn't exist by default).
configurations.register("compileClasspath")

val shadowJar by tasks.creating(Copy::class) {
    into("$buildDir/libs")
}

subprojects {
    buildscript {
        repositories {
            maven("https://jitpack.io")
        }
    }

    afterEvaluate {
        val projectShadowJar = project.tasks.findByName("shadowJar")
        if (projectShadowJar != null && projectShadowJar.hasProperty("archivePath") && project.name != "core") {
            shadowJar.dependsOn(projectShadowJar)
            shadowJar.from(projectShadowJar.withGroovyBuilder { getProperty("archivePath") })
        }
    }
}

defaultTasks("shadowJar")

preprocess {
//    "1.16.2-fabric"(11602, "yarn") {
    "1.16.2"(11602, "srg") {
        "1.15.2"(11502, "srg") {
            "1.12.2"(11202, "srg", file("versions/1.15.2-1.12.2.txt")) {
                "1.8.9"(10809, "srg", file("versions/1.12.2-1.8.9.txt"))
            }
        }
    }
//    }
}
