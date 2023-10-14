package persistence.sql.ddl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DdlGeneratorTest {
    DBColumnTypeMapper columnTypeMapper;
    DdlGenerator generator;
    @BeforeEach
    void setUp() {
        columnTypeMapper = new DefaultDBColumnTypeMapper();
        generator = new DdlGenerator(columnTypeMapper);
    }

    @Test
    @DisplayName("create 쿼리 생성 테스트")
    void generateCreateDdlTest() {
        final String query = generator.generateCreateDdl(Person.class);

        assertThat(query).isEqualToIgnoringCase("create table users (id bigint not null auto_increment,nick_name varchar,old int,email varchar not null,CONSTRAINT PK_Users PRIMARY KEY (id))");
    }

    @Test
    @DisplayName("drop 쿼리 생성 테스트")
    void generateDropDdlTest() {
        final String query = generator.generateDropDdl(Person.class);

        assertThat(query).isEqualToIgnoringCase("drop table users");
    }
}
