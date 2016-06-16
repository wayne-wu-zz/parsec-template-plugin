package com.yahoo.parsec.parsec_templates.tasks

import com.yahoo.parsec.parsec_templates.ParsecTemplatesExtension
import org.gradle.api.tasks.TaskAction
import templates.ProjectTemplate
/**
 * @author waynewu
 */


class CreateParsecProjectTask extends AbstractProjectTask {

    //TODO: Move this to a constructor
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
}
