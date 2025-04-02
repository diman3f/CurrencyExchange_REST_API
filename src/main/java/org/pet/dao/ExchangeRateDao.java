package org.pet.dao;

import org.pet.dto.ExchangeRateDTO;
import org.pet.entity.ExchangeRate;
import org.pet.exception.DaoException;
import org.pet.utils.ConnectionManager;

import java.math.BigDecimal;
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
            INSERT exchangeRates(BaseCurrencyId, TargetCurrencyId, Rate)
            SELECT base.id, target.id, ?
            FROM exchangeRates
                     JOIN Currencies base ON exchangeRates.BaseCurrencyId = base.id
                     JOIN Currencies target ON exchangeRates.TargetCurrencyId = target.id
            WHERE base.code = ?
              AND target.code = ?;
                            """;


//добавление нового обменного курса в бд
    //получаю поля из боди коды и рейт и передаю их в параметры метода слоя dao
    //добавлю обменный курс в бд
    // создаю dto добавленного обменного курса для этого
    // использую метод по созданию dto на каждую валюту для того чтобы собрать обменный json
    // получить модель сохраненого обменного курса, а потом только ее смапить в дто

    private ExchangeRateDao() {

    }

    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }

    public ExchangeRateDTO findAll() {
        ExchangeRateDTO dto = new ExchangeRateDTO();

        return dto;
    }

    public Optional<ExchangeRate> findExchangeRate(String baseCode, String targetCode, Connection connection) {
        Optional<ExchangeRate> exchangeRate = Optional.empty();
        try {
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
            new DaoException(e.getMessage());
        }
        return exchangeRate;
    }


    public List<ExchangeRate> findAllExchangeRate(Connection connection) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try {
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
            throw new DaoException(e.getMessage());
        }
        return exchangeRates;
    }
}

