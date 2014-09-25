package ru.forxy.fraud.test.dvt

import org.junit.Assert
import org.junit.Test
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric
import ru.forxy.fraud.rest.v1.velocity.redis.VMetric
import ru.forxy.fraud.test.BaseFraudServiceTest

/**
 * Test success path for AuthService client
 */
class VelocityServiceClientSuccessPathTest extends BaseFraudServiceTest {

    private static final Random RAND = new Random()

    @Test
    void testSimpleVelocityCheck() {
        Assert.assertNotNull(authServiceClient)
        String transactionGUID = UUID.randomUUID().toString()
        def testData = [
                'Email'     : 'aaa@mail.com',
                'Amount'    : '500.00',
                'CreditCard': '1111222233334444',
        ]
        List<VelocityMetric> metrics = authServiceClient.check(transactionGUID, testData)
        Assert.assertNotNull(metrics)
    }

    @Test
    void stressTest() {
        Assert.assertNotNull(authServiceClient)
        10000.times {
            String transactionGUID = UUID.randomUUID().toString()
            def testData = [
                    'BillAddress': 'Some bill address',
                    'Purchases'  : '500.00',
                    'CreditCard' : generateCC(),
                    'TUID'       : UUID.randomUUID().toString(),
                    'Email'      : 'aaa@mail.com',
                    'DeviceID'   : UUID.randomUUID().toString(),
                    'IPAddress'  : generateIPAddress(),
                    'PhoneNumber': generatePhoneNumber(),
            ]
            List<VMetric> metrics = authServiceClient.rcheck(transactionGUID, testData)
            Assert.assertNotNull(metrics)
        }
    }

    private static String generateIPAddress() {
        String ip = ''
        ip << RAND.nextInt(256)
        3.times {
            ip << '.'
            ip << RAND.nextInt(256)
        }
        ip
    }

    private static String generateCC() {
        String cc = ''
        16.times { cc << RAND.nextInt(10) }
        cc
    }

    private static String generatePhoneNumber() {
        String nbr = ''
        nbr << '+'
        12.times { nbr << RAND.nextInt(10) }
        nbr
    }
}
