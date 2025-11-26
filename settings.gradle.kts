rootProject.name = "VolkswagenTechTask"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":feature-weather:weatherApp")
include(":feature-weather:domain")
include(":feature-weather:core")
include(":feature-weather:networking")
include(":feature-weather:database")
include(":feature-weather:datastore")
include(":di-qualifiers")
include(":ui-theme")
