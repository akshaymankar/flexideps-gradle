package in.axeman.gradle.plugins.flexideps

import org.codehaus.groovy.control.CompilerConfiguration
import org.gradle.api.initialization.Settings

class DependencyFactory {
    static List<Dependency> getDependencies(Settings settings, File projectDir) {
        def flexidepsFile = new File([projectDir, 'flexideps.gradle'].join(File.separator))

        if(flexidepsFile.exists()) {
            def compilerConfiguration = new CompilerConfiguration()
            compilerConfiguration.scriptBaseClass = DependencySpecification.class.name
            def binding = new Binding(settings: settings, dependencies: [], projectDir: projectDir)
            def shell = new GroovyShell(this.classLoader, binding, compilerConfiguration)
            shell.evaluate(flexidepsFile)
            binding.dependencies
        }
        else []
    }
}
