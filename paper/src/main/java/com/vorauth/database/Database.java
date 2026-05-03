package com.vorauth.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

public class Database {

    private final MySQLManager mysql;

    public Database(MySQLManager mysql) {
        this.mysql = mysql;
    }

    public CompletableFuture<Boolean> playerExists(String name) {
        return CompletableFuture.supplyAsync(() -> {

            String sql = "SELECT * FROM users WHERE name = ?";

            try (Connection conn = mysql.getDataSource().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, name);

                ResultSet rs = ps.executeQuery();
                return rs.next();

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
}