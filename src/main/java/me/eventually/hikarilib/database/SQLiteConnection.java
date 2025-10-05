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

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * A wrapped class for SQLite database connection.
 * Use {@link #getDataSource()} to get the datasource, and you can use it to do anything you want
 * @author Eventually
 */
@Getter
public class SQLiteConnection implements DataBaseConnection{
    /**
     * -- GETTER --
     *  Get the datasource, and you can use it to do anything you want
     *
     */
    private final HikariDataSource dataSource;

    /**
     * Constructs a new SQLiteConnection instance.
     *
     * @param path The path to the SQLite database file.
     */
    public SQLiteConnection(String path) {
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
     * Common query method
     * @param sql SQL query command
     * @param params SQL query parameters
     * @return Query result
     * @throws SQLException handle it yourself
     */
    @Override
    public List<Object[]> query(String sql, Object... params) throws SQLException {
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
     * Common sql command execute method
     * @param sql SQL command
     * @param params SQL command parameters
     * @return rows affected, 0 means no change
     * @throws SQLException handle it yourself
     */
    @Override
    public int execute(String sql, Object... params) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParameters(pstmt, params);
            return pstmt.executeUpdate();
        }
    }

    /**
     * Fast way to select data
     * @param tableName table name
     * @param whereClause sql where clause, like "id = ?"
     * @param params parameters for where clause
     * @return query result
     * @throws SQLException handle it yourself
     */
    @Override
    public List<Object[]> select(String tableName, String whereClause, Object... params) throws SQLException {
        return query("SELECT * FROM " + tableName + " WHERE " + whereClause, params);
    }

    /**
     * Fast way to insert data
     * @param tableName table name
     * @param values values to insert
     * @return rows affected
     * @throws SQLException handle
     */
    @Override
    public int insert(String tableName, Object... values) throws SQLException {
        String placeholders = String.join(",",
                Collections.nCopies(values.length, "?"));
        return execute("INSERT INTO " + tableName + " VALUES(" + placeholders + ")", values);
    }

    /**
     * Fast way to do transaction
     * @param task task to run
     * @throws SQLException
     */
    @Override
    public <T> T transaction(Callable<T> task) throws SQLException {
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
