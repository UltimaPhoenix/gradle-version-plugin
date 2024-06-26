package io.github.ultimaphoenix.gradleversionplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

enum UpdateType {
    MAJOR, MINOR, PATCH
}

class VersionPlugin implements Plugin<Project> {

    private static void updateProjectVersion(Project project, String newVersion) {
        File buildFile = project.file("build.gradle")
        String content = buildFile.text

        content = content.replaceAll(/(?m)^version\s*=\s*['"].*['"]/, 'version = "' + newVersion + '"')

        println content
        buildFile.text = content

        println "Version updated to: $newVersion"
    }

    private static String computeNewVersion(String currentVersion, UpdateType updateType) {
        def versionParts = currentVersion.split('\\.')
        def major = versionParts[0].toInteger()
        def minor = versionParts[1].toInteger()
        def patch = versionParts[2].toInteger()
        def suffix = versionParts.size() > 3 ? versionParts[3] : ""

        switch (updateType) {
            case UpdateType.MAJOR:
                major += 1
                minor = 0
                patch = 0
                break
            case UpdateType.MINOR:
                minor += 1
                patch = 0
                break
            case UpdateType.PATCH:
                patch += 1
                break
        }


        return suffix ? "${major}.${minor}.${patch}.${suffix}" : "${major}.${minor}.${patch}"
    }

    void apply(Project project) {
        project.tasks.register('updateVersion') {
            group = "Release"
            description = "Update the project version to the specified."
            doLast {
                String newVersion = project.findProperty("newVersion") ?: '1.0.0' // Default version if not specified
                updateProjectVersion(project, newVersion)
            }
        }

        project.tasks.register('bumpMajor') {
            group = "Release"
            description = "Bump the Major release of the project."
            doLast {
                String newVersion = computeNewVersion("${project.version}", UpdateType.MAJOR)
                updateProjectVersion(project, newVersion)
            }
        }

        project.tasks.register('bumpMinor') {
            group = "Release"
            description = "Bump the Minor release of the project."
            doLast {
                String newVersion = computeNewVersion("${project.version}", UpdateType.MINOR)
                updateProjectVersion(project, newVersion)
            }
        }

        project.tasks.register('bumpPatch') {
            group = "Release"
            description = "Bump the Patch release of the project."
            doLast {
                String newVersion = computeNewVersion("${project.version}", UpdateType.PATCH)
                updateProjectVersion(project, newVersion)
            }
        }

        project.tasks.register('printVersion') {
            group = "Release"
            description = "Print the current project version."
            doLast {
                println "Current project version: ${project.version}"
            }
        }
    }
}

