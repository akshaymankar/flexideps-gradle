package in.axeman.gradle.plugins.flexideps

import org.gradle.internal.impldep.org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class DependencySpecificationTest extends Specification {
    @Rule final TemporaryFolder temporaryFolder = new TemporaryFolder()

    def "should initiate with empty dependencies"() {
        given:
        def specification = new DependencySpecification(new Binding())

        expect:
        specification.dependencies == []
    }

    def "should add a compile dependency"() {
        given:
        temporaryFolder.create()
        def projectDir = temporaryFolder.newFolder()
        def binding = new Binding(projectDir: projectDir, settings: null)
        def specification = new DependencySpecification(binding)

        when:
        specification.compile('foo', 'bar', 'baz')

        then:
        specification.dependencies == [new Dependency(null, new File("${projectDir.canonicalPath}/foo"), "bar", "baz", "compile")]
    }

    def "should add more than one compile dependencies"() {
        given:
        temporaryFolder.create()
        def projectDir = temporaryFolder.newFolder()
        def binding = new Binding(projectDir: projectDir, settings: null)
        def specification = new DependencySpecification(binding)

        when:
        specification.with {
            compile 'foo', 'bar', 'baz'
            compile 'foo1', 'bar1', 'baz1'
        }

        then:
        def expectedDependency1 = new Dependency(null, new File("${projectDir.canonicalPath}/foo"), "bar", "baz", "compile")
        def expectedDependency2 = new Dependency(null, new File("${projectDir.canonicalPath}/foo1"), "bar1", "baz1", "compile")
        specification.dependencies == [expectedDependency1, expectedDependency2]
    }

    def "should add a testCompile dependency"() {
        given:
        temporaryFolder.create()
        def projectDir = temporaryFolder.newFolder()
        def binding = new Binding(projectDir: projectDir, settings: null)
        def specification = new DependencySpecification(binding)

        when:
        specification.testCompile('foo', 'bar', 'baz')

        then:
        specification.dependencies == [new Dependency(null, new File("${projectDir.canonicalPath}/foo"), "bar", "baz", "testCompile")]
    }

    def "should add a compile and a testCompile dependencies"() {
        given:
        temporaryFolder.create()
        def projectDir = temporaryFolder.newFolder()
        def binding = new Binding(projectDir: projectDir, settings: null)
        def specification = new DependencySpecification(binding)

        when:
        specification.with {
            compile 'foo', 'bar', 'baz'
            testCompile 'foo1', 'bar1', 'baz1'
        }

        then:
        def expectedDependency1 = new Dependency(null, new File("${projectDir.canonicalPath}/foo"), "bar", "baz", "compile")
        def expectedDependency2 = new Dependency(null, new File("${projectDir.canonicalPath}/foo1"), "bar1", "baz1", "testCompile")
        specification.dependencies == [expectedDependency1, expectedDependency2]
    }
}
