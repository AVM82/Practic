package com.group.practic.gatlingtest;

import com.group.practic.gatlingtest.steps.CoursesSteps;
import com.group.practic.gatlingtest.steps.PersonSteps;
import com.group.practic.gatlingtest.steps.Steps;
import com.group.practic.gatlingtest.steps.StudentOnCourseSteps;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;

public class Scenario {
    private static final Steps steps = new Steps();
    private static final StudentOnCourseSteps studentSteps = new StudentOnCourseSteps();
    private static final CoursesSteps coursesSteps = new CoursesSteps();
    private static final PersonSteps personSteps = new PersonSteps();

    public static ScenarioBuilder userScenario = CoreDsl.scenario("user running site")
            .exec(steps.getFirstPage())
            .exec(studentSteps.authUserByEmail())
            .exec(studentSteps.getCourses())
            .exec(studentSteps.getCoursesBySlug())
            .exec(studentSteps.applicationForCourse())
            .exec(studentSteps.getStudentsChapters())
            .exec(studentSteps.getChapter())
            .exec(studentSteps.stateChapter())
            .exec(studentSteps.statePractice())
            .exec(studentSteps.postFeedback())
            .exec(studentSteps.getMe())
            .exec(studentSteps.incrementLike())
            .exec(studentSteps.decrementLike())
            .exec(studentSteps.deleteFeedbackById())
            .exec(studentSteps.getCertificateInfo())

            .exec(coursesSteps.getCoursesAdditional())
            .exec(coursesSteps.getCourseBySlug());


    public static ScenarioBuilder adminScenario = CoreDsl.scenario("admin running site")
            .exec(coursesSteps.getAllCourses())
            .exec(coursesSteps.getCourseBySlug())
            .exec(coursesSteps.getCourseSlugLevels())
            .exec(coursesSteps.getCourseByChapterById())
            .exec(coursesSteps.getAllChapters())
            .exec(coursesSteps.getCoursesAdditional())
            .exec(coursesSteps.getCourseById())
            .exec(coursesSteps.getCourseDescriptionById())

            .exec(personSteps.getPersonById())
            .exec(personSteps.getPersonsIdRoles())
            // .exec(personSteps.getPersonsProfile());//error 500
            .exec(personSteps.getPersonsMe())
            .exec(personSteps.getPersonsApplicants())
            .exec(personSteps.getPersonByName())

            .exec(steps.getFirstPage())

            .exec(steps.getChapterById())
            .exec(steps.getTopicReportsByIdChapter())
            .exec(steps.getTopicsReports())

            //.exec(studentSteps.getStudentOnCourse())
            .exec(studentSteps.getStudentsChapters())
            //.exec(studentSteps.getStudentById())
            .exec(studentSteps.getStudentsStates())
            .exec(studentSteps.getStudentPracticesByState())
            .exec(studentSteps.getAllPracticeStates())
            .exec(studentSteps.getStudentsPracticesMy());

    public static ScenarioBuilder visitorsScenario = CoreDsl.scenario("visitors visit the site")
            .exec(coursesSteps.getCourseById())
            .exec(coursesSteps.getAllCourses())
            .exec(coursesSteps.getCourseById())
            .exec(coursesSteps.getAllCourses());
}
