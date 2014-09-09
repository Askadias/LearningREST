package ru.forxy.common.test.utils.gen;

import java.util.Date;

/**
 * Realistic person data generator
 */
public abstract class PersonGenerator extends AbstractGenerator {

    // @formatter:off
    private static final String[] FIRST_NAMES_MALE = {
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

    private static final String[] FIRST_NAMES_FEMALE = {
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
    private static final String[] LAST_NAMES = {
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
    private static final String[] LAST_NAMES_POSTFIXES = {
            "",
            "s",
            "son",
            "ey",
            "es",
            "ett",
            "is",
            "ay"};

    private static final String[] MAILS = {
            "@mail.ru",
            "@gmail.com",
            "@rambler.ru",
            "@tut.by",
            "@hotmail.com",
            "@yahoo.com"};
    // @formatter:on

    public static String generateEmail(String firstName, String lastName) {
        return firstName + "_" + lastName + MAILS[RAND.nextInt(MAILS.length)];
    }

    public static String generateEmail(String login) {
        return login + MAILS[RAND.nextInt(MAILS.length)];
    }

    public static String generateEmail() {
        return NumbersGenerator.generateNumber(NumbersGenerator.generateInt(3, 15)) + MAILS[RAND.nextInt(MAILS.length)];
    }

    public static String generateFirstName(boolean isMale) {
        if (isMale) {
            return FIRST_NAMES_MALE[RAND.nextInt(FIRST_NAMES_MALE.length)];
        } else {
            return FIRST_NAMES_FEMALE[RAND.nextInt(FIRST_NAMES_FEMALE.length)];
        }
    }

    public static String generateLastName() {
        return LAST_NAMES[RAND.nextInt(LAST_NAMES.length)] + LAST_NAMES_POSTFIXES[RAND.nextInt(LAST_NAMES_POSTFIXES.length)];
    }

    public static int generateAge() {
        return NumbersGenerator.generateInt(0, 110);
    }

    public static String generateGender() {
        return RAND.nextBoolean() ? "M" : "F";
    }

    public static Date generateBirthDate(final int age) {
        return DateGenerator.generateDateInPast(age * 365, (age + 1) * 365);
    }

    public static String generatePasswordData() {
        byte[] password = new byte[32];
        RAND.nextBytes(password);
        return new String(password);
    }
}
