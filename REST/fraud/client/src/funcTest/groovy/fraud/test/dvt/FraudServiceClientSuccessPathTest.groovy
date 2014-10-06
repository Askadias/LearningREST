package fraud.test.dvt

import fraud.rest.v1.velocity.VelocityMetric
import fraud.rest.v1.velocity.redis.VMetric
import fraud.test.BaseFraudServiceTest
import org.apache.commons.lang.RandomStringUtils
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

/**
 * Test success path for AuthService client
 */
class FraudServiceClientSuccessPathTest extends BaseFraudServiceTest {

    private static final Random RAND = new Random()

    @Test
    void testSimpleVelocityCassandraCheck() {
        Assert.assertNotNull(fraudServiceClient)
        String transactionGUID = UUID.randomUUID().toString()
        def testData = [
                'Email'     : 'aaa@mail.com',
                'Amount'    : '500.00',
                'CreditCard': '1111222233334444',
        ]
        List<VelocityMetric> metrics = fraudServiceClient.check(transactionGUID, testData)
        Assert.assertNotNull(metrics)
    }

    @Test
    void testSimpleVelocityRedisCheck() {
        Assert.assertNotNull(fraudServiceClient)
        String transactionGUID = UUID.randomUUID().toString()
        def testData = [
                'Email'     : 'aaa@mail.com',
                'Amount'    : '500.00',
                'CreditCard': '1111222233334444',
        ]
        List<VMetric> metrics = fraudServiceClient.rcheck(transactionGUID, testData)
        Assert.assertNotNull(metrics)
    }

    //@Ignore
    @Test
    void stressTest() {
        Assert.assertNotNull(fraudServiceClient)
        10000.times {

            String transactionGUID = UUID.randomUUID().toString()
            def testData = [
                    'BillAddress': RandomStringUtils.randomAlphabetic(50),
                    'Purchases'  : "${RAND.nextInt(5000)}.00".toString(),
                    'CreditCard' : generateCC(),
                    'TUID'       : UUID.randomUUID().toString(),
                    'Email'      : "aaa${RAND.nextInt(10)}@gmail.com".toString(),
                    'DeviceID'   : UUID.randomUUID().toString(),
                    'IPAddress'  : generateIPAddress(),
                    'PhoneNumber': generatePhoneNumber(),
            ]
            List<VMetric> redisMetrics = fraudServiceClient.rcheck(transactionGUID, testData)
            Assert.assertNotNull(redisMetrics)
            //Assert.assertTrue(redisMetrics.size() > 0)
            List<VelocityMetric> cassandraMetrics = fraudServiceClient.check(transactionGUID, testData)
            Assert.assertNotNull(cassandraMetrics)
            //Assert.assertTrue(cassandraMetrics.size() > 0)
        }
    }

    private static String generateIPAddress() {
        String ip = ''
        ip += RAND.nextInt(256)
        3.times {
            ip += '.'
            ip += RAND.nextInt(256) as String
        }
        ip
    }

    private static String generateCC() {
        String cc = ''
        16.times { cc += RAND.nextInt(10) }
        cc
    }

    private static String generatePhoneNumber() {
        String nbr = ''
        nbr += '+'
        12.times { nbr += RAND.nextInt(10) }
        nbr
    }
}
