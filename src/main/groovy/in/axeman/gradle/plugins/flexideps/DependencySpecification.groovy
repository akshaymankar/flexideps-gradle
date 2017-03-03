package in.axeman.gradle.plugins.flexideps

import org.gradle.api.initialization.Settings

class DependencySpecification extends Script {

    DependencySpecification(Binding binding) {
        super(binding)
    }

    DependencySpecification() {}

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

    List<Dependency> getDependencies() {
        if(!this.binding.hasVariable('dependencies'))
           this.binding.setVariable('dependencies', [])
        this.binding.getVariable('dependencies') as List<Dependency>
    }

    private File getProjectDir() {
        this.binding.projectDir
    }

    @Override
    Object run() {
        throw new RuntimeException("lallaallalalal")
    }
}
