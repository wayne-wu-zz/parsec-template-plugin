buildscript{
    repositories{
        maven{
            url "https://plugins.gradle.org/m2/"
        }
        maven{
            url 'http://dl.bintray.com/cjstehno/public'
        }
    }
    dependencies{
        classpath group: 'gradle.plugin.com.yahoo.parsec',
                name: 'parsec-template-plugin',
                version: '1.0.7-pre'
    }
}

allprojects {
    apply plugin: com.yahoo.parsec.parsec_templates.ParsecTemplatesPlugin
}



//TODO: see why the code below does not work
/*
if(!project.plugins.findPlugin(com.yahoo.parsec.parsec_templates.ParsecTemplatesPlugin)){
    project.apply(plugin: com.yahoo.parsec.parsec_templates.ParsecTemplatesPlugin)
}
*/
