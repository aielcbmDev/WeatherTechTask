import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.secrets.gradle.plugin)
    alias(libs.plugins.composeHotReload)
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
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.koin.android)
            implementation(libs.koin.core)
        }
        commonMain.dependencies {
            implementation(project(":feature-weather:weatherApp"))
            implementation(project(":common:di-qualifiers"))
            implementation(project(":common:ui-theme"))
            implementation(project(":common:navigation"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.androidx.nav3.ui)
            implementation(libs.androidx.lifecycle.viewmodel.nav3)
        }
        commonTest.dependencies {
            implementation(libs.kotlinx.test)
        }
    }
}
android {
    namespace = "com.something.volkswagentechtask"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.something.volkswagentechtask"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        val apiKey = project.findProperty("WEATHER_API_KEY") ?: "\"DEFAULT_API_KEY\""
        buildConfigField("String", "WEATHER_API_KEY", "$apiKey")
    }
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}

secrets {
    // To add your own API key to this project:
    // 1. If the secrets.properties file does not exist, create it in the same folder as the local.properties file.
    // 2. Add this line, where YOUR_API_KEY is your API key:
    //        WEATHER_API_KEY=YOUR_API_KEY
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"
}

tasks.register("allUnitTests") {
    group = "verification"
    description = "Runs all specified module tests dynamically"

    val testTasks = gradle.rootProject.subprojects.mapNotNull {
        println("modulePath: ${it.path}")
        val task = project.findProject(":${it.path}")?.tasks?.findByName("allTests")
        println("task: $task")
        task
    }

    println("testTasks: $testTasks")
    dependsOn(testTasks)
}
