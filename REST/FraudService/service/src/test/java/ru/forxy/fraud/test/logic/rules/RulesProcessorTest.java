package ru.forxy.fraud.test.logic.rules;

import org.drools.runtime.StatelessKnowledgeSession;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.forxy.fraud.db.dao.mongo.CurrencyExchangeDAO;
import ru.forxy.fraud.rest.v1.check.Transaction;
import ru.forxy.fraud.test.BaseFraudServiceTest;

import ru.forxy.fraud.test.utils.data.TravelDataGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates Drools against the transaction
 */
public class RulesProcessorTest extends BaseFraudServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesProcessorTest.class);

    @Autowired
    StatelessKnowledgeSession session;

    @Autowired
    CurrencyExchangeDAO currencyExchangeDAO;

    @Test
    public void testRulesEvaluation() {
        Transaction transaction = TravelDataGenerator.generateTransaction();
        List<Object> arguments = new ArrayList<Object>(2);
        arguments.add(transaction);
        arguments.add(currencyExchangeDAO);
        session.execute(arguments);
        LOGGER.debug("Debug test");
    }
}
