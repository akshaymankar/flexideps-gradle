package in.axeman.gradle.plugins.flexideps

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.initialization.ProjectDescriptor
import org.gradle.api.initialization.Settings
import spock.lang.Specification

class DependencyTest extends Specification {
    Settings settings
    ProjectDescriptor projectDescriptor
    def setup() {
        settings = Mock()
        projectDescriptor = Mock()
    }

    def "should include a project as its directory name"() {
        given:
        def file = new File("../../bar/foo")
        def dependency = new Dependency(settings, file, "group:artifactId", "someVariable")

        when:
        dependency.include()

        then:
        1 * settings.include(":foo")
        1 * settings.project(":foo") >> projectDescriptor
        1 * projectDescriptor.setProjectDir(file)
    }

    def "should put itself as compile dependency on project"() {
        given:
        def file              = new File("../../bar/foo")
        def rootProject       = Mock(Project)
        def dependencyProject = Mock(Project)
        def dependencyHandler = Mock(DependencyHandler)

        def dependency = new Dependency(settings, file, "group:artifactId", "someVariable")

        when:
        dependency.addDeps(rootProject)

        then:
        1 * rootProject.afterEvaluate(_) >> {Closure x -> x.call()}
        1 * rootProject.project(':foo') >> dependencyProject
        1 * rootProject.dependencies >> dependencyHandler
        1 * dependencyHandler.add('compile', dependencyProject)
    }
}
