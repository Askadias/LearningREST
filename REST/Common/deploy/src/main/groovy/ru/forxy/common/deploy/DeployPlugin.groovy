package ru.forxy.common.deploy

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import ru.forxy.common.deploy.extension.DeployExtension
import ru.forxy.common.deploy.task.CreateWebContextTask
import ru.forxy.common.deploy.task.UpdateServerXmlTask

class DeployPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.extensions.create("deploy", DeployExtension)
        project.deploy.contextName = project.name
        project.apply plugin: 'war'

        if (project.hasProperty('env')) {
            project.deploy.env = project.getProperty("env")
        }

        project.task('copyEnvConfigApp', type: Copy) << {
            from                    project.deploy.appconfigSrc
            into                    project.deploy.appconfigDir
            //include               '**/*.properties'
            //include               '**/*.xml'
            //exclude               '**/*.bat'
            //exclude               '**/*.sh'
        }

        project.task('copyEnvConfigTomcat', type: Copy) << {
            from                    "$project.deploy.appconfigSrc/env/$project.deploy.env"
            into                    "$project.deploy.tomcatHome/bin"
            include                 '**/*.bat'
            include                 '**/*.sh'
        }

        project.task('updateServerXml', type: UpdateServerXmlTask) << {
            tomcatHome              project.deploy.tomcatHome
            serviceName             project.deploy.serviceName
            useSSL                  project.deploy.useSSL
            basePort                project.deploy.basePort
            protocol                project.deploy.protocol
            connectionTimeout       project.deploy.connectionTimeout
            maxHttpHeaderSize       project.deploy.maxHttpHeaderSize
            maxThreads              project.deploy.maxThreads
            minSpareThreads         project.deploy.minSpareThreads
            maxSpareThreads         project.deploy.maxSpareThreads
            enableLookups           project.deploy.enableLookups
            disableUploadTimeout    project.deploy.disableUploadTimeout
            acceptCount             project.deploy.acceptCount
            ajpProtocol             project.deploy.ajpProtocol
            secure                  project.deploy.secure
            clientAuth              project.deploy.clientAuth
            defaultHost             project.deploy.defaultHost
            appBase                 project.deploy.appBase
            unpackWARs              project.deploy.unpackWARs
            autoDeploy              project.deploy.autoDeploy
            logsDir                 project.deploy.logsDir
            accessLogPrefix         project.deploy.accessLogPrefix
            accessLogSuffix         project.deploy.accessLogSuffix
            accessLogPattern        project.deploy.accessLogPattern
            sslProtocol             project.deploy.sslProtocol
            keyAlias                project.deploy.keyAlias
            keystoreFile            project.deploy.keystoreFile
            keystorePass            project.deploy.keystorePass
            keystoreType            project.deploy.keystoreType
            truststoreFile          project.deploy.truststoreFile
            truststorePass          project.deploy.truststorePass
            truststoreType          project.deploy.truststoreType
        }

        if (project.plugins.findPlugin('war')) {
            project.war {
                manifest {
                    attributes(
                            'Product': project.rootProject.name,
                            'Version': project.version,
                            'Built-On': new Date().format('yyyy-mm-dd HH:MM:ss')
                    )
                }
            }

            project.task('createWebContext', type: CreateWebContextTask, dependsOn: project.tasks.war) << {
                tomcatHome          project.deploy.tomcatHome
                serviceName         project.deploy.serviceName
                host                project.deploy.host
                contextName         project.deploy.contextName
                warFileName         project.war.archiveName
                reloadable          project.deploy.reloadableWar
            }

            project.task('deploy', type: Copy, dependsOn:
                    [project.tasks.war,
                     project.tasks.copyEnvConfigApp,
                     project.tasks.copyEnvConfigTomcat,
                     project.tasks.createWebContext,
                     project.tasks.updateServerXml]) << {
                from project.tasks.war
                into "$project.deploy.tomcatHome/warfiles"
            }
        }
    }
}