package org.pet.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionManager {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        initConnectionPool();
    }

    private ConnectionManager() {
    }

    private static void initConnectionPool() {
        String root = System.getProperty("catalina.base");
        String rootPath = root + "/exchangeDB/Currencies.db";
        config.setJdbcUrl("jdbc:sqlite:" + rootPath);
        config.setMaximumPoolSize(15);
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
