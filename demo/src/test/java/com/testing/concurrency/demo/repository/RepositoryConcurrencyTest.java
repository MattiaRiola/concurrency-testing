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
import java.util.Set;
import java.util.stream.Collectors;

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
        personRepository.deleteAll();
        courseRepository.deleteAll();
        courseRepository.save(new Course("Course 1",1,10));
        courseRepository.save(new Course("Course 2",2,20));
        courseRepository.save(new Course("Course 3",3,30));
        List<Course> courses = courseRepository.findAll();
        List<Person> people = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Person p = new Person("Person "+ i,20+ i);
            courses.forEach(course -> course.enrollPerson(p));
            people.add(p);
        }
        personRepository.saveAll(people);
    }

    public void thread1(){
        System.out.println("Thread 1 start");
        List<Person> people = personRepository.findAll();
        courseRepository.findByName("Course 1").ifPresent(course -> {
            people.forEach(p -> {
                course.expelPerson(p);
                personRepository.save(p);
            });
            System.out.println("Saving "+course.getName()+" after the expulsions");
            courseRepository.save(course);
            System.out.println("Saved course "+course.getName());
        });
        System.out.println("Thread 1 end");
    }

    public void thread2(){
        System.out.println("Thread 2 start");
        List<Person> people = personRepository.findAll();
        courseRepository.findByName("Course 2").ifPresent(course -> {
            people.forEach(p -> {
                course.expelPerson(p);
                personRepository.save(p);
            });
            System.out.println("Saving "+course.getName()+" after the expulsions");
            courseRepository.save(course);
            System.out.println("Saved course "+course.getName());
        });
        System.out.println("Thread 2 end");
    }

    public void thread3(){
        System.out.println("Thread 3 start");
        List<Person> people = personRepository.findAll();
        courseRepository.findByName("Course 3").ifPresent(course -> {
            people.forEach(p -> {
                course.expelPerson(p);
                personRepository.save(p);
            });
            System.out.println("Saving "+course.getName()+" after the expulsions");
            courseRepository.save(course);
            System.out.println("Saved course "+course.getName());
        });
        System.out.println("Thread 3 end");
    }

    @Override
    public void finish() {
        List<Person> people = personRepository.findAll();

        var courses = courseRepository.findAll();
        for (Course course : courses) {
            Set<Person> notExpelledPeople = people.stream()
                    .filter( p->
                            p.getCourses().stream()
                                    .anyMatch(c->c.getName().equals(course.getName()))
                    ).collect(Collectors.toSet());
            assertTrue("Not expelled people should be empty, expelled people from " +
                            "course " + course.getName() + " are: " + notExpelledPeople,
                            notExpelledPeople.isEmpty());
        }

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
