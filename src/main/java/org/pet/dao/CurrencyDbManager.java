package org.pet.dao;


import org.pet.dto.CurrencyDTO;
import org.pet.dto.ExchangeRateDTO;
import org.pet.entity.CurrencyEntity;
import org.pet.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CurrencyDbManager {

    public CurrencyDbManager() {
    }


    public List<CurrencyDTO> getAllEntities() {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        List<CurrencyDTO> currencies = new ArrayList<>();

        List<CurrencyEntity> currencyEntity = findAllCurrencies();

        for (CurrencyEntity entity : currencyEntity) {
            currencies.add(toDTO(entity));
        }
        return currencies;
    }

    public CurrencyDTO getEntity(CurrencyDTO currencyDTO) {
        CurrencyEntity currencyEntity = findCurrencyByCode(currencyDTO);
        CurrencyDTO dto = toDTO(currencyEntity);
        return dto;

    }

    public CurrencyDTO createEntity(CurrencyDTO currencyDTO) {
        CurrencyEntity currencyEntity = createCurrency(currencyDTO);
        CurrencyDTO dto = toDTO(currencyEntity);
        return dto;

    }


    private CurrencyEntity findCurrencyByCode(CurrencyDTO currencyDTO) {
        CurrencyEntity currencyEntity = toEntity(currencyDTO);
        String code = currencyEntity.getCode();
        String sql = """
                SELECT id, code, full_name, sign FROM Currencies 
                WHERE code = ?;
                """;
        Connection connection = ConnectionManager.open();
        try {
            var prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, code);
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                currencyEntity = creatEntity(resultSet);
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return currencyEntity;
    }

    private List<CurrencyEntity> findAllCurrencies() {
        List<CurrencyEntity> currencyEntities = new ArrayList<>();
        String sql = """
                SELECT id, code, full_name, sign FROM Currencies;
                """;
        Connection connection = ConnectionManager.open();
        try {
            var prepareStatement = connection.prepareStatement(sql);
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                CurrencyEntity currencyEntity = creatEntity(resultSet);
                currencyEntities.add(currencyEntity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencyEntities;
    }

    public List<ExchangeRateDTO> findAllExchangeRate() {
        List<ExchangeRateDTO> rateDTO = new ArrayList<>();
        String sql = """
                SELECT exchangeRates.id,
                       base.id          AS Id_b,
                       base.full_name   AS full_name_b,
                       base.code        AS code_b,
                       base.sign        AS sign_b,
                       target.id        AS Id_t,
                       target.full_name AS full_name_t,
                       target.code      AS code_t,
                       target.sign      AS sign_t,
                       rate
                FROM exchangeRates
                    JOIN Currencies base ON exchangeRates.BaseCurrencyId = base.id
                    JOIN Currencies target ON exchangeRates.TargetCurrencyId = target.id;
                """;
        Connection connection = ConnectionManager.open(); //TODO: Зачем тут коннекшин если он есть в сервлете?
        try {
            var prepareStatement = connection.prepareStatement(sql);
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                ExchangeRateDTO dto = createExchangeDTO(resultSet);
                rateDTO.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rateDTO;
    }

    public ExchangeRateDTO getExchangeRateBaseCurrencyTargetCurrency(String baseCode, String targetCode, Connection connection) {
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        String sql =
                """
                          SELECT er.id,
                        base.id          AS Id_b,
                        base.full_name   AS full_name_b,
                        base.code        AS code_b,
                        base.sign        AS sign_b,
                        target.id        AS Id_t,
                        target.full_name AS full_name_t,
                        target.code      AS code_t,
                        target.sign      AS sign_t,
                        rate
                           
                           FROM exchangeRates eR
                           
                           JOIN Currencies base
                             ON er.BaseCurrencyId = base.id
                           
                         JOIN Currencies target
                              ON er.TargetCurrencyId = target.id
                          
                           WHERE base.code = ? AND target.code = ?;
                             """;
        try {
            var prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, baseCode);
            prepareStatement.setString(2, targetCode);
            var resultSet = prepareStatement.executeQuery();
            while (resultSet.next()) {
                exchangeRateDTO = createExchangeDTO(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRateDTO;
    }


    private CurrencyEntity createCurrency(CurrencyDTO currencyDTO) {
        CurrencyEntity currencyEntity = toEntity(currencyDTO);

        String code = currencyEntity.getCode();
        String full_name = currencyEntity.getFull_name();
        String sign = currencyEntity.getSign();

        String sql = """
                INSERT INTO  Currencies (code, full_name, sign) 
                VALUES (?, ?, ?)
                """;
        Connection connection = ConnectionManager.open();
        try {
            var prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, code);
            prepareStatement.setString(2, full_name);
            prepareStatement.setString(3, sign);
            prepareStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencyEntity;

    }

    private CurrencyEntity creatEntity(ResultSet resultSet) {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        try {
            currencyEntity.setId(resultSet.getInt("id"));
            currencyEntity.setCode(resultSet.getString("code"));
            currencyEntity.setFull_name(resultSet.getString("full_name"));
            currencyEntity.setSign(resultSet.getString("sign"));
            return currencyEntity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencyEntity;
    }

    private ExchangeRateDTO createExchangeDTO(ResultSet resultSet) {
        ExchangeRateDTO dto = new ExchangeRateDTO();
        try {
            CurrencyDTO baseCurrency = new CurrencyDTO();
            CurrencyDTO targetCurrency = new CurrencyDTO();

            baseCurrency.setId(resultSet.getInt("id_b"));
            baseCurrency.setFull_name(resultSet.getString("full_name_b"));
            baseCurrency.setCode(resultSet.getString("code_b"));
            baseCurrency.setSign(resultSet.getString("sign_b"));

            targetCurrency.setId(resultSet.getInt("id_t"));
            targetCurrency.setFull_name(resultSet.getString("full_name_t"));
            targetCurrency.setCode(resultSet.getString("code_t"));
            targetCurrency.setSign(resultSet.getString("sign_t"));

            dto.setId(resultSet.getInt("id"));
            dto.setRate(resultSet.getDouble("Rate"));

            dto.setCurrencyDTOBase(baseCurrency);
            dto.setCurrencyDTOTarget(targetCurrency);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dto;
    }

    private CurrencyDTO toDTO(CurrencyEntity currencyEntity) {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setCode(currencyEntity.getCode());
        currencyDTO.setFull_name(currencyEntity.getFull_name());
        currencyDTO.setSign(currencyEntity.getSign());
        return currencyDTO;
    }

    private CurrencyEntity toEntity(CurrencyDTO currencyDTO) {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setCode(currencyDTO.getCode());
        currencyEntity.setFull_name(currencyDTO.getFull_name());
        currencyEntity.setSign(currencyDTO.getSign());

        return currencyEntity;
    }
}
