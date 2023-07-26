package com.testing.concurrency.demo.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "course")
public class Course {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private int credits;
    private int hours;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<Person> people = new HashSet<>();

    public Course(String name, int credits, int hours) {
        this.name = name;
        this.credits = credits;
        this.hours = hours;
    }

    public boolean enrollPerson(Person person) {
        boolean added = this.people.add(person);
        person.getCourses().add(this);
        return added;
    }
    public boolean expelPerson(Person p) {
        boolean isExpelled = this.people.remove(p);
        p.getCourses().remove(this);
        return isExpelled;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Course course = (Course) o;
        return getId() != null && Objects.equals(getId(), course.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public boolean enrollPeople(List<Person> people) {
        boolean changes = this.people.addAll(people);
        people.forEach(p->p.getCourses().add(this));
        return changes;
    }
}
