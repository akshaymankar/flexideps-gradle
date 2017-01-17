package in.axeman.gradle.plugins.flexideps

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.internal.PluginUnderTestMetadataReading
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.*

class FlexidepsPluginTest extends Specification {
    @Rule final TemporaryFolder rootProjectDir = new TemporaryFolder()

    File rootProjectBuildFile, flexidepsFile, rootProjectSettingsFile

    def setup() {
        rootProjectBuildFile = rootProjectDir.newFile("build.gradle")
        rootProjectBuildFile << "apply plugin: 'java'\n"

        rootProjectSettingsFile = rootProjectDir.newFile("settings.gradle")
        rootProjectSettingsFile << """|buildscript {
                                      |  dependencies {
                                      |""".stripMargin()
        PluginUnderTestMetadataReading.readImplementationClasspath().each {
           rootProjectSettingsFile << "    classpath files('${it.canonicalPath}')\n"
        }
        rootProjectSettingsFile << """|  }
                                      |}
                                      |""".stripMargin()

        rootProjectSettingsFile << "apply plugin: 'flexideps'"

        flexidepsFile = rootProjectDir.newFile("flexideps.gradle")
    }

    def "should add projects as dependencies with given configurations"() {
        given:
        def subProject1Dir = new TemporaryFolder()
        def subProject2Dir = new TemporaryFolder()
        subProject1Dir.create()
        subProject2Dir.create()

        def subProject1BuildFile = subProject1Dir.newFile("build.gradle")
        subProject1BuildFile << "apply plugin: 'java'\n"
        def subProject2BuildFile = subProject2Dir.newFile("build.gradle")
        subProject2BuildFile << "apply plugin: 'java'\n"

        flexidepsFile << """|compile '../${subProject1Dir.root.name}', 'in.axeman:foo', 'fooVersion'
                            |testCompile '../${subProject2Dir.root.name}', 'in.axeman:bar', 'barVersion'
                            |""".stripMargin()

        when:
        def compileResult = GradleRunner.create()
                .withProjectDir(rootProjectDir.root)
                .withPluginClasspath()
                .withArguments('dependencies', '--configuration=compile')
                .build()

        def testCompileResult = GradleRunner.create()
                .withProjectDir(rootProjectDir.root)
                .withPluginClasspath()
                .withArguments('dependencies', '--configuration=testCompile')
                .build()

        then:
        def subProject1Description = "project :${subProject1Dir.root.name}"
        def subProject2Description = "project :${subProject2Dir.root.name}"

        compileResult.task(":dependencies").outcome == TaskOutcome.SUCCESS
        expect compileResult.output, stringContainsInOrder(['compile', subProject1Description]*.toString())
        expect compileResult.output, not(containsString(subProject2Description))

        testCompileResult.task(":dependencies").outcome == TaskOutcome.SUCCESS
        expect testCompileResult.output, stringContainsInOrder(['testCompile', subProject1Description, subProject2Description]*.toString())
    }
}
