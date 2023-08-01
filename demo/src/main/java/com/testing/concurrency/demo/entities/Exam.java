package com.testing.concurrency.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private int grade;

    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    public Exam(int grade, Person person, Course course) {
        this.grade = grade;

        if(person == null || course == null)
            throw new IllegalArgumentException("Person and course cannot be null");
        if(!person.getCourses().contains(course))
            throw new IllegalArgumentException(String.format("Person %s is not enrolled in course %s", person.getName(), course.getName()));

        person.getExams().removeIf(exam -> exam.getCourse().equals(course));

        this.person = person;
        person.getExams().add(this);
        this.course = course;
        course.getExams().add(this);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Exam exam = (Exam) o;
        return getId() != null && Objects.equals(getId(), exam.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
