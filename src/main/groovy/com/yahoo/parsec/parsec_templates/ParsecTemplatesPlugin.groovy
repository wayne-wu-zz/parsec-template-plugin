package com.yahoo.parsec.parsec_templates

import com.yahoo.parsec.parsec_templates.tasks.CreateParsecProjectTask
import org.gradle.api.Plugin
import org.gradle.api.Project
/**
 * @author waynwu
 */

class ParsecTemplatesPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.extensions.create("parsecTemplate", ParsecTemplatesExtension)
        project.task 'createParsecProject', type:CreateParsecProjectTask
    }
}

