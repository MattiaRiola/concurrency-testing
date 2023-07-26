package com.testing.concurrency.demo.repository;

import com.testing.concurrency.demo.entities.Course;
import com.testing.concurrency.demo.entities.Person;
import com.testing.concurrency.demo.repositories.CourseRepository;
import com.testing.concurrency.demo.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RepositorySingleThreadTests {
	@Autowired
	private	CourseRepository courseRepository;

	@Autowired
	private PersonRepository personRepository;

	@BeforeEach
	void init() {
		System.out.println("Before all tests");
		courseRepository.deleteAll();
		courseRepository.save(new Course("Course 1",6,30));
		courseRepository.save(new Course("Course 2",8,40));
	}


	@AfterEach
	void tearDown() {
		System.out.println("After all tests");
		personRepository.deleteAll();
		courseRepository.deleteAll();
	}

	@Test
	void testingPersonAndCourses() {
		Person p1 = personRepository.save(new Person("Person 1",20));
		Person p2 = personRepository.save(new Person("Person 2",30));
		List<Course> courses = courseRepository.findAll();
		courses.forEach(c->c.enrollPerson(p1));
		courseRepository.findById(1L).ifPresent(c->c.enrollPerson(p2));

		personRepository.save(p1);
		personRepository.save(p2);

		for (Person person : personRepository.findAll()) {
			assertFalse(person.getCourses().isEmpty());
			if(person.getName().equals("Person 1"))
				assertEquals(2, person.getCourses().size());
			if(person.getName().equals("Person 2"))
				assertEquals(1, person.getCourses().size());
		}
	}

}
