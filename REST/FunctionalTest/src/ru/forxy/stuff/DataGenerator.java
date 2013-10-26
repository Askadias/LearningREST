package ru.forxy.stuff;

import org.apache.cxf.jaxrs.impl.HttpHeadersImpl;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.forxy.BaseSpringContextTest;
import ru.forxy.crypto.ICryptoService;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.util.Random;

/**
 * Class to generate huge amount of realistic data randomly
 */
public class DataGenerator extends BaseSpringContextTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);

    @Autowired(required = false)
    ICryptoService cryptoService;

    @Autowired(required = false)
    IUserService userService;

    private static final int MAX_USERS = 1000;

    public static final long DAY = 86400000;

    final Random rand = new Random();

    private final String[] firstNamesMale = {
            "Adam",
            "Steve",
            "Malcolm",
            "Gray",
            "John",
            "Jacob",
            "James",
            "Abraham",
            "Bob",
            "Jeff",
            "Jorge",
            "Tom",
            "Michael",
            "Eugen",
            "Luis",
            "Gregory",
            "Frank",
            "Denis",
            "Robin",
            "Donald",
            "Josef",
            "Colin"};

    private final String[] firstNamesFemale = {
            "Cat",
            "Ann",
            "Natasha",
            "Rachel",
            "Candy",
            "Denis",
            "Val",
            "Caddy",
            "Stacy",
            "Robin",};
    private final String[] lastNames = {
            "Allan",
            "Adder",
            "Bail",
            "Bak",
            "Benn",
            "Robin",
            "Brown",
            "Carter",
            "Clark",
            "Cook",
            "Cooper",
            "Doyle",
            "Evan",
            "Green",
            "Harri",
            "Hill",
            "Jack",
            "Jam",
            "John",
            "Jon",
            "King",
            "Lew",
            "Martin",
            "Miller",
            "Mitchell",
            "Moore",
            "Mort",
            "Moor",
            "Murry",
            "Parker",
            "Patter",
            "Phillips",
            "Richard",
            "Smith",
            "Spencer",
            "Taylor",
            "Turner",
            "Walker",
            "Wat",
            "White",
            "Wil",
            "Wood",
            "Wright"};
    private final String[] lastNamesPostfixes = {
            "",
            "s",
            "son",
            "ey",
            "es",
            "ett",
            "is",
            "ay"};

    private final String[] mails = {
            "@mail.ru",
            "@gmail.com",
            "@rambler.ru",
            "@tut.by",
            "@hotmail.com",
            "@yahoo.com"};

    @Test
    @Ignore
    public void generateUsers() {
        Message m = new MessageImpl();
        final UriInfo uriInfo = new UriInfoImpl(m);
        final HttpHeaders headers = new HttpHeadersImpl(m);
        final Thread[] threads = new Thread[1];
        for (int t = 0; t < threads.length; t++) {
            threads[t] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < MAX_USERS / threads.length; i++) {
                        boolean exists = true;
                        User user = null;
                        while (exists) {
                            boolean isMale = rand.nextBoolean();
                            String firstName = generateFirstName(isMale);
                            String lastName = generateLastName();
                            String password = firstName + "Password";
                            byte[] encryptedPassword = cryptoService.hash(password);

                            user = new User(generateEMail(firstName, lastName), encryptedPassword);
                            user.setLogin(firstName);
                            user.setFirstName(firstName);
                            user.setLastName(lastName);
                            user.setGender(isMale ? 'M' : 'F');
                            user.setBirthDate(generatePastDate(365 * 10, 365 * 60));

                            Response response = userService.login(user, uriInfo, headers);
                            exists = response.getStatus() == Response.Status.OK.getStatusCode();
                        }
                        userService.createUser(user, uriInfo, headers);
                    }
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                LOGGER.error("Cannot wait for thread {" + thread.getName() + "} execution", e);
            }
        }
    }

    private String generateEMail(String firstName, String lastName) {
        return firstName + "_" + lastName + mails[rand.nextInt(mails.length)];
    }

    private String generateFirstName(boolean isMale) {
        if (isMale) {
            return firstNamesMale[rand.nextInt(firstNamesMale.length)];
        } else {
            return firstNamesFemale[rand.nextInt(firstNamesFemale.length)];
        }
    }

    private String generateLastName() {
        return lastNames[rand.nextInt(lastNames.length)] + lastNamesPostfixes[rand.nextInt(lastNamesPostfixes.length)];
    }

    private Date generatePastDate(int minDays, int maxDays) {
        Date date = new Date();
        date.setTime(date.getTime() - minDays * DAY - Math.abs(rand.nextInt(maxDays - minDays) * DAY));
        return date;
    }
}
