package com.yahoo.parsec.parsec_templates.tasks

import com.yahoo.parsec.parsec_templates.ParsecTemplatesExtension
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import org.gradle.api.Task
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/**
 * @author waynewu
 */

class CreateParsecProjectTaskTest{


    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    protected Project project
    protected Task task

    private final Class taskClass

    CreateParsecProjectTaskTest(){
        this.taskClass = CreateParsecProjectTask
    }

    @Test
    void create(){
        project.extensions.create("parsecTemplate", ParsecTemplatesExtension)

        project.ext["artifactId"] = 'test_name'
        project.ext["groupId"] = 'test.group'
        project.ext["parent_dir"] = folder.getRoot() as String

        task.create()


        assertFileExists 'test_name/src/main/java/test/group'
        assertFileExists 'test_name/src/test/java/test/group'
        assertFileExists 'test_name/src/main/rdl'
        assertFileExists 'test_name/src/main/webapp'
        assertFileExists 'test_name/src/main/resources'

        assertFileExists 'test_name/build.gradle'

        assertFileContains folder.root, 'test_name/README.md', "# test_name"

        //TODO: test for extraTemplate
    }

    @Before
    void before(){
        System.setProperty('init.dir', folder.root as String)
        project = ProjectBuilder.builder().build()
        task = project.task('targetTask', type:taskClass)
    }

    @After
    void after(){
        System.setProperty('init.dir', '')
    }

    /**
     * Asserts that the specified file exists
     *
     * @param root
     * @param path
     */
    protected void assertFileExists(File root = folder.root, String path){
        assert new File(root, path).exists()
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