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


    public List<CurrencyDTO> findAllEntities() {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        List<CurrencyDTO> currencies = new ArrayList<>();

        List<CurrencyEntity> currencyEntity = findAllEntity();

        for (CurrencyEntity entity : currencyEntity) {
            currencies.add(toDTO(entity));
        }
        return currencies;
    }

    private List<CurrencyEntity> findAllEntity() {
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
}
