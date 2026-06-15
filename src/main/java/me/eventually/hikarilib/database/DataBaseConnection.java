/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-10-06 00:21:21
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-06 00:25:43
 * @FilePath: src/main/java/me/eventually/hikarilib/database/DataBaseConnection.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.database;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 数据库连接统一接口，提供通用的增删改查及事务操作。
 *
 * <pre>{@code
 * DataBaseConnection db = new MySQLConnection("localhost", 3306, "mydb", "root", "pass", false);
 * List<Object[]> rows = db.select("users", "age > ?", 18);
 * db.insert("users", null, "Alice", 25);
 * }</pre>
 */
public interface DataBaseConnection {

    /**
     * 执行 SQL 查询。
     *
     * @param sql    SQL 查询语句
     * @param params 查询参数
     * @return 查询结果，每行为一个 Object 数组
     * @throws SQLException 数据库异常时抛出
     */
    @Contract(pure = true)
    List<Object[]> query(@NotNull String sql, Object... params) throws SQLException;

    /**
     * 执行 SQL 写操作（INSERT/UPDATE/DELETE）。
     *
     * @param sql    SQL 语句
     * @param params 参数
     * @return 受影响的行数
     * @throws SQLException 数据库异常时抛出
     */
    int execute(@NotNull String sql, Object... params) throws SQLException;

    /**
     * 快速查询指定表的数据。
     *
     * @param tableName   表名
     * @param whereClause WHERE 子句，如 "id = ?"
     * @param params      WHERE 子句参数
     * @return 查询结果
     * @throws SQLException 数据库异常时抛出
     */
    @Contract(pure = true)
    List<Object[]> select(@NotNull String tableName, @NotNull String whereClause, Object... params) throws SQLException;

    /**
     * 快速插入数据。
     *
     * @param tableName 表名
     * @param values    插入的值，按列顺序排列
     * @return 受影响的行数
     * @throws SQLException 数据库异常时抛出
     */
    int insert(@NotNull String tableName, Object... values) throws SQLException;

    /**
     * 设置 PreparedStatement 的参数。
     *
     * @param pstmt  PreparedStatement 对象
     * @param params 参数值
     * @throws SQLException 设置失败时抛出
     */
    default void setParameters(PreparedStatement pstmt, Object @NotNull ... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }

    /**
     * 执行事务操作。
     *
     * @param task 事务任务，返回结果
     * @param <T>  返回值类型
     * @return 事务任务的返回值
     * @throws SQLException 事务失败时抛出，自动回滚
     */
    <T> T transaction(@NotNull Callable<T> task) throws SQLException;
}
