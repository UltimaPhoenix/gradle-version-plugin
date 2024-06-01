package io.github.ultimaphoenix.gradleversionplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class VersionPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.tasks.register('updateVersion') {
            group = "Release"
            description = "Update the project version to the specified."
            doLast {
                String newVersion = project.findProperty("newVersion") ?: '1.0.0' // Default version if not specified

                File buildFile = project.file("build.gradle")
                String content = buildFile.text

                content = content.replaceAll(/^version *= *['"].*['"]/, 'version = "' + newVersion + '"')

                buildFile.text = content

                println "Version updated to: $newVersion"
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

