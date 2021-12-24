package cn.sy.db.multiDataSource;

public class DbContextHolder {
    public enum DbType {
        MASTER
    }

    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<>();

    public static void setDbType(DbType dbType) {
        if (dbType == null) {
            throw new NullPointerException();
        }

        contextHolder.set(dbType);
    }

    public static DbType getDbType() {
        return contextHolder.get() == null ? DbType.MASTER : contextHolder.get();
    }

    public static void clearType() {
        contextHolder.remove();
    }
}
