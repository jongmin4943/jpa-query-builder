package persistence.sql.ddl;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.lang.reflect.Field;
import java.util.Arrays;

public class DdlGenerator {

    private final DBColumnTypeMapper columnTypeMapper;

    public DdlGenerator(final DBColumnTypeMapper columnTypeMapper) {
        this.columnTypeMapper = columnTypeMapper;
    }

    public String generateCreateDdl(final Class<?> clazz) {
        final StringBuilder builder = new StringBuilder();

        final String className = getTableNameBy(clazz);
        builder.append("create table ")
                .append(className)
                .append(" ")
                .append(generateColumnsStatement(clazz));

        return builder.toString();
    }

    private String generateColumnsStatement(final Class<?> clazz) {
        final StringBuilder builder = new StringBuilder();
        builder.append("(");

        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);

            builder.append(generateColumnDefinition(field))
                    .append(",");
        });

        builder.append(generatePKConstraintStatement(clazz));

        builder.append(")");
        return builder.toString();
    }

    private String generateColumnDefinition(final Field field) {
        final StringBuilder builder = new StringBuilder();
        final String columnName = getColumnName(field);
        final String columnType = columnTypeMapper.getColumnName(field.getType());

        builder.append(columnName)
                .append(" ")
                .append(columnType)
                .append(generateNotNullStatement(field))
                .append(generateAutoIncrementStatement(field));
        return builder.toString();
    }

    private String generateAutoIncrementStatement(final Field field) {
        final GeneratedValue annotation = field.getDeclaredAnnotation(GeneratedValue.class);
        final boolean isStrategyAutoIncrement = annotation != null && annotation.strategy() == GenerationType.IDENTITY;
        if (isStrategyAutoIncrement) {
            return " auto_increment";
        }
        return "";
    }

    private String generateNotNullStatement(final Field field) {
        final Column annotation = field.getDeclaredAnnotation(Column.class);
        final boolean isNotNullable = field.isAnnotationPresent(Id.class) || (annotation != null && !annotation.nullable());
        if (isNotNullable) {
            return " not null";
        }
        return "";
    }

    private String getColumnName(final Field field) {
        final Column annotation = field.getDeclaredAnnotation(Column.class);
        final boolean isColumnNameDefined = annotation != null && !annotation.name().isEmpty();
        if (isColumnNameDefined) {
            return annotation.name();
        }

        return field.getName();
    }

    private String generatePKConstraintStatement(final Class<?> clazz) {
        final StringBuilder builder = new StringBuilder();
        final Field idField = getIdField(clazz);
        final String className = getTableNameBy(clazz);
        builder.append("CONSTRAINT PK_")
                .append(className)
                .append(" PRIMARY KEY (")
                .append(getColumnName(idField))
                .append(")");
        return builder.toString();
    }

    private Field getIdField(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow();
    }

    private String getTableNameBy(final Class<?> clazz) {
        return clazz.getSimpleName();
    }

}
