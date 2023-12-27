package com.group.practic.gatlingtest.steps;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;

import com.group.practic.gatlingtest.Feeders;
import io.gatling.javaapi.core.Body;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.http.HttpDsl;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;

public class StudentOnCourseSteps {
    private static final Body.WithString USER_BODY = StringBody("""
            {
            "email": "gatling@shpp.org",
            "password": "gatling"
            }
            """);
    private static final Body.WithString CERTIFICATE_DTO = StringBody("""
            {
            "studentName": "Gatling",
            "courseName": "Java",
            "skills": [],
            "start": "2000-10-10"
            }
            """);
    private static final Body.WithString FEEDBACK_BODY = StringBody("#{feedback}");
    private final Iterator<Map<String, Object>> feedbackMap;

    public StudentOnCourseSteps() {
        feedbackMap = feedbackMap();
    }

    private Iterator<Map<String, Object>> feedbackMap() {
        return Stream.generate((Supplier<Map<String, Object>>) () -> Collections
                        .singletonMap("feedback", RandomStringUtils.randomAlphanumeric(10, 20)))
                .iterator();
    }

    public ChainBuilder authUserByEmail() {
        return CoreDsl
                .tryMax(1)
                .on(
                        exec(
                                HttpDsl
                                        .http("POST /api/login")
                                        .post("/api/auth")
                                        .body(USER_BODY)
                                        .asJson()
                                        .check(HttpDsl.status().in(200),
                                                jsonPath("$.user.students[0].id")
                                                        .saveAs("studentId"),
                                                jsonPath("$.user.students[0].activeChapterNumber")
                                                        .saveAs("activeChapterNumber"),
                                                jsonPath("$.user.id")
                                                        .saveAs("personId")))
                                .pause(1, 4));
    }

    public ChainBuilder applicationForCourse() {
        return CoreDsl
                .feed(Feeders.slugs)
                .exec(
                        HttpDsl
                                .http("POST /api/persons/application/{slug}")
                                .post("/api/persons/application/#{slug}")
                                .body(USER_BODY)
                                .asJson()
                                .check(HttpDsl.status().in(201))
                )
                .pause(2);
    }

    public ChainBuilder getCourses() {
        return CoreDsl
                .exec(
                        HttpDsl
                                .http("GET /api/courses")
                                .get("/api/courses")
                                .check(HttpDsl.status().in(200))
                )
                .pause(2);
    }

    public ChainBuilder getCoursesBySlug() {
        return CoreDsl
                .feed(Feeders.slugs)
                .exec(
                        HttpDsl
                                .http("GET /api/courses/{slug}")
                                .get("/api/courses/#{slug}")
                                .check(HttpDsl.status().in(200))
                )
                .pause(2);
    }

    public ChainBuilder getStudentsChapters() {
        return CoreDsl
                .exec(
                        HttpDsl
                                .http("GET /students/chapters")
                                .get("/api/students/chapters/#{studentId}")
                                .asJson()
                                .check(HttpDsl.status().in(200, 204),
                                        jsonPath("$[0].id")
                                                .saveAs("studentChapterId")))
                .pause(1, 4);
    }

    public ChainBuilder getChapter() {
        return CoreDsl
                .exec(
                        HttpDsl
                                .http("GET /students/chapters/{studentId}/{chapterId}")
                                .get("/api/students/chapters/#{studentId}/#{activeChapterNumber}")
                                .check(HttpDsl.status().in(200, 204)))
                .pause(1, 4);
    }

    public ChainBuilder stateChapter() {
        return CoreDsl
                .exec(
                        HttpDsl
                                .http("PUT /api/students/chapters/states/"
                                        + "{studentChapterId}/IN_PROCESS")
                                .put("/api/students/chapters/states/"
                                        + "#{studentChapterId}/IN_PROCESS")
                                .check(HttpDsl.status().in(200, 204)))
                .pause(1, 4)
                .exec(HttpDsl
                        .http("PUT /api/students/chapters/states/{studentChapterId}/PAUSE")
                        .put("/api/students/chapters/states/"
                                + "#{studentChapterId}/PAUSE")
                        .check(HttpDsl.status().in(200, 204)))
                .pause(1, 4);
    }

    public ChainBuilder statePractice() {
        return CoreDsl
                .exec(
                        HttpDsl
                                .http("PUT /api/students/practices/states/"
                                        + "{studentChapterId}/IN_PROCESS")
                                .put("/api/students/practices/states/"
                                        + "#{studentChapterId}/IN_PROCESS")
                                .check(HttpDsl.status().in(200)))
                .pause(1, 4)
                .exec(HttpDsl
                        .http("PUT /api/students/practices/states/{studentChapterId}/PAUSE")
                        .put("/api/students/practices/states/#{studentChapterId}/PAUSE")
                        .check(HttpDsl.status().in(200)))
                .pause(1, 4)
                .exec(HttpDsl
                        .http("PUT /api/students/practices/states/"
                                + "{studentChapterId}/READY_TO_REVIEW")
                        .put("/api/students/practices/states/"
                                + "#{studentChapterId}/READY_TO_REVIEW")
                        .check(HttpDsl.status().in(200)))
                .pause(1, 4);
    }

    public ChainBuilder postFeedback() {
        return CoreDsl
                .feed(feedbackMap)
                .exec(
                        HttpDsl
                                .http("POST /api/feedbacks/")
                                .post("/api/feedbacks/")
                                .body(FEEDBACK_BODY)
                                .asJson()
                                .check(HttpDsl.status().in(200),
                                        jsonPath("$.id").saveAs("feedbackId"))
                )
                .pause(1, 3);
    }

    public ChainBuilder getMe() {
        return CoreDsl
                .exec(
                        HttpDsl
                                .http("GET /api/persons/me")
                                .get("/api/persons/me")
                                .check(HttpDsl.status().in(200)))
                .pause(1, 3);
    }

    public ChainBuilder incrementLike() {
        return CoreDsl
                .exec(
                        HttpDsl
                                .http("PATCH /api/feedbacks/add/{feedbackId}")
                                .patch("/api/feedbacks/add/#{feedbackId}"
                                        + "?page=0&size=10&sortState=DATE_DESCENDING")
                                .asJson()
                                .check(HttpDsl.status().in(200)))
                .pause(1, 3);
    }

    public ChainBuilder decrementLike() {
        return CoreDsl
                .exec(
                        HttpDsl
                                .http("PATCH /api/feedbacks/remove/{feedbackId}")
                                .patch("/api/feedbacks/remove/#{feedbackId}"
                                        + "?page=0&size=10&sortState=DATE_DESCENDING")
                                .asJson()
                                .check(HttpDsl.status().in(200)))
                .pause(1, 3);
    }

    public ChainBuilder deleteFeedbackById() {
        return CoreDsl.exec(
                        HttpDsl
                                .http("DELETE /api/feedbacks/{feedbackId}")
                                .delete("/api/feedbacks/delete/#{feedbackId}"
                                        + "?page=0&size=10&sortState=DATE_DESCENDING")
                                .asJson()
                                .check(HttpDsl.status().is(200)))
                .pause(1, 3);
    }

    public ChainBuilder getCertificateInfo() {
        return CoreDsl.exec(
                HttpDsl
                        .http("GET /api/certification/{studentId}")
                        .get("/api/certification/#{studentId}")
                        .asJson()
                        .check(HttpDsl.status().is(200))
        );
    }

    // WARNING: this step can do DoS attack!!!
    /*public ChainBuilder sendCertificateRequest() {
        return CoreDsl.exec(
                HttpDsl
                        .http("POST /api/certification/request/{studentId}")
                        .post("/api/certification/request/#{studentId}")
                        .body(CERTIFICATE_DTO)
                        .asJson()
                        .check(HttpDsl.status().is(200))
        );
    }*/

    public ChainBuilder getStudentById() {
        return CoreDsl
                .feed(Feeders.persons)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET /students/{id}")
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
                                        .http("GET /students/reports/state")
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
                                        .http("GET /students/practices/{practiceState}")
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
                                        .http("GET /students/practices/states")
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
                                        .http("GET /students/practices/my")
                                        .get("/api/students/practices/my")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 5)
                ).exitHereIfFailed();
    }


    public ChainBuilder getStudentOnCourse() {
        return CoreDsl
                .feed(Feeders.courses)
                .feed(Feeders.persons)
                .tryMax(5)
                .on(exec(HttpDsl.http("GET /students?courseId=Id&studentId=Id")
                        .get("/api/students?courseId=#{courseId}&studentId=#{personId}")
                        .check(HttpDsl.status()
                                .in(200, 204)))
                        .pause(1, 4)
                )
                .exitHereIfFailed();
    }
}
