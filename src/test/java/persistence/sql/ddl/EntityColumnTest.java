package persistence.sql.ddl;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class EntityColumnTest {

    private Class<?> mockClass;

    @Test
    @DisplayName("EntityColumn Id 테스트")
    void testEntityColumnWithId() throws Exception {
        mockClass = MockEntity.WithId.class;
        final Field field = mockClass.getDeclaredField("id");
        final EntityColumn column = new EntityColumn(field);
        assertResult(column, "id", true, true, false);
    }

    @Test
    @DisplayName("EntityColumn IdAndColumn 테스트")
    void testEntityColumnWithIdAndColumn() throws Exception {
        mockClass = MockEntity.WithIdAndColumn.class;
        final Field field = mockClass.getDeclaredField("id");
        final EntityColumn column = new EntityColumn(field);
        assertResult(column, "test_id", true, true, false);
    }

    @Test
    @DisplayName("EntityColumn Id GeneratedValue 테스트")
    void testEntityColumnWithIdGeneratedValue() throws Exception {
        mockClass = MockEntity.IdWithGeneratedValue.class;
        final Field field = mockClass.getDeclaredField("id");
        final EntityColumn column = new EntityColumn(field);
        assertResult(column, "id", true, true, true);
    }

    @Test
    @DisplayName("EntityColumn WithoutColumn 테스트")
    void testEntityColumnWithoutColumn() throws Exception {
        mockClass = MockEntity.WithoutColumn.class;
        final Field field = mockClass.getDeclaredField("column");
        final EntityColumn column = new EntityColumn(field);
        assertResult(column, "column", false, false, false);
    }

    @Test
    @DisplayName("EntityColumn WithColumn 테스트")
    void testEntityColumnWithColumn() throws Exception {
        mockClass = MockEntity.WithColumn.class;
        final Field field = mockClass.getDeclaredField("column");
        final EntityColumn column = new EntityColumn(field);
        assertResult(column, "test_column", false, false, false);
    }

    @Test
    @DisplayName("EntityColumn WithColumn 테스트")
    void testEntityColumnWithColumnNonNull() throws Exception {
        mockClass = MockEntity.WithColumn.class;
        final Field field = mockClass.getDeclaredField("notNullColumn");
        final EntityColumn column = new EntityColumn(field);
        assertResult(column, "notNullColumn", false, true, false);
    }

    @Test
    @DisplayName("EntityColumn WithTransient 테스트")
    void testEntityColumnWithTransient() throws Exception {
        mockClass = MockEntity.WithTransient.class;
        final Field field = mockClass.getDeclaredField("column");
        final EntityColumn column = new EntityColumn(field);
        assertThat(column.isTransient()).isTrue();
    }

    private void assertResult(final EntityColumn result, final String fieldName, final boolean isId, final boolean isNotNull, final boolean isAutoIncrement) {
        assertThat(result.getName()).isEqualTo(fieldName);
        assertThat(result.isId()).isEqualTo(isId);
        assertThat(result.isNotNull()).isEqualTo(isNotNull);
        assertThat(result.isAutoIncrement()).isEqualTo(isAutoIncrement);
    }

}