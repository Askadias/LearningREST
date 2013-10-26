package ru.forxy;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Base test that loads spring context.
 */
@Ignore
@ContextConfiguration(locations =
        {"classpath:/ru/forxy/spring-context.xml"})
public class BaseSpringContextTest extends AbstractJUnit4SpringContextTests {
}
