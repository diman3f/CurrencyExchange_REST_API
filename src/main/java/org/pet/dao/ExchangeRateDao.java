package org.pet.dao;

import org.pet.entity.ExchangeRate;
import org.pet.exception.*;
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

    public ExchangeRate findExchangeRate(String baseCode, String targetCode) {
        final int BASE_CURRENCY_INDEX = 1;
        final int TARGET_CURRENCY_INDEX= 2;

        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement prepareStatement = connection.prepareStatement(FIND_EXCHANGE_RATE_BY_CODES_SQL);
            prepareStatement.setString(BASE_CURRENCY_INDEX, baseCode);
            prepareStatement.setString(TARGET_CURRENCY_INDEX, targetCode);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                ExchangeRate exchangeRate = createOfResultSet(resultSet);
               return exchangeRate;
            } else throw new CurrencyNotFoundException("Обменный курс для пары валют не найден"); //todo приходится кидать not found чтобы не дублировать 404 в обработчике
        } catch (SQLException e) {
           throw  new DaoException("База данных не доступна");
        }
    }

    private ExchangeRate createOfResultSet(ResultSet resultSet) throws SQLException {
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .id(resultSet.getInt("id"))
                .baseCurrencyId(resultSet.getInt("base_currency_id"))
                .targetCurrencyId(resultSet.getInt("target_currency_id"))
                .rate(resultSet.getBigDecimal("rate"))
                .build();
        return exchangeRate;
    }

    public List<ExchangeRate> findAllExchangeRate() {
        List<ExchangeRate> exchangeRate = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement prepareStatement = connection.prepareStatement(FIND_ALL_EXCHANGE_RATE_SQL);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                exchangeRate.add(ExchangeRate.builder()
                        .id(resultSet.getInt("id"))
                        .baseCurrencyId(resultSet.getInt("base_id"))
                        .targetCurrencyId(resultSet.getInt("target_id"))
                        .rate(resultSet.getBigDecimal("rate"))
                        .build());
            }
        } catch (SQLException e) {
            throw new CurrencyNotFoundException("Обменный курс по указанной паре валют отсутствует");
        }
        return exchangeRate;
    }

    public ExchangeRate saveExchangeRate(ExchangeRate exchangeRate) {
        try (Connection connection = ConnectionManager.getConnection();) {
            PreparedStatement prepareStatement = connection.prepareStatement(CREATE_EXCHANGE_RATE_SQL, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            prepareStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            prepareStatement.setBigDecimal(3, exchangeRate.getRate());
            prepareStatement.executeUpdate();
            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                exchangeRate.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            throw new CurrencyAlreadyExistsException("Валютная пара с таким кодом уже существует");
        }
        return exchangeRate;
    }

    public ExchangeRate updateExchangeRate(ExchangeRate exchangeRate) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_EXCHANGE_RATE_SQL);
            prepareStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            prepareStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            prepareStatement.setBigDecimal(3, exchangeRate.getRate());
            prepareStatement.setInt(4, exchangeRate.getId());
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка доступка к базе данных");
        }
        return exchangeRate;
    }
}




