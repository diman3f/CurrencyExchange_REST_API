package org.pet.dao;

import org.pet.dto.CurrencyDTO;
import org.pet.entity.Currency;
import org.pet.exception.CurrencyException;
import org.pet.exception.DaoException;
import org.pet.mapper.CurrencyMapper;
import org.pet.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {
    private static CurrencyDao INSTANCE = new CurrencyDao();

    private static final String FIND_BY_CODE_SQL = """
            SELECT id, code, full_name, sign FROM Currencies 
                           WHERE code = ?;
                       """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, code, full_name, sign FROM Currencies 
                           WHERE id = ?;
                       """;

    private static final String FIND_ALL_CURRENCIES_SQL = """
            SELECT id, code, full_name, sign FROM Currencies;
            """;
    private static final String CREATE_CURRENCY_SQL = """
            INSERT INTO  Currencies (code, full_name, sign)
                VALUES (?, ?, ?)
            """;

    private CurrencyDao() {
    }

    public static CurrencyDao getINSTANCE() {
        return INSTANCE;
    }

    public Optional<Currency> findByCode(String code, Connection connection) {
        try {
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
            } else {
                throw new CurrencyException("Валюта не найдена");
            }return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new DaoException("Ошибка обращения к базе данных");
        }
    }

    public Optional<Currency> findById(int id, Connection connection) {
        try {
            var prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            prepareStatement.setInt(1,id);
            ResultSet resultSet = prepareStatement.executeQuery();
            Currency currency = null;
            if (resultSet.next()) {
                currency = Currency.builder()
                        .id(resultSet.getInt("id"))
                        .code(resultSet.getString("code"))
                        .full_name(resultSet.getString("full_name"))
                        .sign(resultSet.getString("sign"))
                        .build();
            } else {
                throw new CurrencyException("Валюта не найдена");
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new DaoException("Ошибка обращения к базе данных");
        }
    }

    public List<Currency> findAllCurrencies() {
        List<Currency> currencyEntities = new ArrayList<>();
        Connection connection = ConnectionManager.open();
        try {
            var prepareStatement = connection.prepareStatement(FIND_ALL_CURRENCIES_SQL);
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                var currency = creatCurrency(resultSet);
                if (currency.isPresent()) {
                    currencyEntities.add(currency.get());
                } else {
                    throw new DaoException("Валюты отсутствуют");
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e, "Ошибка на сервере, повторите запрос позже");
        }
        return currencyEntities;
    }

    public Optional<Currency> createCurrency(CurrencyDTO dto) {
        Optional<Currency> currency = Optional.empty();
        Connection connection = ConnectionManager.open();
        int result = 0;
        try (var prepareStatement = connection.prepareStatement(CREATE_CURRENCY_SQL)) {
            prepareStatement.setString(1, dto.getCode());
            prepareStatement.setString(2, dto.getFull_name());
            prepareStatement.setString(3, dto.getSign());
            result = prepareStatement.executeUpdate();
            if (result != 0) {
                setIdCreatCurrency(prepareStatement, dto);
                currency = Optional.ofNullable(CurrencyMapper.INSTANCE.toCurrency(dto));
            }
        } catch (SQLException e) {
            throw new DaoException(e, "Валюта добавлена ранее");
        }
        return currency;
    }


    private void setIdCreatCurrency(PreparedStatement ps, CurrencyDTO dto) throws SQLException {
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int id = rs.getInt(1);
            dto.setId(id);
        } else {
            throw new RuntimeException("id валюты не найдено");
        }
    }

    private Optional<Currency> creatCurrency(ResultSet resultSet) {
        Currency currency = null;
        try {
            currency = Currency.builder()
                    .id(resultSet.getInt("id"))
                    .code(resultSet.getString("code"))
                    .full_name(resultSet.getString("full_name"))
                    .sign(resultSet.getString("sign"))
                    .build();
        } catch (SQLException e) {
            throw new DaoException(e, "Ошибка получения атрибута из БД");
        }
        return Optional.ofNullable(currency);
    }

}





