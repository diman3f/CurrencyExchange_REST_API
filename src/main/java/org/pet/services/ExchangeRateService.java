package org.pet.services;

import org.pet.dao.CurrencyDao;
import org.pet.dao.ExchangeRateDao;
import org.pet.dto.CurrencyDTO;
import org.pet.dto.ExchangeRateDTO;
import org.pet.dto.ExchangeRateRequestDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.entity.Currency;
import org.pet.entity.ExchangeRate;
import org.pet.exception.CurrencyException;
import org.pet.exception.DaoException;
import org.pet.mapper.CurrencyMapper;
import org.pet.servlets.ExchangeRateServlet;
import org.pet.utils.ConnectionManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private static String codeCurrencyBase;
    private static String codeCurrencyTarget;
    private static CurrencyDao instanceCurrencyDao = CurrencyDao.getINSTANCE();
    private static ExchangeRateDao instanceExchangeRateDao = ExchangeRateDao.getInstance();
    Connection open = ConnectionManager.open();

    public ExchangeRateService() {
    }
    public static ExchangeRateService getINSTANCE() {
        return INSTANCE;
    }


    public ExchangeRateResponseDTO getExchangeRateDTO(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        ExchangeRateResponseDTO exchangeRateDTO = new ExchangeRateResponseDTO();
        ExchangeRate exchangeRate = getExchangeRate(exchangeRateRequestDTO);
        exchangeRateDTO.setId(exchangeRate.getId());
        Optional<CurrencyDTO> currencyDTOBase = instanceCurrencyDao.findByCode(codeCurrencyBase);
        Optional<CurrencyDTO> currencyDTOTarget = instanceCurrencyDao.findByCode(codeCurrencyTarget);
        CurrencyMapper instance = CurrencyMapper.INSTANCE;
        exchangeRateDTO.setBaseCurrency(instance.toCurrency(currencyDTOBase.orElse(new CurrencyDTO())));
        exchangeRateDTO.setTargetCurrency(instance.toCurrency(currencyDTOTarget.orElse(new CurrencyDTO())));
        exchangeRateDTO.setRate(exchangeRate.getRate());
        return exchangeRateDTO;

    }

    private ExchangeRate getExchangeRate(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        ExchangeRate exchangeRateModel;
        extractСodesCurrencyFromRequest(exchangeRateRequestDTO);

        Optional<ExchangeRate> exchangeRate = instanceExchangeRateDao.findExchangeRate(codeCurrencyBase, codeCurrencyTarget, open);
        try {
            return exchangeRate.orElseThrow();
        } catch (NoSuchElementException e) {
            throw new DaoException("Обменный курс по запрошенным кодам отсутствует");
        }
    }

    public List<ExchangeRateResponseDTO> getAllExchangeRate() {
        List<ExchangeRateResponseDTO> exchangeRateResponseDTOList = new ArrayList<>();
        CurrencyMapper mapper = CurrencyMapper.INSTANCE;
        try {
            ExchangeRateDao instance = ExchangeRateDao.getInstance();
            List<ExchangeRate> exchangeRate = instance.findAllExchangeRate(open);
            if (!exchangeRate.isEmpty()) {
                for (ExchangeRate er : exchangeRate) {
                    List<Currency> baseTargetCurrency= getBaseTargetCurrencyExchangeRateById(er);
                    Currency base = baseTargetCurrency.get(0);
                    Currency target = baseTargetCurrency.get(1);
                   exchangeRateResponseDTOList.add(mapper.toExchangeRateDTO(base,target,er));
                }
            }
        } catch (DaoException | CurrencyException e) {
            throw new DaoException("Валюты отсутствуют");
        }
        return exchangeRateResponseDTOList;
    }

    private List<Currency> getBaseTargetCurrencyExchangeRateById(ExchangeRate exchangeRate) {
        List<Currency> currencies = new ArrayList<>();
        try {
            Optional<Currency> base = instanceCurrencyDao.findById(exchangeRate.getBaseCurrencyId());
            Optional<Currency> target = instanceCurrencyDao.findById(exchangeRate.getTargetCurrencyId());
            currencies.add(base.orElseThrow());
            currencies.add(target.orElseThrow());
        } catch (NoSuchElementException e) {
            throw new DaoException("Валюты отсутствуют");
        }
        return currencies;
    }


    private void extractСodesCurrencyFromRequest(ExchangeRateRequestDTO exchangeRateRequestDTO) {
        codeCurrencyBase = exchangeRateRequestDTO.getParametersCodesRequest().substring(0, 3);
        codeCurrencyTarget = exchangeRateRequestDTO.getParametersCodesRequest().substring(3, 6);

    }


}
