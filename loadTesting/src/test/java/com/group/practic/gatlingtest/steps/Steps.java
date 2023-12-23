package com.group.practic.gatlingtest.steps;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.exec;

import com.github.javafaker.Faker;
import com.group.practic.gatlingtest.Feeders;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.http.HttpDsl;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class Steps {
    private static Iterator<Map<String, Object>> feedData() {
        Faker faker = new Faker();
        Iterator<Map<String, Object>> iterator;
        iterator = Stream.generate(() -> {
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("name", faker.food().dish());
            return stringObjectMap;
        })
                .iterator();
        return iterator;
    }

    public ChainBuilder getFirstPage() {
        return CoreDsl
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/Main page")
                                        .get("/")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getFeedback() {
        return CoreDsl
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/feedback")
                                        .get("/feedback")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(2, 5)
                ).exitHereIfFailed();
    }

    public ChainBuilder postFeedback() {
        return CoreDsl
                .feed(Feeders.emails)
                .feed(feedData())
                .tryMax(5)
                .on(exec(HttpDsl.http("POST/feedbacks/")
                        .post("/api/feedbacks/")
                        .body(
                                StringBody("{\"email\": \"#{email}\", \"feedback\": \"#{name}\"}"))
                        .asJson()
                        .check(HttpDsl.status().in(200, 204)))
                        .pause(1, 4)).exitHereIfFailed();
    }

    public ChainBuilder getChapterById() {
        return CoreDsl
                .feed(Feeders.chapters)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/chapters/{Id}")
                                        .get("/api/chapters/#{chapterId}")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getTopicsReports() {
        return CoreDsl
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/topicsreports/")
                                        .get("/api/topicsreports/")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getTopicReportsByIdChapter() {
        return CoreDsl
                .feed(Feeders.chapters)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/topicsreports/{chapterId}")
                                        .get("/api/topicsreports/#{chapterId}")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

}