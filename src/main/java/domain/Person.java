package domain;

import jakarta.persistence.*;

@Table(name = "users")
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nick_name")
    private String name;

    @Column(name = "old")
    private Integer age;

    @Column(nullable = false)
    private String email;

    @Transient
    private Integer index;

    protected Person() {
    }

    public Person(final String name, final Integer age, final String email, final Integer index) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.index = index;
    }

    public Person(final String name, final Integer age, final String email) {
        new Person(name, age, email, 0);
    }
}
