package com.yahoo.parsec.parsec_templates.tasks

import com.yahoo.parsec.parsec_templates.ParsecTemplatesExtension
import org.gradle.api.tasks.TaskAction
import templates.ProjectTemplate
/**
 * @author waynewu
 */


class CreateParsecProjectTask extends AbstractProjectTask {

    String projectGroup
    String projectName
    String projectVersion
    String projectPath
    String projectGroupPath
    ParsecTemplatesExtension pluginExtension

    public CreateParsecProjectTask(){
        pluginExtension = getProject().getExtensions().findByType(ParsecTemplatesExtension.class)
        if(pluginExtension == null){
            pluginExtension = new ParsecTemplatesExtension();
        }
    }

    @TaskAction
    void create() {

        projectGroup = groupName()
        projectName = projectName()
        projectVersion = projectVersion()

        projectGroupPath = projectGroup.replace('.', '/')

        projectPath = projectPath(projectName)

        generate_template()
        generate_extra()

        if(pluginExtension.createSampleRDL){
            generate_sample_rdl()
        }
        if(pluginExtension.createTravisCI){
            generate_travis_ci()
        }

        //TODO: Nice to generate wrapper as well
    }

    /**
     * Generate the template (folder structure and files)
     * Client can overload this method to create their own base template
     */
    protected void generate_template(){
        ProjectTemplate.fromRoot(projectPath) {

            'src' {
                'main' {
                    'java' {
                        "${projectGroupPath}" {}
                    }
                    'rdl' {}
                    'webapp' {}
                    'resources' {}
                }
                'test' {
                    'java' {
                        "${projectGroupPath}" {}
                    }
                    'resources'{}
                }
            }
            'config' {
                'checkstyle' {
                    'checkstyle-suppressions.xml' getText('/templates/checkstyle-suppressions.xml')
                }
                'pmd' {}
                'findbugs' {
                    'excludeFilter.xml' getText('/templates/excludeFilter.xml')
                }
            }

            'build.gradle' template: '/templates/build.gradle', applyFromPath: pluginExtension.applyFromPath, projectGroup: projectGroup
            'gradle.properties' template: '/templates/gradle.properties', projectVersion: projectVersion
            'README.md' template: '/templates/README.md', projectName: projectName
            'README.sh' getText('/templates/README.sh')
            //'pom.xml' getText('/templates/pom.xml') //TODO: Use built in gradle function to generate pom.xml
        }
    }

    /**
     * Generate extra folder structures and files defined by Client in configurations
     */
    protected void generate_extra(){
        ProjectTemplate.fromRoot(projectPath, pluginExtension.extraTemplate)
    }

    /**
     * Generate sample rdl file
     */
    protected void generate_sample_rdl(){
        ProjectTemplate.fromRoot(projectPath){
            'src/main/rdl'{
                'sample.rdl' template: '/templates/sample.rdl', groupName: projectGroup
            }
        }
    }

    protected void generate_travis_ci(){
        ProjectTemplate.fromRoot(projectPath){
            '.travis.yml' getText('/templates/.travis.yml')
        }
    }
}
