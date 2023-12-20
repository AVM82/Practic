package com.group.practic.gatlingtest;

import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GatlingTest extends Simulation {
    public static final Logger logger = LoggerFactory.getLogger(GatlingTest.class);
    private static final String PROPERTY = "simulation.properties";

    public GatlingTest() {
        Properties properties = getProperties();
        HttpProtocolBuilder httpProtocol = HttpDsl.http.baseUrl(properties.getProperty("baseUrl"))
                .authorizationHeader(properties.getProperty("jwtToken"))
                .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .acceptLanguageHeader("en-US,en;q=0.5")
                .acceptEncodingHeader("gzip, deflate")
                .userAgentHeader("Mozilla/5.0 (Macintosh; "
                        + "Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0");

        this.setUp(Scenario.userScenario.injectOpen(
                CoreDsl.constantUsersPerSec(Integer.parseInt(properties.getProperty("users")))
                        .during(Integer
                                .parseInt(properties.getProperty("during"))))

//                Scenario.adminScenario.injectOpen(
//                        rampUsers(Integer.parseInt(properties.getProperty("admins")))
//                                .during(Integer.parseInt(properties.getProperty("during")))
//                ),
//
//                Scenario.visitorsScenario.injectOpen(
//                        atOnceUsers(Integer.parseInt(properties.getProperty("visitors")))
//                )
        ).protocols(httpProtocol);
    }



    private Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream is = GatlingTest.class.getClassLoader().getResourceAsStream(PROPERTY)) {
            Reader reader = new InputStreamReader(
                    Objects.requireNonNull(is), StandardCharsets.UTF_8);
            properties.load(reader);
        } catch (IOException e) {
            logger.error("cannot load property ", e);
        }
        return properties;
    }
}