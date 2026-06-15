/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-10-06 00:16:59
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-06 00:25:02
 * @FilePath: src/main/java/me/eventually/hikarilib/database/SQLiteConnection.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * SQLite 数据库连接实现，基于 HikariCP 连接池。
 *
 * <pre>{@code
 * SQLiteConnection db = new SQLiteConnection("plugins/MyPlugin/data.db");
 * List<Object[]> rows = db.query("SELECT * FROM users WHERE age > ?", 18);
 * }</pre>
 */
@Getter
public class SQLiteConnection implements DataBaseConnection{
    private final HikariDataSource dataSource;

    /**
     * 创建 SQLite 连接。
     *
     * @param path SQLite 数据库文件路径
     */
    public SQLiteConnection(@NotNull String path) {
        HikariConfig config = new HikariConfig();
        // SQLite JDBC URL format: jdbc:sqlite:path_to_db_file
        config.setJdbcUrl("jdbc:sqlite:" + path);
        // SQLite does not require username or password
        // Pool settings can be adjusted as needed
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        this.dataSource = new HikariDataSource(config);
    }

    /**
     * 执行 SQL 查询。
     *
     * @param sql    SQL 查询语句
     * @param params 查询参数
     * @return 查询结果
     * @throws SQLException 数据库异常时抛出
     */
    @Override
    public List<Object[]> query(@NotNull String sql, Object... params) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParameters(pstmt, params);
            ResultSet rs = pstmt.executeQuery();

            List<Object[]> results = new ArrayList<>();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                results.add(row);
            }
            return results;
        }
    }

    /**
     * 执行 SQL 写操作。
     *
     * @param sql    SQL 语句
     * @param params 参数
     * @return 受影响的行数
     * @throws SQLException 数据库异常时抛出
     */
    @Override
    public int execute(@NotNull String sql, Object... params) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParameters(pstmt, params);
            return pstmt.executeUpdate();
        }
    }

    /**
     * 快速查询。
     *
     * @param tableName   表名
     * @param whereClause WHERE 子句
     * @param params      参数
     * @return 查询结果
     * @throws SQLException 数据库异常时抛出
     */
    @Override
    public List<Object[]> select(@NotNull String tableName, @NotNull String whereClause, Object... params) throws SQLException {
        return query("SELECT * FROM " + tableName + " WHERE " + whereClause, params);
    }

    /**
     * 快速插入。
     *
     * @param tableName 表名
     * @param values    插入的值
     * @return 受影响的行数
     * @throws SQLException 数据库异常时抛出
     */
    @Override
    public int insert(@NotNull String tableName, Object... values) throws SQLException {
        String placeholders = String.join(",",
                Collections.nCopies(values.length, "?"));
        return execute("INSERT INTO " + tableName + " VALUES(" + placeholders + ")", values);
    }

    /**
     * 执行事务。
     *
     * @param task 事务任务
     * @return 事务任务的返回值
     * @throws SQLException 事务失败时抛出，自动回滚
     */
    @Override
    public <T> T transaction(@NotNull Callable<T> task) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                T result = task.call();
                conn.commit();
                return result;
            } catch (Exception e) {
                conn.rollback();
                throw new SQLException(e);
            }
        }
    }

}
