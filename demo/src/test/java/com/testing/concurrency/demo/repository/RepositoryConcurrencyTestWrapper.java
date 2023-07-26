package com.testing.concurrency.demo.repository;

import com.testing.concurrency.demo.repositories.CourseRepository;
import com.testing.concurrency.demo.repositories.PersonRepository;
import edu.umd.cs.mtc.TestFramework;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RepositoryConcurrencyTestWrapper {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void init(){
        courseRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    public void testRepositoryConcurrency() throws Throwable {
        var test = new RepositoryConcurrencyTest();
        test.setPersonRepository(personRepository);
        test.setCourseRepository(courseRepository);
        test.initDB();
        TestFramework.runManyTimes(test, 10);
    }

}
