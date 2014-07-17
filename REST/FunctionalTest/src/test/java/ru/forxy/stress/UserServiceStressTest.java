package ru.forxy.stress;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.forxy.BaseSpringContextTest;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.StatusEntity;
import ru.forxy.common.support.Configuration;
import ru.forxy.user.rest.v1.UserServiceClient;
import ru.forxy.user.rest.v1.pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
    private UserServiceClient userServiceClient;

    @Test
    public void testAddDelete() throws InterruptedException, ExecutionException {
        final String transactionGUID = UUID.randomUUID().toString();
        final int threadsCount = configuration.getInt(Config.ThreadsCount, DEFAULT_THREADS_COUNT);
        final int iterationsCount = configuration.getInt(Config.IterationsCount, DEFAULT_ITERATIONS_COUNT);
        List<Runnable> userServiceCallTaskList = new ArrayList<Runnable>();
        for (int i = 0; i < threadsCount; i++) {
            final String userEmail = "xander" + i + "@gmail.com";
            userServiceCallTaskList.add(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < iterationsCount; j++) {
                        final User xander = new User(userEmail, "");

                        StatusEntity status = userServiceClient.createUser(transactionGUID, xander);
                        Assert.assertNotNull(status);

                        User user = userServiceClient.getUser(transactionGUID, userEmail);
                        Assert.assertNotNull(user);
                        LOGGER.info("User  has been successfully created: {}", user);
                        Assert.assertEquals(userEmail, user.getEmail());

                        status = userServiceClient.deleteUser(transactionGUID, userEmail);
                        Assert.assertNotNull(status);

                        user = userServiceClient.getUser(transactionGUID, userEmail);
                        LOGGER.info("User has been successfully removed");
                    }
                }
            });
        }
        final ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        final List<Future<?>> futures = new ArrayList<Future<?>>();
        for (final Runnable task : userServiceCallTaskList) {
            futures.add(executor.submit(task));
        }
        for (final Future<?> future : futures) {
            future.get();
        }
    }

    @Test
    public void testGetPage() throws InterruptedException, ExecutionException {
        final String transactionGUID = UUID.randomUUID().toString();
        final int threadsCount = configuration.getInt(Config.ThreadsCount, DEFAULT_THREADS_COUNT);
        final int iterationsCount = configuration.getInt(Config.IterationsCount, DEFAULT_ITERATIONS_COUNT);
        List<Runnable> userServiceCallTaskList = new ArrayList<Runnable>();
        for (int i = 0; i < threadsCount; i++) {
            userServiceCallTaskList.add(new Runnable() {
                @Override
                public void run() {
                    final Random random = new Random();
                    for (int j = 0; j < iterationsCount; j++) {
                        EntityPage<User> userPage = userServiceClient.getUsers(transactionGUID, random.nextInt(threadsCount));
                        Assert.assertNotNull(userPage);
                        Assert.assertTrue(CollectionUtils.isNotEmpty(userPage.getContent()));
                    }
                }
            });
        }
        final ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        final List<Future<?>> futures = new ArrayList<Future<?>>();
        for (final Runnable task : userServiceCallTaskList) {
            futures.add(executor.submit(task));
        }
        for (final Future<?> future : futures) {
            future.get();
        }
    }

    @Test
    public void testSystemStatus() throws InterruptedException, ExecutionException {
        final int threadsCount = configuration.getInt(Config.ThreadsCount, DEFAULT_THREADS_COUNT);
        final int iterationsCount = configuration.getInt(Config.IterationsCount, DEFAULT_ITERATIONS_COUNT);
        final List<Runnable> userServiceCallTaskList = new ArrayList<Runnable>();
        for (int i = 0; i < threadsCount; i++) {
            userServiceCallTaskList.add(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < iterationsCount; j++) {
                        /*final Response response = userServiceClient.getSystemStatus();
                        Assert.assertNotNull(response);
                        Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());*/
                    }
                }
            });
        }
        final ExecutorService executor = Executors.newFixedThreadPool(threadsCount);
        final List<Future<?>> futures = new ArrayList<Future<?>>();
        for (final Runnable task : userServiceCallTaskList) {
            futures.add(executor.submit(task));
        }
        for (final Future<?> future : futures) {
            future.get();
        }
    }
}
