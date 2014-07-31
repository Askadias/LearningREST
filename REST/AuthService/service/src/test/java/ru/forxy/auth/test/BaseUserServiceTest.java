package ru.forxy.auth.test;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.forxy.common.string.GenericGroovyContextLoader;

/**
 * Base class for all the UserService unit test classes.
 * Has Spring context configured
 */
@Ignore
@ContextConfiguration(locations = {"classpath:spring/spring-test-context.xml"})
public abstract class BaseUserServiceTest extends AbstractJUnit4SpringContextTests {
}
