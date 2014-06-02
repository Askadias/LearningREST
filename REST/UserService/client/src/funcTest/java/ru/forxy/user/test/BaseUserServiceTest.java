package ru.forxy.user.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.forxy.common.string.GenericGroovyContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        value = "classpath:/spring/user.client.test.spring-context.groovy",
        loader = GenericGroovyContextLoader.class)
public abstract class BaseUserServiceTest {
}
