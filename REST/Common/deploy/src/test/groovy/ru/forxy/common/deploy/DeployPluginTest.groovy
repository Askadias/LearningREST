package ru.forxy.common.deploy

import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Ignore
import org.junit.Test
import ru.forxy.common.deploy.task.CreateWebContextTask
import ru.forxy.common.deploy.task.UpdateServerXmlTask

import static org.junit.Assert.*

/**
 * Created by Tiger on 08.05.14.
 */
class DeployPluginTest {
    def static final TOMCAT_HOME = this.getClass().getResource("/tomcatHome").path
    def static final SERVICE_NAME = 'Test'

    @Test
    public void deployPluginAddsItsTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'war'
        project.apply plugin: 'deploy'

        assertNotNull(project.deploy.serviceName)
        assertTrue(project.tasks.copyEnvConfigApp instanceof Copy)
        assertTrue(project.tasks.copyEnvConfigTomcat instanceof Copy)
        assertTrue(project.tasks.createWebContext instanceof CreateWebContextTask)
        assertTrue(project.tasks.updateServerXml instanceof UpdateServerXmlTask)
        assertTrue(project.tasks.deploy instanceof Copy)
    }

    @Ignore
    @Test
    public void deployPluginNoWar() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'deploy'

        assertNotNull(project.deploy.serviceName)
        assertNull(project.tasks.findByName('deploy'))
    }
}
