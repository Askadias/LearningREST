package ru.forxy.fraud.test.logic.rules;

import org.drools.runtime.Globals;
import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.forxy.fraud.rest.pojo.Transaction;
import ru.forxy.fraud.rest.pojo.product.Price;
import ru.forxy.fraud.rest.pojo.product.Product;
import ru.forxy.fraud.rest.pojo.product.travel.TravelProduct;
import ru.forxy.fraud.test.BaseFraudServiceTest;
import ru.forxy.fraud.test.utils.gen.TravelDataGenerator;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import static ru.forxy.fraud.test.utils.gen.TravelDataGenerator.*;

/**
 * Evaluates Drools against the transaction
 */
public class RulesProcessorTest extends BaseFraudServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesProcessorTest.class);
    private static final Long millisPerDAy = 86400000l;

    @Autowired
    StatelessKnowledgeSession session;

    @Test
    public void testRulesEvaluation() {
        Transaction transaction = generateTransaction();
        session.execute(transaction);
        LOGGER.debug("Debug test");
        //transaction.getProducts().get(0).getPrice().getUsdValue().doubleValue()
        Globals globals = session.getGlobals();
        Assert.assertNotNull(transaction.getAttributes());
        //Assert.assertTrue(transaction.getAttributes().size() > 0);
    }
}
