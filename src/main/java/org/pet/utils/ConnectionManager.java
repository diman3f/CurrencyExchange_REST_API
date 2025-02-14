package org.pet.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final String url = "jdbc:sqlite:C:/projectJava/CurrencyExchange/Currencies.db";

private ConnectionManager() {
}
    public static Connection open() {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
