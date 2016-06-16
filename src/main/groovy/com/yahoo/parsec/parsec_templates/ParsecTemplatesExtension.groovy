package com.yahoo.parsec.parsec_templates

/**
 * @author waynewu
 */

class ParsecTemplatesExtension {

    /**
     * Extra elements to generate
     */
    Closure extraTemplate = {}

    /**
     * File that that the generated build.gradle should inherit (extend) from
     * default will be the parsec-base-build
     * TODO: add script to always target to the latest when release
     */
    String applyFromPath = "https://dl.bintray.com/wayne-wu/Gradle/parsec-base-build/1.0.1/parsec.gradle"

    String repository = ""

    String dependency = ""

    public Closure getExtraTemplate(){ return extraTemplate; }

    public String getApplyFromPath(){ return applyFromPath; }

    //TODO: Provide some boolean options such as Overriding etc. to provide more flexibility

}
