package org.pet.dao;

import org.pet.entity.ExchangeRate;
import org.pet.exception.CurrencyPairAlreadyExistsException;
import org.pet.exception.DaoException;
import org.pet.exception.DataBaseException;
import org.pet.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao {

    private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();
    private static final String FIND_EXCHANGE_RATE_BY_CODES_SQL = """
            SELECT exchangeRate.id AS id,
                   base.id AS base_currency_id,
                   target.id AS target_currency_id,
                   Rate AS rate
            FROM exchangeRate exchangeRate
                JOIN Currencies base ON exchangeRate.BaseCurrencyId = base.id
                JOIN Currencies target ON exchangeRate.TargetCurrencyId = target.id
                       
            WHERE base.code = ? AND target.code = ?;
                             """;
    private static final String FIND_ALL_EXCHANGE_RATE_SQL = """
            SELECT exchangeRate.id AS id,
                   exchangeRate.BaseCurrencyId AS base_id,
                   exchangeRate.TargetCurrencyId AS target_id,
                   exchangeRate.Rate AS rate
                            
            FROM exchangeRate;
            """;
    private static final String CREATE_EXCHANGE_RATE_SQL = """
            INSERT INTO exchangeRate(BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?,?,?);
            """;
    private static final String UPDATE_EXCHANGE_RATE_SQL = """
            UPDATE exchangeRate
            SET BaseCurrencyId   = ?,
                TargetCurrencyId = ?,
                Rate             = ?
            WHERE id = ?;
                      
              """;

    private ExchangeRateDao() {
    }

    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }

    public Optional<ExchangeRate> findExchangeRate(String baseCode, String targetCode) {
        Optional<ExchangeRate> exchangeRate = Optional.empty();
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement prepareStatement = connection.prepareStatement(FIND_EXCHANGE_RATE_BY_CODES_SQL);
            prepareStatement.setString(1, baseCode);
            prepareStatement.setString(2, targetCode);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                exchangeRate = Optional.ofNullable(ExchangeRate.builder()
                        .id(resultSet.getInt("id"))
                        .baseCurrencyId(resultSet.getInt("base_currency_id"))
                        .targetCurrencyId(resultSet.getInt("target_currency_id"))
                        .rate(resultSet.getBigDecimal("rate"))
                        .build());
            }
        } catch (SQLException e) {
            new DaoException("База данных не доступна");
        }
        return exchangeRate;
    }

    public List<ExchangeRate> findAllExchangeRate() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            var prepareStatement = connection.prepareStatement(FIND_ALL_EXCHANGE_RATE_SQL);
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                exchangeRates.add(ExchangeRate.builder()
                        .id(resultSet.getInt("id"))
                        .baseCurrencyId(resultSet.getInt("base_id"))
                        .targetCurrencyId(resultSet.getInt("target_id"))
                        .rate(resultSet.getBigDecimal("rate"))
                        .build());
            }
        } catch (SQLException e) {
            throw new DataBaseException("База данных не доступна");
        }
        return exchangeRates;
    }

    public ExchangeRate saveExchangeRate(ExchangeRate exchangeRate) {
        try (Connection connection = ConnectionManager.getConnection();) {
            var prepareStatement = connection.prepareStatement(CREATE_EXCHANGE_RATE_SQL, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            prepareStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            prepareStatement.setBigDecimal(3, exchangeRate.getRate());
            prepareStatement.executeUpdate();
            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                exchangeRate.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new CurrencyPairAlreadyExistsException("Валютная пара с таким кодом уже существует");
        }
        return exchangeRate;
    }

    public ExchangeRate updateExchangeRate(ExchangeRate exchangeRate) {
        try (Connection connection = ConnectionManager.getConnection()) {
            var prepareStatement = connection.prepareStatement(UPDATE_EXCHANGE_RATE_SQL);
            prepareStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            prepareStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            prepareStatement.setBigDecimal(3, exchangeRate.getRate());
            prepareStatement.setInt(4, exchangeRate.getId());
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseException("База данных не доступна");
        }
        return exchangeRate;
    }
}




