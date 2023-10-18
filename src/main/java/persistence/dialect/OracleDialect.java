package persistence.dialect;

public class OracleDialect implements Dialect {
    private final PagingStrategy pagingStrategy;
    private final DBColumnTypeMapper dbColumnTypeMapper;

    public OracleDialect(final PagingStrategy pagingStrategy, final DBColumnTypeMapper dbColumnTypeMapper) {
        this.pagingStrategy = RownumPagingStrategy.getInstance();
        // 임시
        this.dbColumnTypeMapper = OracleColumnTypeMapper.getInstance();
    }

    @Override
    public PagingStrategy getPagingStrategy() {
        return this.pagingStrategy;
    }

    @Override
    public DBColumnTypeMapper getColumnTypeMapper() {
        return this.dbColumnTypeMapper;
    }
}