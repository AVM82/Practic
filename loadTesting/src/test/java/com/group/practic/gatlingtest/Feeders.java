package com.group.practic.gatlingtest;


import static io.gatling.javaapi.core.CoreDsl.csv;

import io.gatling.javaapi.core.FeederBuilder;

public class Feeders {
    public static FeederBuilder<String> slugs = csv("slugs.csv").random();
    public static FeederBuilder<String> courses = csv("courses.csv").random();
    public static FeederBuilder<String> persons = csv("persons.csv").random();
    public static FeederBuilder<String> states = csv("practiceState.csv").random();
    public static FeederBuilder<String> chapters = csv("chapters.csv").random();
    public static  FeederBuilder<String> emails = csv("emails.csv").random();

}