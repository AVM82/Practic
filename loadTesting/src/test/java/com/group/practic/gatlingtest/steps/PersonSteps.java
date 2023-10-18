package com.group.practic.gatlingtest.steps;

import static io.gatling.javaapi.core.CoreDsl.exec;

import com.group.practic.gatlingtest.Feeders;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.http.HttpDsl;

public class PersonSteps {

    public ChainBuilder getPersonsIdRoles() {
        return CoreDsl
                .feed(Feeders.persons)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/persons/{personId}/roles")
                                        .get("/api/persons/#{personId}/roles")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                )
                .exitHereIfFailed();
    }

    public ChainBuilder getPersonsProfile() {
        return CoreDsl
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/persons/profile")
                                        .get("/api/persons/profile")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getPersonById() {
        return CoreDsl
                .feed(Feeders.persons)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/persons/{Id}")
                                        .get("/api/persons/#{personId}")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getPersonsMe() {
        return CoreDsl
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/persons/me")
                                        .get("/api/persons/me")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getPersonsApplicants() {
        return CoreDsl
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/persons/applicants")
                                        .get("/api/persons/me")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getPersonByName() {
        return CoreDsl
                .feed(Feeders.persons)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/persons/")
                                        .get("/api/persons/?name=#{name}")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

}