import org.gradle.internal.os.OperatingSystem

plugins {
    kotlin("multiplatform") version "1.7.20" //1.7.20 - last with no compile errors
}

group = "me.slava"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

val kglVersion = "0.1.11-2-g41155a8"

kotlin {
    val os = OperatingSystem.current()

    val nativeTarget = when {
        os.isMacOsX -> macosX64("native")
        os.isLinux -> linuxX64("native")
        os.isWindows -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
                runTask!!.workingDir("src/nativeMain/resources")
            }
        }

        compilations { "main" {
                dependencies {
                    //implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.klib"))))
                    //implementation(kotlin("stdlib"))

                    implementation("com.kgl:kgl-glfw:$kglVersion")
                    implementation("com.kgl:kgl-glfw-static:$kglVersion")
                    implementation("com.kgl:kgl-opengl:$kglVersion")
                    implementation("com.kgl:kgl-stb:$kglVersion")
                }

                //cinterops.create("bits")
                //cinterops.create("sockets")
            }
        }

    }

    sourceSets {
        val nativeMain by getting
    }

//    sourceSets.all {
//        languageSettings.apply {
//            enableLanguageFeature("InlineClasses")
//        }
//    }

}
