package com.testing.concurrency.demo.service;

import com.testing.concurrency.demo.services.SchoolService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SchoolCoursesTest {
    @Autowired
    private SchoolService schoolService;

    @BeforeEach
    protected void setUp() {
        schoolService.cleanDB();
    }

    @Test
    public void addDefaultCourses() {
        schoolService.addDefaultCourses();
        assertNotEquals(0, schoolService.getAllCourses().size());
        SchoolService.DEFAULT_COURSES.forEach(course ->
            assertTrue( schoolService.getCourseByName(course.getName()).isPresent()));
    }

    @Test
    public void addDefaultCoursesMultipleTimes(){
        schoolService.addDefaultCourses();
        schoolService.addDefaultCourses();
        schoolService.addDefaultCourses();
        assertNotEquals(0, schoolService.getAllCourses().size());
        assertEquals(SchoolService.DEFAULT_COURSES.size(), schoolService.getAllCourses().size());

        SchoolService.DEFAULT_COURSES.forEach(course ->
                assertTrue( schoolService.getCourseByName(course.getName()).isPresent()));
    }

}
