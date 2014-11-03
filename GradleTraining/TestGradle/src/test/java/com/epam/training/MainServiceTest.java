package com.epam.training;

import org.junit.Assert;
import org.junit.Test;

public class MainServiceTest {
    private MainService mainService = new MainService();

    @Test
    public void testDoService() {
        System.out.println("Message: " + mainService.doService());
        Assert.assertEquals("service result", mainService.doService());
    }
}
