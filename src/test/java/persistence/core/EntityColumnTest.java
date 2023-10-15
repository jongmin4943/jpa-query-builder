package persistence.core;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.domain.FixtureEntity;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class EntityColumnTest {

    private Class<?> mockClass;

    @Test
    @DisplayName("Id 컬럼 정보로 EntityColumn 인스턴스를 생성 할 수 있다.")
    void testEntityColumnWithId() throws Exception {
        mockClass = FixtureEntity.WithId.class;
        final Field field = mockClass.getDeclaredField("id");
        final EntityColumn column = new EntityColumn(field);
        assertResult(column, "id", true, true, false, Long.class);
    }

    @Test
    @DisplayName("Id 컬럼인데 Column 설정으로 이름을 설정해 EntityColumn 인스턴스를 생성 할 수 있다.")
    void testEntityColumnWithIdAndColumn() throws Exception {
        mockClass = FixtureEntity.WithIdAndColumn.class;
        final Field field = mockClass.getDeclaredField("id");
        final EntityColumn column = new EntityColumn(field);
        assertResult(column, "test_id", true, true, false, Long.class);
    }

    @Test
    @DisplayName("Id 컬럼의 GeneratedValue 를 이용하면 not null 과 autoIncrement 값이 true 인 EntityColumn 인스턴스를 생성 할 수있다.")
    void testEntityColumnWithIdGeneratedValue() throws Exception {
        mockClass = FixtureEntity.IdWithGeneratedValue.class;
        final Field field = mockClass.getDeclaredField("id");
        final EntityColumn column = new EntityColumn(field);
        assertResult(column, "id", true, true, true, Long.class);
    }

    @Test
    @DisplayName("일반 필드를 이용해 EntityColumn 인스턴스를 생성 할 수 있다.")
    void testEntityColumnWithoutColumn() throws Exception {
        mockClass = FixtureEntity.WithoutColumn.class;
        final Field field = mockClass.getDeclaredField("column");
        final EntityColumn column = new EntityColumn(field);
        assertResult(column, "column", false, false, false, String.class);
    }

    @Test
    @DisplayName("일반 필드에 @Cloumn 을 이용해 이름 설정해 EntityColumn 인스턴스를 생성 할 수 있다.")
    void testEntityColumnWithColumn() throws Exception {
        mockClass = FixtureEntity.WithColumn.class;
        final Field field = mockClass.getDeclaredField("column");
        final EntityColumn column = new EntityColumn(field);
        assertResult(column, "test_column", false, false, false, String.class);
    }

    @Test
    @DisplayName("일반 필드에 @Cloumn 을 이용해 NotNull 이 true 인 EntityColumn 인스턴스를 생성 할 수 있다.")
    void testEntityColumnWithColumnNonNull() throws Exception {
        mockClass = FixtureEntity.WithColumn.class;
        final Field field = mockClass.getDeclaredField("notNullColumn");
        final EntityColumn column = new EntityColumn(field);
        assertResult(column, "notNullColumn", false, true, false, String.class);
    }

    @Test
    @DisplayName("일반 필드에 @Transient 를 붙이면 isTransient 가 true 인 EntityColumn 인스턴스를 생성 할 수 있다.")
    void testEntityColumnWithTransient() throws Exception {
        mockClass = FixtureEntity.WithTransient.class;
        final Field field = mockClass.getDeclaredField("column");
        final EntityColumn column = new EntityColumn(field);
        assertThat(column.isTransient()).isTrue();
    }

    private void assertResult(final EntityColumn result,
                              final String fieldName,
                              final boolean isId,
                              final boolean isNotNull,
                              final boolean isAutoIncrement,
                              final Class<?> type) {
        assertSoftly(softly -> {
            softly.assertThat(result.getName()).isEqualTo(fieldName);
            softly.assertThat(result.isId()).isEqualTo(isId);
            softly.assertThat(result.isNotNull()).isEqualTo(isNotNull);
            softly.assertThat(result.isAutoIncrement()).isEqualTo(isAutoIncrement);
            softly.assertThat(result.getType()).isEqualTo(type);
        });

    }

}