package com.testing.concurrency.demo.services;

import com.testing.concurrency.demo.entities.Course;
import com.testing.concurrency.demo.entities.Exam;
import com.testing.concurrency.demo.entities.Person;
import com.testing.concurrency.demo.repositories.CourseRepository;
import com.testing.concurrency.demo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class SchoolService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PersonRepository personRepository;
    public static final List<Course> DEFAULT_COURSES = List.of(
            new Course("Math",6,30),
            new Course("English",3,10),
            new Course("Chemistry",8,40),
            new Course("Physics",10,50)
    );

    public void addDefaultCourses(){
        courseRepository.saveAll(DEFAULT_COURSES);
    }

    public Optional<Course> getCourseByName(String name){
        return courseRepository.findByName(name);
    }
    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }


    public void cleanDB() {
        List<Course> oldCourses = courseRepository.findAll();
        oldCourses.forEach(Course::expelEveryone);
        courseRepository.saveAll(oldCourses);
        personRepository.deleteAll();
        courseRepository.deleteAll();
    }

    public void addPeopleToCourse(String courseName, List<Person> people){
        Optional<Course> course = courseRepository.findByName(courseName);
        course.ifPresent(c -> {
            if(c.enrollPeople(people))
                courseRepository.save(c);
        });
    }

    public void addExam(int grade, Person person, Course course){
        Exam exam = new Exam(grade, person, course);
        personRepository.save(person);
        courseRepository.save(course);
    }

}
