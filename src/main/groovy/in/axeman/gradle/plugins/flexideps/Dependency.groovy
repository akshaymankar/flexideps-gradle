package in.axeman.gradle.plugins.flexideps

import groovy.transform.EqualsAndHashCode
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

@EqualsAndHashCode
class Dependency {
    private Settings settings
    private String path, locator, versionVariable
    private List<Dependency> dependencies
    private File projectDir
    private String configuration

    Dependency(Settings settings, File projectDir, String locator, String versionVariable, String configuration) {
        this.configuration = configuration
        this.settings = settings
        this.projectDir =  projectDir
        this.locator = locator
        this.versionVariable = versionVariable

        //this.dependencies = DependencyFactory.getDependencies(settings, projectDir)
    }

    def getName() {
        projectDir.name
    }

    def addDeps(Project rootProject) {
        rootProject.afterEvaluate {
            if(rootProject.hasProperty(versionVariable)) {
                rootProject.dependencies.add(configuration, "${locator}:${rootProject.properties[versionVariable]}")
            } else {
                rootProject.dependencies.add(configuration, rootProject.project(":${name}"))
            }
        }
    }

    def include() {
        if(!settings.hasProperty(versionVariable)) {
            settings.include(":${name}")
            settings.project(":${name}").projectDir = projectDir
        }
    }
}
