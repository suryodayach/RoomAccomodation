pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "EventPlanner"
include(":app")
include(":core:authentication")
include(":core:common")
include(":core:network")
include(":core:designsystem")
include(":core:data")
include(":core:model")
include(":core:ui")
include(":feature:authentication")
include(":feature:splash")
include(":feature:notes")
