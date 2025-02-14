package org.pet.dao;


import org.pet.dto.CurrencyDTO;
import org.pet.entity.CurrencyEntity;
import org.pet.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

public class CurrencyDbManager implements DataAccessObject<CurrencyEntity> {

    public CurrencyDbManager() {
    }

    public CurrencyDTO readEntity(int id) {
        CurrencyDTO currencyDTO = new CurrencyDTO();

            CurrencyEntity currencyEntity = getEntity(id);

            currencyDTO.setCode(currencyEntity.getCode());
            currencyDTO.setFull_name(currencyEntity.getFull_name());
            currencyDTO.setSign(currencyEntity.getSign());

        return currencyDTO;
    }


    @Override
    public CurrencyEntity getEntity(int id) {
        String sql = """
                SELECT id, code, full_name, sign FROM Currencies
                WHERE id = ?;
                """;
        CurrencyEntity currencyEntity = new CurrencyEntity();
        Connection connection = ConnectionManager.open();
        try {
            var prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id);
            var resultSet = prepareStatement.executeQuery();

            while (resultSet.next()) {
                currencyEntity.setId(resultSet.getInt("id"));
                currencyEntity.setCode(resultSet.getString("code"));
                currencyEntity.setFull_name(resultSet.getString("full_name"));
                currencyEntity.setSign(resultSet.getString("sign"));
            }
            return currencyEntity;
        } catch (SQLException e) {
        }
        return currencyEntity;
    }

    @Override
    public CurrencyEntity create() {
        return null;
    }
}