package org.pet.dao;

import org.pet.dto.CurrencyDTO;
import org.pet.entity.Currency;
import org.pet.exception.*;
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

    public Optional<Currency> findByCode(String code) {
        try (Connection connection = ConnectionManager.getConnection();) {
            var prepareStatement = connection.prepareStatement(FIND_BY_CODE_SQL);
            prepareStatement.setString(1, code);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                Currency currency = Currency.builder()
                        .id(resultSet.getInt("id"))
                        .code(resultSet.getString("code"))
                        .name(resultSet.getString("full_name"))
                        .sign(resultSet.getString("sign"))
                        .build();
                return Optional.ofNullable(currency);
            } else {
                throw new CurrencyNotFoundException("Валюта не найдена в БД");
            }
        } catch (SQLException e) {
            throw new DataBaseException("База данных не доступна");
        }
    }

    public Optional<Currency> findById(int id) {
        try (Connection connection = ConnectionManager.getConnection()) {
            var prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            prepareStatement.setInt(1, id);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                Currency currency = Currency.builder()
                        .id(resultSet.getInt("id"))
                        .code(resultSet.getString("code"))
                        .name(resultSet.getString("full_name"))
                        .sign(resultSet.getString("sign"))
                        .build();
                return Optional.ofNullable(currency);
            } else {
                throw new CurrencyException("Валюта не найдена");
            }
        } catch (SQLException e) {
            throw new DaoException("Ошибка обращения к базе данных");
        }
    }

    public List<Currency> findAllCurrencies() {
        List<Currency> currencyEntities = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            var prepareStatement = connection.prepareStatement(FIND_ALL_CURRENCIES_SQL);
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                var currency = creatCurrency(resultSet);
                currencyEntities.add(currency.orElseThrow());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataBaseException("Ошибка на сервере, повторите запрос позже");
        }
        return currencyEntities;
    }

    private Optional<Currency> creatCurrency(ResultSet resultSet) throws SQLException {
        Currency currency = Currency.builder()
                .id(resultSet.getInt("id"))
                .code(resultSet.getString("code"))
                .name(resultSet.getString("full_name"))
                .sign(resultSet.getString("sign"))
                .build();
        return Optional.ofNullable(currency);
    }

    public Optional<Currency> createCurrency(CurrencyDTO dto) {
        Optional<Currency> currency = Optional.empty();
        try (Connection connection = ConnectionManager.getConnection()) {
            var prepareStatement = connection.prepareStatement(CREATE_CURRENCY_SQL);
            prepareStatement.setString(1, dto.getCode());
            prepareStatement.setString(2, dto.getName());
            prepareStatement.setString(3, dto.getSign());
            int result = prepareStatement.executeUpdate();
            if (result != 0) {
                setIdCreatCurrency(prepareStatement, dto);
                currency = Optional.ofNullable(CurrencyMapper.INSTANCE.toCurrency(dto));
            }
        } catch (SQLException e) {
            throw new DaoException("Валюта с таким кодом уже существует");
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
}





