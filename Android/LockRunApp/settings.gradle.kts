pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        includeBuild("build-logic")
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

rootProject.name = "LockRunApp"
include(":app")
include(":libraries:network")
include(":libraries:network-contract")
include(":libraries:storage")
include(":libraries:storage-contract")
include(":ui_components")
include(":core")
include(":features:home")
