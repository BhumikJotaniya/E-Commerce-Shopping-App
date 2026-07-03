pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        repositories {
            maven { url = uri("https://www.jitpack.io" ) }
            maven {
                url = uri ("https://oss.sonatype.org/content/repositories/snapshots")
            }
        }
        gradlePluginPortal()
        mavenCentral()

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "JB MART"
include(":app")
 