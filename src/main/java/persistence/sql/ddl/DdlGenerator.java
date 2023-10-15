package persistence.sql.ddl;

public class DdlGenerator {

    private final DBColumnTypeMapper columnTypeMapper;

    public DdlGenerator(final DBColumnTypeMapper columnTypeMapper) {
        this.columnTypeMapper = columnTypeMapper;
    }

    public String generateCreateDdl(final EntityMetadata<?> entityMetadata) {
        final StringBuilder builder = new StringBuilder();

        final String tableName = entityMetadata.getTableName();
        builder.append("create table ")
                .append(tableName)
                .append(" ")
                .append(generateColumnsClause(entityMetadata));

        return builder.toString();
    }

    private String generateColumnsClause(final EntityMetadata<?> entityMetadata) {
        final StringBuilder builder = new StringBuilder();
        builder.append("(");

        entityMetadata.getColumns().forEach(column -> {
            if(column.isTransient()) {
                return;
            }

            builder.append(generateColumnDefinition(column))
                    .append(",");
        });

        builder.append(generatePKConstraintClause(entityMetadata));

        builder.append(")");
        return builder.toString();
    }

    private String generateColumnDefinition(final EntityColumn column) {
        final StringBuilder builder = new StringBuilder();
        final String columnName = column.getName();
        final String columnType = columnTypeMapper.getColumnName(column.getType());

        builder.append(columnName)
                .append(" ")
                .append(columnType)
                .append(generateNotNullClause(column))
                .append(generateAutoIncrementClause(column));
        return builder.toString();
    }

    private String generateAutoIncrementClause(final EntityColumn column) {
        if (column.isAutoIncrement()) {
            return " auto_increment";
        }

        return "";
    }

    private String generateNotNullClause(final EntityColumn column) {
        if (column.isNotNull()) {
            return " not null";
        }

        return "";
    }

    private String generatePKConstraintClause(final EntityMetadata<?> entityMetadata) {
        final StringBuilder builder = new StringBuilder();
        builder.append("CONSTRAINT PK_")
                .append(entityMetadata.getTableName())
                .append(" PRIMARY KEY (")
                .append(entityMetadata.getIdColumnName())
                .append(")");
        return builder.toString();
    }

    public String generateDropDdl(final EntityMetadata<?> entityMetadata) {
        final StringBuilder builder = new StringBuilder();
        builder.append("drop table ")
                .append(entityMetadata.getTableName());

        return builder.toString();
    }
}
