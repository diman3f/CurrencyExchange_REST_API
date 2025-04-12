package org.pet.services;

import org.pet.dao.CurrencyDao;
import org.pet.dao.ExchangeRateDao;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.entity.Currency;
import org.pet.entity.ExchangeRate;
import org.pet.exception.CurrencyException;
import org.pet.exception.DaoException;
import org.pet.mapper.ExchangeRateMapper;
import org.pet.utils.ConnectionManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private static CurrencyDao instanceCurrencyDao = CurrencyDao.getINSTANCE();
    private static ExchangeRateDao instanceExchangeRateDao = ExchangeRateDao.getInstance();
    Connection open = ConnectionManager.open();

    public ExchangeRateService() {
    }

    public static ExchangeRateService getINSTANCE() {
        return INSTANCE;
    }

    public ExchangeRateResponseDTO getExchangeRate(ExchangeRateRequestServletDTO exchangeRateRequestDTO) {
        String baseCode = exchangeRateRequestDTO.getBaseCode();
        String targetCode = exchangeRateRequestDTO.getTargetCode();

        Optional<ExchangeRate> exchangeRate = instanceExchangeRateDao.findExchangeRate(baseCode, targetCode, open);
        Optional<Currency> currencyBase = instanceCurrencyDao.findByCode(baseCode, open);
        Optional<Currency> currencyTarget = instanceCurrencyDao.findByCode(targetCode, open);
        ExchangeRateMapper instance = ExchangeRateMapper.INSTANCE;
        try {
            return instance.toExchangeRateResponseDTO(currencyBase.orElseThrow(), currencyTarget.orElseThrow(), exchangeRate.orElseThrow());
        } catch (NoSuchElementException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public List<ExchangeRateResponseDTO> getAllExchangeRate() {
        List<ExchangeRateResponseDTO> exchangeRateResponseDTOList = new ArrayList<>();
        ExchangeRateMapper mapper = ExchangeRateMapper.INSTANCE;
        try {
            List<ExchangeRate> exchangeRate = instanceExchangeRateDao.findAllExchangeRate(open);
            if (!exchangeRate.isEmpty()) {
                for (ExchangeRate er : exchangeRate) {
                    Optional<Currency> baseCurrency = instanceCurrencyDao.findById(er.getBaseCurrencyId(), open);
                    Optional<Currency> targetCurrency = instanceCurrencyDao.findById(er.getTargetCurrencyId(), open);
                    exchangeRateResponseDTOList.add(mapper.toExchangeRateResponseDTO(baseCurrency.orElseThrow(), targetCurrency.orElseThrow(), er));
                }
            }
        } catch (DaoException | CurrencyException | NoSuchElementException e) {
            throw new DaoException(e.getMessage());
        }
        return exchangeRateResponseDTOList;
    }

    public ExchangeRateResponseDTO saveExchangeRate(ExchangeRateRequestServletDTO dtoExchangeRate) {
        try {
            Optional<Currency> baseCurrency = instanceCurrencyDao.findByCode(dtoExchangeRate.getBaseCode(), open);
            Optional<Currency> targetCurrency = instanceCurrencyDao.findByCode(dtoExchangeRate.getTargetCode(), open);

            ExchangeRate exchangeRate = ExchangeRate.builder()
                    .baseCurrencyId(baseCurrency.orElseThrow().getId())
                    .targetCurrencyId(targetCurrency.orElseThrow().getId())
                    .rate(dtoExchangeRate.getRate())
                    .build();

            ExchangeRate result = instanceExchangeRateDao.saveExchangeRate(exchangeRate, open);
            exchangeRate = result;

            return ExchangeRateMapper.INSTANCE.toExchangeRateResponseDTO(baseCurrency.orElseThrow(), targetCurrency.orElseThrow(), exchangeRate);

        } catch (NoSuchElementException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public ExchangeRateResponseDTO updateExchangeRate(ExchangeRateRequestServletDTO dto) {
        try {
            Optional<ExchangeRate> exchangeRateOptional = instanceExchangeRateDao.findExchangeRate(dto.getBaseCode(), dto.getTargetCode(), open);
            ExchangeRate exchangeRate = exchangeRateOptional.orElseThrow();
            exchangeRate.setRate(dto.getRate());
            exchangeRate = instanceExchangeRateDao.updateExchangeRate(exchangeRate, open);
            Optional<Currency> baseCurrency = instanceCurrencyDao.findByCode(dto.getBaseCode(), open);
            Optional<Currency> targetCurrency = instanceCurrencyDao.findByCode(dto.getTargetCode(), open);
            return ExchangeRateMapper.INSTANCE.toExchangeRateResponseDTO(baseCurrency.orElseThrow(), targetCurrency.orElseThrow(), exchangeRate);
        } catch (NoSuchElementException e) {
            throw new DaoException(e.getMessage());
        }
    }
}

