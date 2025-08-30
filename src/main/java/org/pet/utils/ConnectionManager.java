package org.pet.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.pet.exception.DataBaseConnectException;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionManager {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    private static final String PATH = "\\exchangeDB\\Currencies.db";
    private static final int maximumPoolSize = 15;

    static {
        initConnectionPool();
    }

    private ConnectionManager() {
        throw new ArrayStoreException("Нельзя создать объект статического класса");
    }

    private static void initConnectionPool() {
        String root = System.getProperty("catalina.base");
        String rootPath = root + PATH;
        config.setJdbcUrl("jdbc:sqlite:" + rootPath);
        config.setMaximumPoolSize(maximumPoolSize);
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new DataBaseConnectException("Database is not available");
        }
    }
}
