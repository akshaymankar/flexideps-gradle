package in.axeman.gradle.plugins.flexideps

import org.gradle.api.initialization.Settings

class DependencySpecification extends Script {

    def compile(String path, String locator, String versionVariable) {
        add(path, locator, versionVariable, 'compile')
    }

    def testCompile(String path, String locator, String versionVariable) {
        add(path, locator, versionVariable, 'testCompile')
    }

    private void add(String path, String locator, String versionVariable, String configuration) {
        def dependencyDir = new File([projectDir.canonicalPath, path].join(File.separator))
        dependencies.add new Dependency(settings, dependencyDir, locator, versionVariable, configuration)
    }

    private Settings getSettings() {
        this.binding.settings
    }

    private List<Dependency> getDependencies() {
        this.binding.dependencies
    }

    private File getProjectDir() {
        this.binding.projectDir
    }

    @Override
    Object run() {
        throw new RuntimeException("lallaallalalal")
    }
}
