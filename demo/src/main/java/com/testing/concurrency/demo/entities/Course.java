package com.testing.concurrency.demo.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "course", uniqueConstraints = {
        @UniqueConstraint(name = "uc_course_name", columnNames = {"name"})
})
public class Course {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private int credits;
    private int hours;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "person_enrolled_courses",
            joinColumns = @JoinColumn(name = "id"))
    private Set<Person> people = new HashSet<>();


    @OneToMany
    private Set<Exam> exams = new LinkedHashSet<>();

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
    public boolean enrollPeople(List<Person> people) {
        boolean changes = this.people.addAll(people);
        people.forEach(p->p.getCourses().add(this));
        return changes;
    }
    public boolean expelPerson(Person p) {
        System.out.println("Expelling person " + p.getName() + " from course " + this.getName());
        boolean isExpelled = this.people.remove(p);
        p.getCourses().remove(this);
        return isExpelled;
    }

    public void expelEveryone(){
        this.people.forEach(p->p.getCourses().remove(this));
        this.people.clear();
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "credits = " + credits + ", " +
                "hours = " + hours + ")" +
                "people = " + people.stream().map(Person::getName).collect(Collectors.toSet())
                ;
    }
}
