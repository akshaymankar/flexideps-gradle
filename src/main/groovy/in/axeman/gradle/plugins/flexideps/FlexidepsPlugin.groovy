package in.axeman.gradle.plugins.flexideps

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

class FlexidepsPlugin implements Plugin<Settings> {
    @Override
    void apply(Settings settings) {
        def dependencies = DependencyFactory.getDependencies(settings, settings.rootDir)
        dependencies*.include()
        settings.gradle.beforeProject { Project project ->
            if(project.rootProject == project)
                dependencies*.addDeps(project)
        }
    }
}
