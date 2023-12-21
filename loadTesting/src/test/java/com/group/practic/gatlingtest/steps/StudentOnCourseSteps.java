package com.group.practic.gatlingtest.steps;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;

import com.group.practic.gatlingtest.Feeders;
import com.group.practic.gatlingtest.PropertyLoader;
import io.gatling.javaapi.core.Body;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.http.HttpDsl;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;

public class StudentOnCourseSteps {
    private final Iterator<Map<String, Object>> feedbackMap;
    private static final Body.WithString USER_BODY = StringBody("""
            {
            "email": "gatling@shpp.org",
            "password": "gatling"
            }
            """);
    private static final Body.WithString FEEDBACK_BODY = StringBody("#{feedback}");
    private final String slug;


    public StudentOnCourseSteps() {
        Properties properties = new PropertyLoader().getProperties();
        slug = properties.getProperty("slug");
        feedbackMap = feedbackMap();
    }

    private Iterator<Map<String, Object>> feedbackMap() {
        return Stream.generate((Supplier<Map<String, Object>>) () -> Collections
                        .singletonMap("feedback", RandomStringUtils.randomAlphanumeric(10, 20)))
                .iterator();
    }

    public ChainBuilder authUserByEmail() {
        return CoreDsl
                .exec(
                        HttpDsl
                                .http("POST/api/login")
                                .post("/api/auth")
                                .body(USER_BODY)
                                .asJson()
                                .check(HttpDsl.status().in(200))
                )
                .pause(2);
    }

    public ChainBuilder getCourses() {
        return CoreDsl
                .exec(
                        HttpDsl
                                .http("GET/api/courses")
                                .get("/api/courses")
                                .check(HttpDsl.status().in(200))
                )
                .pause(2);
    }

    public ChainBuilder getCoursesBySlug() {
        return CoreDsl
                .exec(
                        HttpDsl
                                .http("GET/api/courses/" + slug)
                                .get("/api/courses/" + slug)
                                .check(HttpDsl.status().in(200))
                )
                .pause(2);
    }

    public ChainBuilder postFeedback() {
        return CoreDsl
                .feed(feedbackMap)
                .exec(
                        HttpDsl
                                .http("POST/api/feedbacks/")
                                .post("/api/feedbacks/")
                                .body(FEEDBACK_BODY)
                                .asJson()
                                .check(HttpDsl.status().in(200),
                                        jsonPath("$.id").saveAs("feedbackId"))
                )
                .pause(2);
    }

    public ChainBuilder deleteFeedbackById() {
        return CoreDsl.exec(
                HttpDsl
                        .http("DELETE /api/feedbacks/{feedbackId}")
                        .delete("/api/feedbacks/delete/#{feedbackId}"
                                + "?page=0&size=10&sortState=DATE_DESCENDING")
                        .asJson()
                        .check(HttpDsl.status().is(200))
        );
    }


    public ChainBuilder getStudentsReportsCourseSlugTimeslots() {
        return CoreDsl
                .feed(Feeders.slugs)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/students/reports/course/{slug}/timeslots")
                                        .get("/api/students/reports/course/#{slug}/timeslots")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getStudentOnCourse() {
        return CoreDsl
                .feed(Feeders.courses)
                .feed(Feeders.persons)
                .tryMax(5)
                .on(exec(HttpDsl.http("GET/students?courseId=Id&studentId=Id")
                        .get("/api/students?courseId=#{courseId}&studentId=#{personId}")
                        .check(HttpDsl.status()
                                .in(200, 204)))
                        .pause(1, 4)
                )
                .exitHereIfFailed();
    }

    public ChainBuilder getStudentsReportsCourse() {
        return CoreDsl
                .feed(Feeders.slugs)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/students/reports/course/{slug}")
                                        .get("/api/students/reports/course/#{slug}")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(3, 7)
                ).exitHereIfFailed();
    }

    public ChainBuilder getStudentsChapters() {
        return CoreDsl
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/students/chapters")
                                        .get("/api/students/chapters")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getStudentById() {
        return CoreDsl
                .feed(Feeders.persons)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/students/{id}")
                                        .get("/api/students/#{personId}")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getStudentsStates() {
        return CoreDsl
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/students/reports/state")
                                        .get("/api/students/reports/states")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getStudentPracticesByState() {
        return CoreDsl
                .feed(Feeders.states)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/students/practices/{practiceState}")
                                        .get("/api/students/practices/#{state}")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 5)
                ).exitHereIfFailed();
    }

    public ChainBuilder getAllPracticeStates() {
        return CoreDsl
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/students/practices/states")
                                        .get("/api/students/practices/states")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 5)
                ).exitHereIfFailed();
    }

    public ChainBuilder getStudentsPracticesMy() {
        return CoreDsl
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/students/practices/my")
                                        .get("/api/students/practices/my")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 5)
                ).exitHereIfFailed();
    }
}
