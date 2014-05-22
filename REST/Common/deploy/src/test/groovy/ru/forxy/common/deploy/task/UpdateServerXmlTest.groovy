package ru.forxy.common.deploy.task

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import static org.junit.Assert.*

/**
 * Created by Tiger on 08.05.14.
 */
class UpdateServerXmlTest {
    def static final TOMCAT_HOME = this.getClass().getResource("/tomcatHome").path
    def static final SERVICE_NAME = 'Test'

    @Test
    public void testUpdateServerConfigFile() {
        Project project = ProjectBuilder.builder().build()
        def UpdateServerXmlTask taskAdd = project.task('updateServerXml', type: UpdateServerXmlTask) as UpdateServerXmlTask
        assertTrue(taskAdd instanceof UpdateServerXmlTask)

        // configure test add service task
        configureUpdateTask(taskAdd)
        taskAdd.updateServerConfigFile()

        def server = new XmlParser().parse("$TOMCAT_HOME/conf/server.xml");
        def serviceNode = server.Service.find{it.@name == SERVICE_NAME}
        assertNotNull(serviceNode)
        assertEquals(SERVICE_NAME, serviceNode.@name)
        assertEquals('8080', serviceNode.Connector[0].@port)


        // now remove service from server.xml
        def UpdateServerXmlTask taskRemove = project.task('removeServiceFromServerXml', type: RemoveServiceFromServerXmlTask) as RemoveServiceFromServerXmlTask
        assertTrue(taskRemove instanceof RemoveServiceFromServerXmlTask)
        // configure test remove service task
        configureUpdateTask(taskRemove)
        taskRemove.updateServerConfigFile()

        server = new XmlParser().parse("$TOMCAT_HOME/conf/server.xml");
        serviceNode = server.Service.find{it.@name == SERVICE_NAME}
        assertNull(serviceNode)
    }

    private static void configureUpdateTask(UpdateServerXmlTask task) {
        task.tomcatHome = TOMCAT_HOME
        task.useSSL = true
        task.basePort = 8000
        task.serviceName = SERVICE_NAME
    }
}
