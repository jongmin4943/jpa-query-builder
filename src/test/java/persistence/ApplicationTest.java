package persistence;

import database.DatabaseServer;
import database.H2;
import domain.Person;
import jdbc.JdbcTemplate;
import jdbc.RowMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.core.EntityMetadata;
import persistence.core.EntityMetadataProvider;
import persistence.sql.ddl.DdlGenerator;
import persistence.sql.ddl.DefaultDBColumnTypeMapper;
import persistence.sql.dml.DmlGenerator;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest {
    private DatabaseServer server;
    private JdbcTemplate jdbcTemplate;
    private DdlGenerator ddlGenerator;
    private DmlGenerator dmlGenerator;
    private EntityMetadata<?> entityMetadata;
    private List<Person> people;

    @BeforeEach
    void setup() throws SQLException {
        server = new H2();
        server.start();
        jdbcTemplate = new JdbcTemplate(server.getConnection());
        ddlGenerator = new DdlGenerator(DefaultDBColumnTypeMapper.getInstance());
        dmlGenerator = new DmlGenerator();
        entityMetadata = EntityMetadataProvider.getInstance().getEntityMetadata(Person.class);
        final String createDdl = ddlGenerator.generateCreateDdl(entityMetadata);
        jdbcTemplate.execute(createDdl);
        people = createDummyUsers();
        people.forEach(person -> jdbcTemplate.execute(dmlGenerator.insert(person)));
    }

    @AfterEach
    void tearDown() {
        final String dropDdl = ddlGenerator.generateDropDdl(entityMetadata);
        jdbcTemplate.execute(dropDdl);
        server.stop();
    }

    @Test
    @DisplayName("쿼리 실행을 통해 데이터를 여러 row 를 넣어 정상적으로 나오는지 확인할 수 있다.")
    void findAllTest() {
        final List<Person> result =
                jdbcTemplate.query(dmlGenerator.findAll(Person.class), personRowMapper());

        assertThat(result).hasSize(people.size());
    }

    @Test
    @DisplayName("findById 를 통해 원하는 row 를 찾을 수 있다.")
    void findByIdTest() {
        final Person result =
                jdbcTemplate.queryForObject(dmlGenerator.findById(Person.class, 1L), personRowMapper());

        assertThat(result).isNotNull();
    }

    private RowMapper<Person> personRowMapper() {
        return rs -> new Person(rs.getLong("id"), rs.getString("nick_name"), rs.getInt("old"), rs.getString("email"));
    }

    private static List<Person> createDummyUsers() {
        final Person test00 = new Person("test00", 0, "test00@gmail.com");
        final Person test01 = new Person("test01", 10, "test01@gmail.com");
        final Person test02 = new Person("test02", 20, "test02@gmail.com");
        final Person test03 = new Person("test03", 30, "test03@gmail.com");
        return List.of(test00, test01, test02, test03);
    }
}
