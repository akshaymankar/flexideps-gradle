package in.axeman.gradle.plugins.flexideps

import org.gradle.api.initialization.Settings

class DependencySpecification extends Script{

    def compile(String path, String locator, String versionVariable) {
        def dependencyDir = new File([projectDir.canonicalPath, path].join(File.separator))
        dependencies.add new Dependency(settings, dependencyDir, locator, versionVariable)
    }

    def testCompile(String path, String locator, String versionVariable) {
        def dependencyDir = new File([projectDir.canonicalPath, path].join(File.separator))
        dependencies.add new Dependency(settings, dependencyDir, locator, versionVariable)
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
