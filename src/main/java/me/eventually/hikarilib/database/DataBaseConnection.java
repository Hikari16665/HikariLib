/**
 * @Author: Eventually contact@hikari.bond
 * @Date: 2025-10-06 00:21:21
 * @LastEditors: Eventually contact@hikari.bond
 * @LastEditTime: 2025-10-06 00:25:43
 * @FilePath: src/main/java/me/eventually/hikarilib/database/DataBaseConnection.java
 * @Description: This file is licensed under MIT license
 */
package me.eventually.hikarilib.database;

import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public interface DataBaseConnection {
    List<Object[]> query(String sql, Object... params) throws SQLException;

    int execute(String sql, Object... params) throws SQLException;

    List<Object[]> select(String tableName, String whereClause, Object... params) throws SQLException;

    int insert(String tableName, Object... values) throws SQLException;

    default void setParameters(PreparedStatement pstmt, Object @NotNull ... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }

    <T> T transaction(Callable<T> task) throws SQLException;
}
