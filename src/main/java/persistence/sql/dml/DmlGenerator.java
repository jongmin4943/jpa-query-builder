package persistence.sql.dml;

import persistence.core.EntityColumn;
import persistence.core.EntityMetadata;
import persistence.core.EntityMetadataCache;
import persistence.exception.PersistenceException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class DmlGenerator {

    private final EntityMetadataCache entityMetadataCache;
    private final InsertQueryBuilder insertQueryBuilder;
    private final SelectQueryBuilder selectQueryBuilder;

    public DmlGenerator() {
        this.entityMetadataCache = EntityMetadataCache.getInstance();
        this.insertQueryBuilder = new InsertQueryBuilder();
        this.selectQueryBuilder = new SelectQueryBuilder();
    }

    public String generateInsertDml(final Object entity) {
        final EntityMetadata<?> entityMetadata = entityMetadataCache.getEntityMetadata(entity.getClass());
        return insertQueryBuilder
                .table(entityMetadata.getTableName())
                .addData(entityMetadata.getInsertableColumnNames(), getEntityValues(entity, entityMetadata))
                .build();
    }

    public String generateFindAllDml(final Class<?> clazz) {
        final EntityMetadata<?> entityMetadata = entityMetadataCache.getEntityMetadata(clazz);
        return selectQueryBuilder
                .table(entityMetadata.getTableName())
                .column(entityMetadata.getColumnNames())
                .build();
    }

    public String generateFindByIdDml(final Class<?> clazz, final Object id) {
        final EntityMetadata<?> entityMetadata = entityMetadataCache.getEntityMetadata(clazz);
        return selectQueryBuilder
                .table(entityMetadata.getTableName())
                .column(entityMetadata.getColumnNames())
                .where(entityMetadata.getIdColumnName(), String.valueOf(id))
                .build();
    }

    public String generateDeleteDml(final Class<?> clazz) {
        final StringBuilder builder = new StringBuilder();
        final EntityMetadata<?> entityMetadata = entityMetadataCache.getEntityMetadata(clazz);
        builder.append("delete from ")
                .append(entityMetadata.getTableName());
        return builder.toString();
    }

    private List<String> getEntityValues(final Object entity, final EntityMetadata<?> entityMetadata) {
        return entityMetadata.getColumns()
                .stream()
                .filter(EntityColumn::isInsertable)
                .map(entityColumn -> buildValueForColumn(entity, entityColumn))
                .collect(Collectors.toList());
    }

    private String buildValueForColumn(final Object entity, final EntityColumn entityColumn) {
        try {
            final Field field = entity.getClass().getDeclaredField(entityColumn.getFieldName());
            field.setAccessible(true);
            final Object value = field.get(entity);
            return entityColumn.isStringValued() ? addSingleQuote(value) : String.valueOf(value);
        } catch (final IllegalAccessException | NoSuchFieldException e) {
            throw new PersistenceException(e);
        }
    }

    private String addSingleQuote(final Object value) {
        return "'" + value + "'";
    }

}
