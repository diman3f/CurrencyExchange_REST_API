package org.pet.dao;

import org.pet.dto.CurrencyRequestDto;
import org.pet.entity.Currency;
import org.pet.exception.CurrencyAlreadyExistsException;
import org.pet.exception.CurrencyNotFoundException;
import org.pet.exception.DataBaseException;
import org.pet.mapper.CurrencyMapper;
import org.pet.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class CurrencyDAO implements CurrencyRepository {
    private static CurrencyDAO INSTANCE = new CurrencyDAO();

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

    private static final int CODE_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int SIGN_INDEX = 3;
    private static final int ID_INDEX = 1;

    private CurrencyDAO() {
    }

    public static CurrencyDAO getINSTANCE() {
        return INSTANCE;
    }

    public Currency findByCode(String code) {
        try (Connection connection = ConnectionManager.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(FIND_BY_CODE_SQL);
            prepareStatement.setString(CODE_INDEX, code);
            ResultSet result = prepareStatement.executeQuery();
            if (result.next()) {
                Currency currency = creatCurrency(result);
                return currency;
            } else {
                throw new CurrencyNotFoundException(String.format("Валюта %s не найдена в базе данных", code));
            }
        } catch (SQLException e) {
            throw new DataBaseException("Database is not available");
        }
    }

    public Currency findById(int id) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            prepareStatement.setInt(ID_INDEX, id);
            ResultSet result = prepareStatement.executeQuery();
            if (result.next()) {
                Currency currency = creatCurrency(result);
                return currency;
            } else {
                throw new CurrencyNotFoundException(String.format("Валюта по id=%d не найдена в базе данных", id));
            }
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка обращения к базе данных");
        }
    }

    public Set<Currency> findAllCurrencies() {
        Set<Currency> currencyEntities = new HashSet<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement prepareStatement = connection.prepareStatement(FIND_ALL_CURRENCIES_SQL);
            ResultSet result = prepareStatement.executeQuery();
            while (result.next()) {
                Currency currency = creatCurrency(result);
                currencyEntities.add(currency);
            }
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка на сервере, повторите запрос позже");
        }
        return currencyEntities;
    }

    public Currency createCurrency(CurrencyRequestDto dto) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement prepareStatement = connection.prepareStatement(CREATE_CURRENCY_SQL);
            prepareStatement.setString(CODE_INDEX, dto.getCode());
            prepareStatement.setString(NAME_INDEX, dto.getName());
            prepareStatement.setString(SIGN_INDEX, dto.getSign());
            Currency currency = CurrencyMapper.INSTANCE.toCurrency(dto);
            prepareStatement.executeUpdate();
            setIdCreateCurrency(prepareStatement, currency);
            return currency;
        } catch (SQLException e) {
            throw new CurrencyAlreadyExistsException(String.format("Валюта с кодом %s уже существует в базе данных", dto.getCode()));
        }
    }

    private Currency creatCurrency(ResultSet result) throws SQLException {
        Currency currency = Currency.builder()
                .id(result.getInt("id"))
                .code(result.getString("code"))
                .name(result.getString("full_name"))
                .sign(result.getString("sign"))
                .build();
        return currency;
    }

    private void setIdCreateCurrency(PreparedStatement ps, Currency currency) throws SQLException {
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int id = rs.getInt(ID_INDEX);
            currency.setId(id);
        } else {
            throw new DataBaseException("База данных не доступна");
        }
    }
}





