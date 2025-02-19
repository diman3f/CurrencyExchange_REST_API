package org.pet.dao;


import org.pet.dto.CurrencyDTO;
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
