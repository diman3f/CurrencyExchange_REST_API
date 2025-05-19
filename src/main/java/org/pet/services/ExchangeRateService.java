package org.pet.services;

import org.pet.dao.CurrencyDao;
import org.pet.dao.ExchangeRateDao;
import org.pet.dto.CurrencyExchangeRateRequestDto;
import org.pet.dto.CurrencyExchangeRateResponseDto;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.entity.Currency;
import org.pet.entity.ExchangeRate;
import org.pet.exception.CurrencyException;
import org.pet.exception.DaoException;
import org.pet.mapper.ExchangeRateMapper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private static CurrencyDao instanceCurrencyDao = CurrencyDao.getINSTANCE();
    private static ExchangeRateDao instanceExchangeRateDao = ExchangeRateDao.getInstance();


    public ExchangeRateService() {
    }

    public static ExchangeRateService getINSTANCE() {
        return INSTANCE;
    }

    public ExchangeRateResponseDTO getExchangeRate(ExchangeRateRequestServletDTO exchangeRateRequestDTO) {
        String baseCode = exchangeRateRequestDTO.getBaseCode();
        String targetCode = exchangeRateRequestDTO.getTargetCode();
        Optional<ExchangeRate> exchangeRate = instanceExchangeRateDao.findExchangeRate(baseCode, targetCode);
        Optional<Currency> currencyBase = instanceCurrencyDao.findByCode(baseCode);
        Optional<Currency> currencyTarget = instanceCurrencyDao.findByCode(targetCode);
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
            List<ExchangeRate> exchangeRate = instanceExchangeRateDao.findAllExchangeRate();
            if (!exchangeRate.isEmpty()) {
                for (ExchangeRate er : exchangeRate) {
                    Optional<Currency> baseCurrency = instanceCurrencyDao.findById(er.getBaseCurrencyId());
                    Optional<Currency> targetCurrency = instanceCurrencyDao.findById(er.getTargetCurrencyId());
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
            Optional<Currency> baseCurrency = instanceCurrencyDao.findByCode(dtoExchangeRate.getBaseCode());
            Optional<Currency> targetCurrency = instanceCurrencyDao.findByCode(dtoExchangeRate.getTargetCode());

            ExchangeRate exchangeRate = ExchangeRate.builder()
                    .baseCurrencyId(baseCurrency.orElseThrow().getId())
                    .targetCurrencyId(targetCurrency.orElseThrow().getId())
                    .rate(dtoExchangeRate.getRate())
                    .build();

            ExchangeRate result = instanceExchangeRateDao.saveExchangeRate(exchangeRate);
            exchangeRate = result;

            return ExchangeRateMapper.INSTANCE.toExchangeRateResponseDTO(baseCurrency.orElseThrow(), targetCurrency.orElseThrow(), exchangeRate);

        } catch (NoSuchElementException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public ExchangeRateResponseDTO updateExchangeRate(ExchangeRateRequestServletDTO dto) {
        try {
            Optional<ExchangeRate> exchangeRateOptional = instanceExchangeRateDao.findExchangeRate(dto.getBaseCode(), dto.getTargetCode());
            ExchangeRate exchangeRate = exchangeRateOptional.orElseThrow();
            exchangeRate.setRate(dto.getRate());
            exchangeRate = instanceExchangeRateDao.updateExchangeRate(exchangeRate);
            Optional<Currency> baseCurrency = instanceCurrencyDao.findByCode(dto.getBaseCode());
            Optional<Currency> targetCurrency = instanceCurrencyDao.findByCode(dto.getTargetCode());
            return ExchangeRateMapper.INSTANCE.toExchangeRateResponseDTO(baseCurrency.orElseThrow(), targetCurrency.orElseThrow(), exchangeRate);
        } catch (NoSuchElementException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public CurrencyExchangeRateResponseDto executeExchangeCurrency(CurrencyExchangeRateRequestDto dto) {
        String baseCurrency = dto.getBaseCurrency();
        String targetCurrency = dto.getTargetCurrency();
        Double amount = dto.getAmount();
        try {
            if (isExchangeRateByStraightRate(baseCurrency, targetCurrency)) {
                ExchangeRate rate = instanceExchangeRateDao.findExchangeRate(baseCurrency, targetCurrency).get();
                return CurrencyExchangeRateResponseDto.builder()
                        .baseCurrency(instanceCurrencyDao.findById(rate.getBaseCurrencyId()).orElseThrow())
                        .targetCurrency(instanceCurrencyDao.findById(rate.getTargetCurrencyId()).orElseThrow())
                        .rate(rate.getRate())
                        .amount(dto.getAmount())
                        .convertedAmount((rate.getRate().multiply(BigDecimal.valueOf(dto.getAmount()))))
                        .build();
            } else if (isExchangeRateByReverseRate(targetCurrency, baseCurrency)) {
                ExchangeRate rate = instanceExchangeRateDao.findExchangeRate(targetCurrency, baseCurrency).get();
                return buildDtoFromExchangeRate(rate, amount);
            } else return buildDtoCalculateCrossRate(baseCurrency, targetCurrency, amount);
        } catch (NoSuchElementException | DaoException e) {
            e.printStackTrace();
            throw new DaoException("Такого обменного курса не существует");
        }
    }


    private CurrencyExchangeRateResponseDto buildDtoFromExchangeRate(ExchangeRate rate, double amount) {
        return CurrencyExchangeRateResponseDto.builder()
                .baseCurrency(instanceCurrencyDao.findById(rate.getTargetCurrencyId()).orElseThrow())
                .targetCurrency(instanceCurrencyDao.findById(rate.getBaseCurrencyId()).orElseThrow())
                .rate(calculateReverseRate(rate))
                .amount(amount)
                .convertedAmount((calculateReverseRate(rate).multiply(BigDecimal.valueOf(amount))))
                .build();
    }


    private BigDecimal calculateReverseRate(ExchangeRate exchangeRate) {
        BigDecimal one = new BigDecimal("1");
        BigDecimal target = exchangeRate.getRate();
        return one.divide(target, 6, RoundingMode.HALF_DOWN);
    }

    private boolean isExchangeRateByStraightRate(String baseCurrency, String targetCurrency) {
        Optional<ExchangeRate> straightRate = instanceExchangeRateDao.findExchangeRate(baseCurrency, targetCurrency);
        return straightRate.isPresent();
    }

    private boolean isExchangeRateByReverseRate(String targetCurrency, String baseCurrency) {
        Optional<ExchangeRate> reverseRate = instanceExchangeRateDao.findExchangeRate(targetCurrency, baseCurrency);
        return reverseRate.isPresent();
    }


    private CurrencyExchangeRateResponseDto buildDtoCalculateCrossRate(String baseCode, String targetCode, double amount) {
        ExchangeRate straightCurrencyBaseExchangeRate = getStraightCurrencyExchangeRateToUSD(baseCode);
        ExchangeRate straightCurrencyTargetExchangeRate = getStraightCurrencyExchangeRateToUSD(targetCode);
        BigDecimal crossRate = calculateCrossRate(straightCurrencyBaseExchangeRate, straightCurrencyTargetExchangeRate);
        BigDecimal convertedAmount = (crossRate.multiply(BigDecimal.valueOf(amount)).setScale(2,RoundingMode.HALF_EVEN));
        return CurrencyExchangeRateResponseDto.builder()
                .baseCurrency(instanceCurrencyDao.findById(straightCurrencyBaseExchangeRate.getTargetCurrencyId()).orElseThrow())
                .targetCurrency(instanceCurrencyDao.findById(straightCurrencyTargetExchangeRate.getTargetCurrencyId()).orElseThrow())
                .rate(crossRate)
                .amount(amount)
                .convertedAmount(convertedAmount)
                .build();
    }

    private ExchangeRate getStraightCurrencyExchangeRateToUSD(String targetCurrencyCode) {
        String referenceCurrency = "USD";
        Optional<ExchangeRate> exchangeRate = instanceExchangeRateDao.findExchangeRate(referenceCurrency, targetCurrencyCode);
        try {
            return exchangeRate.orElseThrow();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            throw new DaoException("Прямой обменный курс отсутствует для указнной валюты");
        }
    }

    private ExchangeRate getReverseCurrencyExchangeRateToUSD(String baseCurrencyCode) {
        String referenceCurrency = "USD";
        Optional<ExchangeRate> baseExchangeRate = instanceExchangeRateDao.findExchangeRate(baseCurrencyCode, referenceCurrency);
        try {
            return baseExchangeRate.orElseThrow();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            throw new DaoException("Обратный обменный курс отсутствует для указнной валюты");
        }
    }

    private BigDecimal calculateCrossRate(ExchangeRate straightBaseRate, ExchangeRate straightTargetRate) {
        BigDecimal baseRate = straightBaseRate.getRate();
        BigDecimal targetRate = straightTargetRate.getRate();

        return targetRate.divide(baseRate, 6, RoundingMode.HALF_DOWN);


    }
}


