package com.group.practic.gatlingtest.steps;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.exec;

import com.group.practic.gatlingtest.Feeders;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.http.HttpDsl;

public class StudentOnCourseSteps {

    public ChainBuilder authUserByEmail(){
        return CoreDsl
                .exec(
                        HttpDsl
                                .http("login")
                                .post("/api/auth")
                                .body(StringBody("""
                                        {
                                        "email": "gatling@shpp.org",
                                        "password": "gatling"
                                        }
                                        """))
                                .asJson()
                                .check(HttpDsl.status().in(200))
                )
                .pause(2);
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
