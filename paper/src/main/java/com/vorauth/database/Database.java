package com.vorauth.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Database {

    private final MySQLManager mysql;

    public Database(MySQLManager mysql) {
        this.mysql = mysql;
    }

    public CompletableFuture<Boolean> playerExists(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {

            String sql = "SELECT * FROM users WHERE uuid = ?";

            try (Connection conn = mysql.getDataSource().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, uuid.toString());

                ResultSet rs = ps.executeQuery();
                return rs.next();

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> createUser(UUID uuid, String passwordHash) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "INSERT INTO users (uuid, password, discord_code, discord_id, created_at) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = mysql.getDataSource().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, uuid.toString());
                ps.setString(2, passwordHash);
                ps.setNull(3, Types.INTEGER);
                ps.setNull(4, Types.BIGINT);
                ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

                return ps.executeUpdate() > 0;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> createTablesIfNotExist() {
        return CompletableFuture.supplyAsync(() -> {
            String sql = """
                CREATE TABLE IF NOT EXISTS users (
                  uuid VARCHAR(36) NOT NULL,
                  password VARCHAR(100) NOT NULL,
                  discord_code INT NULL,
                  discord_id BIGINT NULL,
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  PRIMARY KEY (uuid)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """;

            try (Connection conn = mysql.getDataSource().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }
}
