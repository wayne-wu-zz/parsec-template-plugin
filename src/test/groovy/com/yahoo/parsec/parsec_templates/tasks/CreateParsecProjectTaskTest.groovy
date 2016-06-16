package com.yahoo.parsec.parsec_templates.tasks

import com.yahoo.parsec.parsec_templates.ParsecTemplatesExtension
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author waynewu
 */
class CreateParsecProjectTaskTest extends Specification{

    @Rule
    TemporaryFolder folder = new TemporaryFolder()

    private Project project
    private Task task
    private ParsecTemplatesExtension pluginExtension

    def setup(){
        project = new ProjectBuilder().withProjectDir(folder.getRoot()).build()
        pluginExtension = project.extensions.create("parsecTemplate", ParsecTemplatesExtension)
        task = project.task('createTask', type:CreateParsecProjectTask)

        project.ext["artifactId"] = 'test_name'
        project.ext["groupId"] = 'test.group'
        project.ext["parent_dir"] = folder.getRoot() as String
    }

    def "all the basic directories and files should exists"(){

        when:
            task.create()

        then:
            assertFileExists 'test_name/src/main/java/test/group'
            assertFileExists 'test_name/src/test/java/test/group'
            assertFileExists 'test_name/src/main/rdl'
            assertFileExists 'test_name/src/main/webapp'
            assertFileExists 'test_name/src/main/resources'

            assertFilesExists('test_name',
                    (String[])["build.gradle", "README.md", "README.sh", "gradle.properties"])
    }


    def "extra template should generate based on given extension input"(){
        given:
            def file = folder.newFile("test.txt")
            file.write("This file is for testing")
            pluginExtension.extraTemplate = {
                'src' {
                    'main'{
                        'sample' {
                            'test.txt' template: file.getAbsolutePath()
                        }
                    }
                }
            }

        when:
            task.create()

        then:
            String filePath = 'test_name/src/main/sample/test.txt'
            assertFileExists 'test_name/src/main/sample'
            assertFileExists filePath
            assertFileContains folder.root, filePath, "This file is for testing"
    }


    def "build.gradle should apply from what the extension specifies"() {
        given:
            pluginExtension.applyFromPath = "no/such/path/just/testing"

        when:
            task.create()

        then:
            assertFileContains folder.root, 'test_name/build.gradle', "apply from: 'no/such/path/just/testing'"
    }

    @Unroll
    def "sample.rdl should or should not get generated based on extension"(){
        given:
            pluginExtension.createSampleRDL = createSampleRDL

        when:
            task.create()

        then:
            assertFileExists folder.root, 'test_name/src/main/rdl/sample.rdl', createSampleRDL

        where:
            createSampleRDL << [true, false]

    }


    /**
     * Asserts that the specified file exists
     *
     * @param root
     * @param path
     */
    protected void assertFileExists(File root = folder.root, String path, boolean assertion = true){
        assert assertion == new File(root, path).exists()
    }

    protected void assertFilesExists(File root = folder.root, String projectName, String[] files){
        for (file in files){
            assertFileExists(root, "${projectName}/${file}")
        }
    }

    /**
     * Asserts that the given file contains the specific content
     *
     * @param root
     * @param path
     * @param contents
     */
    protected void assertFileContains(File root, String path, String... contents){
        assertFileExists root, path
        String text = new File(root, path).text

        contents.each{String str -> assert text.contains(str)}
    }



}

