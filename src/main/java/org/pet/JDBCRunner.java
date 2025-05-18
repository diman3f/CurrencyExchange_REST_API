package org.pet;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.pet.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

@Data
public class JDBCRunner {
    public static void main(String[] args) throws SQLException, JsonProcessingException {

        try (Connection open = ConnectionManager.getConnection()) {
            System.out.println("working");
            System.out.println(open.getTransactionIsolation());
        }

    }

    public static boolean code(String code) {
        return code.matches("^[A-Z]{3}+$");
    }

}



