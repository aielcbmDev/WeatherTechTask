import dev.mokkery.gradle.mokkery

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.mokkeryPlugin)
    alias(libs.plugins.kotlinAllOpen)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    compilerOptions {
        // Removes the following warning when executing unit tests:
        //
        // 'expect'/'actual' classes (including interfaces, objects, annotations, enums,
        // and 'actual' typealiases) are in Beta. Consider using the '-Xexpect-actual-classes'
        // flag to suppress this warning.
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.charly.core"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withHostTestBuilder {
            sourceSetTreeName = "test"
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "androidDeviceTest"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "coreKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            // Add KMP dependencies here
            implementation(project(":feature-weather:domain"))
            implementation(project(":feature-weather:networking"))
            implementation(project(":feature-weather:database"))
            implementation(project(":common:datastore"))
            implementation(project(":common:di-qualifiers"))

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        commonTest.dependencies {
            implementation(mokkery("coroutines"))
            implementation(libs.kotlinx.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            // Add Android-specific dependencies here. Note that this source set depends on
            // commonMain by default and will correctly pull the Android artifacts of any KMP
            // dependencies declared in commonMain.
        }

        getByName("androidDeviceTest").dependencies {
            implementation(libs.androidx.runner)
            implementation(libs.androidx.core)
            implementation(libs.androidx.testExt.junit)
        }

        iosMain.dependencies {
            // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
            // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
            // part of KMP’s default source set hierarchy. Note that this source set depends
            // on common by default and will correctly pull the iOS artifacts of any
            // KMP dependencies declared in commonMain.
        }
    }
}

// This check might require adjustment depending on your project type and the tasks that you use.
// Works with "*Test", "check", "*allTests" and "allUnitTests" tasks from Multiplatform projects.
fun isTestingTask(name: String) = name.endsWith("Test") || name.contentEquals("check")
        || name.endsWith("allTests") || name.contentEquals("allUnitTests")

val isTesting = gradle
    .startParameter
    .taskNames
    .any(::isTestingTask)

if (isTesting) {
    allOpen {
        annotation("com.charly.core.OpenClassForMocking")
    }
}
