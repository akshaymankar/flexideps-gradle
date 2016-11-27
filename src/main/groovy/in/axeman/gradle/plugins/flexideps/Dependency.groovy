package in.axeman.gradle.plugins.flexideps

import org.gradle.api.Project
import org.gradle.api.initialization.Settings

class Dependency {
    private Settings settings
    private String path, locator, versionVariable
    private List<Dependency> dependencies
    private File projectDir

    Dependency(Settings settings, File projectDir, String locator, String versionVariable) {
        this.settings = settings
        this.projectDir =  projectDir
        this.locator = locator
        this.versionVariable = versionVariable

        //this.dependencies = DependencyFactory.getDependencies(settings, projectDir)
    }

    public File getProjectDir() {
        this.projectDir
    }

    public getName() {
        projectDir.name
    }

    def addDeps(Project rootProject) {
        rootProject.afterEvaluate {
            rootProject.dependencies.add('compile', rootProject.project(":${name}"))
        }
    }

    def include() {
        settings.include(":${name}")
        settings.project(":${name}").projectDir = projectDir
    }
}