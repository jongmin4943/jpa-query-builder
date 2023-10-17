package persistence.sql.dml;


import domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DmlGeneratorTest {

    private DmlGenerator generator;

    @BeforeEach
    void setUp() {
        this.generator = new DmlGenerator();
    }

    @Test
    @DisplayName("주어진 person 인스턴스를 이용해 insert ddl 을 생성할 수 있다.")
    void dmlGeneratorTest() {
        final String name = "min";
        final int age = 30;
        final String email = "jongmin4943@gmail.com";
        final Person person = new Person(name, age, email);

        final String query = generator.insert(person);

        assertThat(query).isEqualToIgnoringCase(String.format("insert into users (nick_name, old, email) values ('%s', %d, '%s')", name, age, email));
    }

    @Test
    @DisplayName("Person 클래스 정보로 select ddl 을 생성할 수 있다.")
    void findAllTest() {
        final String query = generator.findAll(Person.class);
        assertThat(query).isEqualToIgnoringCase("select id, nick_name, old, email from users");
    }

    @Test
    @DisplayName("Person 클래스 정보로 select ddl 을 생성할 수 있다.")
    void findByIdTest() {
        final String query = generator.findById(Person.class, 1L);
        assertThat(query).isEqualToIgnoringCase("select id, nick_name, old, email from users where id=1");
    }

    @Test
    @DisplayName("Person 클래스 정보로 delete ddl 을 생성할 수 있다.")
    void generateDeleteDmlTest() {
        final String query = generator.delete(Person.class);
        assertThat(query).isEqualToIgnoringCase("delete from users");
    }

}
