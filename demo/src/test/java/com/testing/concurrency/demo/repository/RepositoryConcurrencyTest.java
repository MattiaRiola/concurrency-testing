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
            createPersonAndEnrollIt(course1, new Person("Person "+ i,20+ i));
        }
    }

    public void thread2() throws InterruptedException {
        List<Course> courses = courseRepository.findAll();

        for (int i = 10; i < 20; i++) {
            createPersonAndEnrollIt(courses, new Person("Person "+ i,20+ i));
        }
    }

    public void thread3() throws InterruptedException{
        System.out.println("Thread 3");
        Course course = courseRepository.findByName("Course 1").get();
        List<String> expelledFromCourse1 = new LinkedList<>();
        for (int i = 5; i < 15; i++) {
            personRepository.findByName("Person "+i).ifPresent(p->{
                course.expelPerson(p);
                personRepository.save(p);
                expelledFromCourse1.add(p.getName());
            });
        }
        System.out.println("Expelled from course 1: " + expelledFromCourse1);
    }


    public void createPersonAndEnrollIt(List<Course> courses, Person p) throws InterruptedException {
        Thread.sleep(100);
        for (Course course : courses) {
            course.enrollPerson(p);
        }
        personRepository.save(p);
    }

    @Override
    public void finish() {
        List<Person> people = personRepository.findAll();
        people.stream().filter(p->p.getCourses().isEmpty()).forEach(p->{
            System.out.println("Person " + p.getName() + " has no courses");
        });
        long numOfExpelledPeople = people.stream()
                .filter( p->
                        p.getCourses().stream()
                                .anyMatch(c->c.getName().equals("Course 1"))
                ).count();

        System.out.println("Number of people expelled from course 1: " + numOfExpelledPeople);

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
