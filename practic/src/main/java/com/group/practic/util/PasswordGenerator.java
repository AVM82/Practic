package com.group.practic.util;

import com.github.javafaker.Faker;
import java.util.Locale;
import java.util.Random;


public class PasswordGenerator {

    private static final Random random = new Random();

    private static final Faker faker = new Faker(new Locale("en"));


    public static String generateRandomPassword() {
        int randomNumb = random.nextInt(10);
        String result;
        result = switch (randomNumb) {
            case 0 -> faker.beer().name();
            case 1 -> faker.rockBand().name();
            case 2 -> faker.address().city();
            case 3 -> faker.app().name();
            case 4 -> faker.cat().name();
            case 5 -> faker.color().name();
            case 6 -> faker.dog().name();
            case 7 -> faker.food().vegetable();
            case 8 -> faker.food().fruit();
            case 9 -> faker.hobbit().character();
            default -> faker.harryPotter().character();
        };
        result = result.replaceAll(" ", "");
        return result;
    }

}
