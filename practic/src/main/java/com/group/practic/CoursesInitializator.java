package com.group.practic;

import com.group.practic.service.CourseService;
import jakarta.annotation.PostConstruct;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class CoursesInitializator {

    public static final String COURSE_MASK = ".course";

    public static final String COURSE_PROPERTY_FOLDER = "./";

    CourseService courseService;

    @Value("${refreshDB_on_start}")
    boolean refresh;


    @Autowired
    CoursesInitializator(CourseService courseService) {
        this.courseService = courseService;
    }


    @PostConstruct
    void initialize() {
        if (refresh) {
            File[] files =
                    new File(".").listFiles(f -> f.isFile() && f.getName().endsWith(COURSE_MASK));
            for (File file : files) {
                initCourse(file.getName());
            }
        }
    }


    public boolean initCourse(String filename) {
        if (PropertyLoader.isComprehendedString(filename)) {
            PropertyLoader prop = new PropertyLoader(new File(filename));
            return prop.initialized && courseService.createOrUpdate(prop).isPresent();
        }
        return false;
    }

}
