package com.group.practic.gatlingtest.simulations;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import com.github.javafaker.Faker;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class StudentAddingSimulation extends Simulation {
    private static final  Config conf = ConfigFactory.load("simulation.properties");
    private static final HttpProtocolBuilder HTTP_PROTOCOL_BUILDER = setupProtocolForSimulation();
    private static final Iterator<Map<String, Object>> FEED_DATA = setupTestFeedData();
    private static final ScenarioBuilder POST_SCENARIO_BUILDER = buildPostScenario();


    public StudentAddingSimulation() {
        setUp(POST_SCENARIO_BUILDER.injectOpen(postEndpointInjectionProfile())
                        .protocols(HTTP_PROTOCOL_BUILDER)).assertions(
                        global().responseTime().max().lte(conf.getInt("maxResponseTime")),
                        global().successfulRequests().count()
                            .gt(conf.getLong("successRequestsCount")));
    }

    private static HttpProtocolBuilder setupProtocolForSimulation() {
        return HttpDsl.http.baseUrl(conf.getString("baseUrl"))
            .acceptHeader("application/json")
            .maxConnectionsPerHost(conf.getInt("maxConnectionsPerHost"))
            .userAgentHeader("Gatling/Performance Test");
    }

    private static Iterator<Map<String, Object>> setupTestFeedData() {
        Faker faker = new Faker();
        Iterator<Map<String, Object>> iterator;
        iterator = Stream.generate(() -> {
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("studentPib", faker.name().fullName());
            return stringObjectMap;
        })
            .iterator();
        return iterator;
    }

    private static ScenarioBuilder buildPostScenario() {
        return CoreDsl.scenario(conf.getString("scenarioName"))
            .feed(FEED_DATA)
            .exec(http("create-student-request")
                .post(conf.getString("createStudentEndpoint"))
                .header("Content-Type", "application/json")
                .body(StringBody(conf.getString("createUserBody")))
                .check(status().is(conf.getInt("postCreateUserRequiredStatus")))
            );
    }

    private OpenInjectionStep postEndpointInjectionProfile() {
        return atOnceUsers(conf.getInt("usersCount"));
    }
}

