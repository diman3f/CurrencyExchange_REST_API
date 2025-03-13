package org.pet.dao;

import org.pet.dto.CurrencyDTO;
import org.pet.entity.Currency;
import org.pet.exception.DaoException;
import org.pet.mapper.CurrencyMapper;
import org.pet.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CurrencyDao {
    private static CurrencyDao INSTANCE = new CurrencyDao();
    private static final String FIND_BY_CODE_SQL = """
            SELECT id, code, full_name, sign FROM Currencies 
                           WHERE code = ?;
                       """;

    private CurrencyDao() {
    }

    public static CurrencyDao getINSTANCE() {
        return INSTANCE;
    }

    public Optional<CurrencyDTO> findByCode(String code) {

        try {
            Connection connection = ConnectionManager.open();
            var prepareStatement = connection.prepareStatement(FIND_BY_CODE_SQL);
            prepareStatement.setString(1, code);
            ResultSet resultSet = prepareStatement.executeQuery();
            Currency currency = null;
            if (resultSet.next()) {
                currency = Currency.builder()
                        .id(resultSet.getInt("id"))
                        .code(resultSet.getString("code"))
                        .full_name(resultSet.getString("full_name"))
                        .sign(resultSet.getString("sign"))
                        .build();
            }
            CurrencyDTO currencyDTO = CurrencyMapper.INSTANCE.toCurrencyDTO(currency);
            return Optional.ofNullable(currencyDTO);

        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }
}


