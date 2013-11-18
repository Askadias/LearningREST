package ru.forxy.stress;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.jaxrs.impl.HttpHeadersImpl;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.forxy.BaseSpringContextTest;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.service.ISystemStatusService;
import ru.forxy.common.support.Configuration;
import ru.forxy.user.rest.IUserService;
import ru.forxy.user.rest.pojo.User;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Test for UserService loading to test performance via splunk
 */
public class UserServiceStressTest extends BaseSpringContextTest {

    public enum Config {
        ThreadsCount,
        IterationsCount
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceStressTest.class);

    private static final int DEFAULT_THREADS_COUNT = 8;
    private static final int DEFAULT_ITERATIONS_COUNT = 10;

    @Autowired
    private Configuration configuration;

    @Autowired
    private IUserService userService;

    @Autowired
    private ISystemStatusService userSystemStatus;

    @Test
    public void testAddDelete() throws InterruptedException, ExecutionException {
        final int threadsCount = configuration.getInt(Config.ThreadsCount, DEFAULT_THREADS_COUNT);
        final int iterationsCount = configuration.getInt(Config.IterationsCount, DEFAULT_ITERATIONS_COUNT);
        List<Runnable> userServiceCallTaskList = new ArrayList<Runnable>();
        for (int i = 0; i < threadsCount; i++) {
            final String userEmail = "xander" + i + "@gmail.com";
            userServiceCallTaskList.add(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < iterationsCount; j++) {
                        Message m = new MessageImpl();
                        UriInfo uriInfo = new UriInfoImpl(m);
                        HttpHeaders headers = new HttpHeadersImpl(m);
                        User xander = new User(userEmail, new byte[]{});
                        Response response = userService.createUser(xander, uriInfo, headers);
                        Assert.assertNotNull(response);
                        response = userService.login(xander, uriInfo, headers);
                        Assert.assertNotNull(response);
                        User user = response.readEntity(User.class);
                        Assert.assertNotNull(user);
                        LOGGER.info("User  has been successfully created: {}", user);
                        Assert.assertEquals(userEmail, user.getEmail());
                        response = userService.deleteUser(xander.getEmail(), uriInfo, headers);
                        Assert.assertNotNull(response);
                        response = userService.getUser(xander, uriInfo, headers);
                        Object entity = response.getEntity();
                        Assert.assertNotNull(entity);
                        Assert.assertEquals(404, response.getStatus());
                        LOGGER.info("User has been successfully removed");
                    }
                }
            });
        }
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        List<Future<?>> futures = new ArrayList<Future<?>>();
        for (Runnable task : userServiceCallTaskList) {
            futures.add(executor.submit(task));
        }
        for (Future<?> future : futures) {
            future.get();
        }
    }

    @Test
    public void testGetPage() throws InterruptedException, ExecutionException {
        final int threadsCount = configuration.getInt(Config.ThreadsCount, DEFAULT_THREADS_COUNT);
        final int iterationsCount = configuration.getInt(Config.IterationsCount, DEFAULT_ITERATIONS_COUNT);
        List<Runnable> userServiceCallTaskList = new ArrayList<Runnable>();
        for (int i = 0; i < threadsCount; i++) {
            userServiceCallTaskList.add(new Runnable() {
                @Override
                public void run() {
                    Random random = new Random();
                    Message m = new MessageImpl();
                    UriInfo uriInfo = new UriInfoImpl(m);
                    HttpHeaders headers = new HttpHeadersImpl(m);
                    for (int j = 0; j < iterationsCount; j++) {
                        Response response = userService.getUsers(random.nextInt(threadsCount), uriInfo, headers);
                        Assert.assertNotNull(response);
                        EntityPage<User> userPage = response.readEntity(new GenericType<EntityPage<User>>() {
                        });
                        Assert.assertNotNull(userPage);
                        Assert.assertTrue(CollectionUtils.isNotEmpty(userPage.getContent()));
                    }
                }
            });
        }
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        List<Future<?>> futures = new ArrayList<Future<?>>();
        for (Runnable task : userServiceCallTaskList) {
            futures.add(executor.submit(task));
        }
        for (Future<?> future : futures) {
            future.get();
        }
    }

    @Test
    public void testSystemStatus() throws InterruptedException, ExecutionException {
        final int threadsCount = configuration.getInt(Config.ThreadsCount, DEFAULT_THREADS_COUNT);
        final int iterationsCount = configuration.getInt(Config.IterationsCount, DEFAULT_ITERATIONS_COUNT);
        List<Runnable> userServiceCallTaskList = new ArrayList<Runnable>();
        for (int i = 0; i < threadsCount; i++) {
            userServiceCallTaskList.add(new Runnable() {
                @Override
                public void run() {
                    Message m = new MessageImpl();
                    UriInfo uriInfo = new UriInfoImpl(m);
                    HttpHeaders headers = new HttpHeadersImpl(m);
                    for (int j = 0; j < iterationsCount; j++) {
                        Response response = userSystemStatus.getSystemStatus(uriInfo, headers);
                        Assert.assertNotNull(response);
                        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
                    }
                }
            });
        }
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        List<Future<?>> futures = new ArrayList<Future<?>>();
        for (Runnable task : userServiceCallTaskList) {
            futures.add(executor.submit(task));
        }
        for (Future<?> future : futures) {
            future.get();
        }
    }
}
