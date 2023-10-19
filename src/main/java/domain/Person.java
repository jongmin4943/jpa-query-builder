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

    public Person(final Long id, final String name, final Integer age, final String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public Person(final String name, final Integer age, final String email) {
        this(null, name, age, email);
    }

    public boolean isId(final Long id) {
        return id.equals(this.id);
    }

    public boolean isName(final String name) {
        return name.equals(this.name);
    }

    public boolean isAge(final Integer age) {
        return age.equals(this.age);
    }

    public boolean isEmail(final String email) {
        return email.equals(this.email);
    }

}
