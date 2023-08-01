package com.testing.concurrency.demo.service;

import com.testing.concurrency.demo.entities.Course;
import com.testing.concurrency.demo.entities.Person;
import com.testing.concurrency.demo.services.SchoolService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SchoolEnrollmentTest {
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
        schoolService.addPeopleToCourse("Math", List.of(new Person("John",18), new Person("Mary",20)));
        Optional<Course> math = schoolService.getCourseByName("Math");
        assertTrue(math.isPresent());
        assertEquals(2, math.get().getPeople().size());
    }

}
