package com.group.practic.gatlingtest.steps;

import static io.gatling.javaapi.core.CoreDsl.exec;

import com.group.practic.gatlingtest.Feeders;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.http.HttpDsl;

public class CoursesSteps {

    public ChainBuilder getAllCourses() {
        return CoreDsl
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/courses")
                                        .get("/api/courses")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                )
                .exitHereIfFailed();
    }

    public ChainBuilder getCourseBySlug() {
        return CoreDsl
                .feed(Feeders.slugs)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/courses/{slug}")
                                        .get("/api/courses/#{slug}")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getCourseSlugLevels() {
        return CoreDsl
                .feed(Feeders.slugs)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/courses/{slug}/levels")
                                        .get("/api/courses/#{slug}/levels")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getCourseByChapterById() {
        return CoreDsl
                .feed(Feeders.slugs)
                .feed(Feeders.chapters)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/courses/{slug}/chapters/{Id}")
                                        .get("/api/courses/#{slug}/chapters/#{chapterId}")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getAllChapters() {
        return CoreDsl
                .feed(Feeders.slugs)
                .tryMax(5)
                .on(exec(
                        HttpDsl
                                .http("GET/courses/{slug}/allchapters")
                                .get("/api/courses/#{slug}/allchapters")
                                .check(HttpDsl.status().in(200, 204)))
                        .pause(1, 5)
                ).exitHereIfFailed();
    }



    public ChainBuilder getCoursesAdditional() {
        return CoreDsl
                .feed(Feeders.slugs)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/courses/{slug}/additional")
                                        .get("/api/courses/#{slug}/additional")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(3, 7)
                ).exitHereIfFailed();
    }

    public ChainBuilder getCourseById() {
        return CoreDsl
                .feed(Feeders.courses)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/courses/{Id}/purpose")
                                        .get("/api/courses/#{courseId}/purpose")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

    public ChainBuilder getCourseDescriptionById() {
        return CoreDsl
                .feed(Feeders.courses)
                .tryMax(5)
                .on(
                        exec(
                                HttpDsl
                                        .http("GET/courses/{Id}/description")
                                        .get("/api/courses/#{courseId}/description")
                                        .check(HttpDsl.status().in(200, 204)))
                                .pause(1, 4)
                ).exitHereIfFailed();
    }

}
