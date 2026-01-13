rootProject.name = "WeatherTechTask"
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
include(":common:datastore")
include(":common:di-qualifiers")
include(":common:ui-theme")
include(":common:navigation")
