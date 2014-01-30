package ru.forxy.fraud.test;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Base class for all the FraudService unit test classes.
 * Has Spring context configured
 */
@Ignore
@ContextConfiguration(locations = {"classpath:/ru/forxy/fraud/test/spring-test-context.xml"})
public abstract class BaseFraudServiceTest extends AbstractJUnit4SpringContextTests {}
