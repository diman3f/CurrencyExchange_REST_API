package org.pet.services;

import org.pet.dao.CurrencyDao;
import org.pet.dao.ExchangeRateDao;
import org.pet.dto.CurrencyDTO;
import org.pet.dto.ExchangeRateDTO;
import org.pet.dto.ExchangeRateRequestDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.entity.ExchangeRate;
import org.pet.exception.DaoException;
import org.pet.mapper.CurrencyMapper;
import org.pet.servlets.ExchangeRateServlet;
import org.pet.utils.ConnectionManager;

import java.sql.Connection;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ExchangeRateService {
    private String codeCurrencyBase;
    private String codeCurrencyTarget;


    private ExchangeRate getExchangeRate(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        ExchangeRate exchangeRateModel;
        Connection open = ConnectionManager.open();
        extractСodesCurrencyFromRequest(exchangeRateRequestDTO);
        ExchangeRateDao instance = ExchangeRateDao.getInstance();
        Optional<ExchangeRate> exchangeRate = instance.findExchangeRate(codeCurrencyBase, codeCurrencyTarget, open);
        try {
            return exchangeRate.orElseThrow();
        } catch (NoSuchElementException e) {
            throw new DaoException("Обменный курс по запрошенным кодам отсутствует");
        }
    }

    private void extractСodesCurrencyFromRequest(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        codeCurrencyBase = exchangeRateRequestDTO.getParametersCodesRequest().substring(0, 3);
        codeCurrencyTarget = exchangeRateRequestDTO.getParametersCodesRequest().substring(3, 6);

    }

    public ExchangeRateResponseDTO getExchangeRateDTO(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        ExchangeRateResponseDTO exchangeRateDTO = new ExchangeRateResponseDTO();
        ExchangeRate exchangeRate = getExchangeRate(exchangeRateRequestDTO);
        exchangeRateDTO.setId(exchangeRate.getId());
        CurrencyDao instanceCurrencyDao = CurrencyDao.getINSTANCE();
        Optional<CurrencyDTO> currencyDTOBase = instanceCurrencyDao.findByCode(codeCurrencyBase);
        Optional<CurrencyDTO> currencyDTOTarget = instanceCurrencyDao.findByCode(codeCurrencyTarget);
        CurrencyMapper instance = CurrencyMapper.INSTANCE;
        exchangeRateDTO.setBaseCurrency(instance.toCurrency(currencyDTOBase.orElse(new CurrencyDTO())));
        exchangeRateDTO.setTargetCurrency(instance.toCurrency(currencyDTOTarget.orElse(new CurrencyDTO())));
        exchangeRateDTO.setRate(exchangeRate.getRate());
        return exchangeRateDTO;

    }


}
