package org.pet.utils;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class ConnectionManager {
    private static final String POLL_SIZE_KEY = "db.pool.size";
    private static final String URL_KEY = "db.url";
    private static final int DEFAULT_POOL_SIZE = 10;
    private static BlockingQueue<Connection> pool;
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        loadDriver();
        initConnectionPool();
    }

    private ConnectionManager() {
    }

    private static void initConnectionPool() {
        config.setJdbcUrl(PropertiesUtil.get(URL_KEY));
        config.setMaximumPoolSize(50);
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    private static Connection open() {
//        try {
//            loadDriver();
//            return DriverManager.getConnection(PropertiesUtil.get(URL_KEY));
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
