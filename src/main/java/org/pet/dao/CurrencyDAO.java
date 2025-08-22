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

    private CurrencyDAO() {
    }

    public static CurrencyDAO getINSTANCE() {
        return INSTANCE;
    }

    public Currency findByCode(String code) {
        try (Connection connection = ConnectionManager.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(FIND_BY_CODE_SQL);
            prepareStatement.setString(1, code);
            ResultSet result = prepareStatement.executeQuery();
            if (result.next()) {
                Currency currency = Currency.builder()
                        .id(result.getInt("id"))
                        .code(result.getString("code"))
                        .name(result.getString("full_name"))
                        .sign(result.getString("sign"))
                        .build();
                return currency;
            } else {
                throw new CurrencyNotFoundException(String.format("Валюта %s не найдена в базе данных", code));
            }
        } catch (SQLException e) {
            throw new DataBaseException("Database is not available");
        }
    }

    public Optional<Currency> findById(int id) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            prepareStatement.setInt(1, id);
            ResultSet result = prepareStatement.executeQuery();
            if (result.next()) {
                Currency currency = Currency.builder()
                        .id(result.getInt("id"))
                        .code(result.getString("code"))
                        .name(result.getString("full_name"))
                        .sign(result.getString("sign"))
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
           PreparedStatement prepareStatement = connection.prepareStatement(FIND_ALL_CURRENCIES_SQL);
            ResultSet result = prepareStatement.executeQuery();
            while (result.next()) {
                Optional <Currency> currency = creatCurrency(result);
                currencyEntities.add(currency.orElseThrow());
            }
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка на сервере, повторите запрос позже");
        }
        return currencyEntities;
    }

    private Optional<Currency> creatCurrency(ResultSet result) throws SQLException {
        Currency currency = Currency.builder()
                .id(result.getInt("id"))
                .code(result.getString("code"))
                .name(result.getString("full_name"))
                .sign(result.getString("sign"))
                .build();
        return Optional.ofNullable(currency);
    }

    public Currency createCurrency(CurrencyDTO dto) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement prepareStatement = connection.prepareStatement(CREATE_CURRENCY_SQL);
            prepareStatement.setString(1, dto.getCode());
            prepareStatement.setString(2, dto.getName());
            prepareStatement.setString(3, dto.getSign());
            int result = prepareStatement.executeUpdate();
            if (result != 0) {
                setIdCreatCurrency(prepareStatement, dto);
            }
        } catch (SQLException e) {
            throw new CurrencyAlreadyExistsException("Currency code is already registered. Please use a different ISO 4217 code.");
        }
        return CurrencyMapper.INSTANCE.toCurrency(dto);
    }


    private void setIdCreatCurrency(PreparedStatement ps, CurrencyDTO dto) throws SQLException {
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            int id = rs.getInt(1);
            dto.setId(id);
        } else {
            throw new DataBaseException("");
        }
    }
}





