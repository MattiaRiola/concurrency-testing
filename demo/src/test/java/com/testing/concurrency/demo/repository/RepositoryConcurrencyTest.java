package com.testing.concurrency.demo.repository;

import com.testing.concurrency.demo.entities.Course;
import com.testing.concurrency.demo.entities.Person;
import com.testing.concurrency.demo.repositories.CourseRepository;
import com.testing.concurrency.demo.repositories.PersonRepository;
import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RepositoryConcurrencyTest extends MultithreadedTestCase {

    private CourseRepository courseRepository;


    private PersonRepository personRepository;

    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void initialize() {
        System.out.println("Before all tests");
        initDB();
    }


    public void initDB() {
        courseRepository.deleteAll();
        courseRepository.save(new Course("Course 1",6,30));
        courseRepository.save(new Course("Course 2",8,40));
    }

    public void thread1() throws InterruptedException {
        var course1 = List.of(courseRepository.findByName("Course 1").get());
        for (int i = 0; i < 10; i++) {
            createPersonAndEnrollIt(course1, i);
        }
    }

    public void thread2() throws InterruptedException {
        List<Course> courses = courseRepository.findAll();

        for (int i = 10; i < 20; i++) {
            createPersonAndEnrollIt(courses, i);
        }



    }


    public void createPersonAndEnrollIt(List<Course> courses, int i) throws InterruptedException {
        Person p = personRepository.save(new Person("Person "+ i,20+ i));
        Thread.sleep(100);
        for (Course course : courses) {
            course.enrollPerson(p);
        }
    }

    @Override
    public void finish() {
        personRepository.findAll().forEach(
                p->{
                    if(p.getCourses().contains(courseRepository.findByName("Course 1").get()))
                        System.out.println(p + " was not expelled");
                }
        );
        personRepository.findAll().forEach(
                System.out::println
        );
        System.out.println("After all tests");
        personRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    public void testRepositoryWithMTC() throws Throwable {
        TestFramework.runManyTimes(new RepositoryConcurrencyTest(), 1000);
    }
}
