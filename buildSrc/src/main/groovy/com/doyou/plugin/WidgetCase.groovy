package com.doyou.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class WidgetCase implements Plugin<Project>{
    @Override
    void apply(Project project) {
        def extension = project.extensions.create('widgetcase',WidgetCaseExtension)
        project.afterEvaluate {
            println "Hi ${extension.keyAlias} ${extension.signPwd} ${extension.keyStoreFileDir}"
            extension.widgetCase()
        }
    }
}
