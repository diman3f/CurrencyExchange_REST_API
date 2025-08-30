package org.pet.services;

import org.pet.dao.CurrencyDAO;
import org.pet.dao.ExchangeRateDao;
import org.pet.dto.CurrencyExchangeRateRequestDto;
import org.pet.dto.CurrencyExchangeRateResponseDto;
import org.pet.dto.ExchangeRateRequestServletDTO;
import org.pet.dto.ExchangeRateResponseDTO;
import org.pet.entity.Currency;
import org.pet.entity.ExchangeRate;
import org.pet.exception.DataBaseException;
import org.pet.exception.ExchangeRateException;
import org.pet.mapper.ExchangeRateMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private static CurrencyDAO instanceCurrencyDao = CurrencyDAO.getINSTANCE();
    private static ExchangeRateDao instanceExchangeRateDao = ExchangeRateDao.getInstance();

    private ExchangeRateService() {
    }

    public static ExchangeRateService getINSTANCE() {
        return INSTANCE;
    }

    public ExchangeRateResponseDTO getExchangeRate(ExchangeRateRequestServletDTO exchangeRateRequestDTO) {

        String baseCode = exchangeRateRequestDTO.getBaseCode();
        String targetCode = exchangeRateRequestDTO.getTargetCode();
        ExchangeRate exchangeRate = instanceExchangeRateDao.findExchangeRate(baseCode, targetCode);
        Currency currencyBase = instanceCurrencyDao.findByCode(baseCode);
        Currency currencyTarget = instanceCurrencyDao.findByCode(targetCode);
        ExchangeRateMapper instance = ExchangeRateMapper.INSTANCE;
        return instance.toExchangeRate(currencyBase, currencyTarget, exchangeRate);
    }

    public List<ExchangeRateResponseDTO> getAllExchangeRate() {
        List<ExchangeRateResponseDTO> exchangeRateResponseDTOList = new ArrayList<>();
        ExchangeRateMapper mapper = ExchangeRateMapper.INSTANCE;
        try {
            List<ExchangeRate> exchangeRate = instanceExchangeRateDao.findAllExchangeRate();
            if (!exchangeRate.isEmpty()) {
                for (ExchangeRate er : exchangeRate) {
                    Currency baseCurrency = instanceCurrencyDao.findById(er.getBaseCurrencyId());
                    Currency targetCurrency = instanceCurrencyDao.findById(er.getTargetCurrencyId());
                    exchangeRateResponseDTOList.add(mapper.toExchangeRate(baseCurrency, targetCurrency, er));
                }
            }
        } catch (RuntimeException e) {
            throw new DataBaseException("Ошибка на стороне сервера");
        }
        return exchangeRateResponseDTOList;
    }

    public ExchangeRateResponseDTO saveExchangeRate(ExchangeRateRequestServletDTO dtoExchangeRate) {
        Currency baseCurrency = instanceCurrencyDao.findByCode(dtoExchangeRate.getBaseCode());
        Currency targetCurrency = instanceCurrencyDao.findByCode(dtoExchangeRate.getTargetCode());

        ExchangeRate exchangeRate = ExchangeRate.builder()
                .baseCurrencyId(baseCurrency.getId())
                .targetCurrencyId(targetCurrency.getId())
                .rate(dtoExchangeRate.getRate())
                .build();
        ExchangeRate result = instanceExchangeRateDao.saveExchangeRate(exchangeRate);
        exchangeRate = result;
        return ExchangeRateMapper.INSTANCE.toExchangeRate(baseCurrency, targetCurrency, exchangeRate);
    }

    public ExchangeRateResponseDTO updateExchangeRate(ExchangeRateRequestServletDTO dto) {
        try {
            Currency baseCurrency = instanceCurrencyDao.findByCode(dto.getBaseCode());
            Currency targetCurrency = instanceCurrencyDao.findByCode(dto.getTargetCode());
            ExchangeRate exchangeRate = instanceExchangeRateDao.findExchangeRate(dto.getBaseCode(), dto.getTargetCode());
            exchangeRate.setRate(dto.getRate());
            ExchangeRate updatedExchangeRate = instanceExchangeRateDao.updateExchangeRate(exchangeRate);
            return ExchangeRateMapper.INSTANCE.toExchangeRate(baseCurrency, targetCurrency, updatedExchangeRate);
        } catch (RuntimeException e) {
            throw new ExchangeRateException("Валютная пара отсутствует в базе данных");
        }
    }

    public CurrencyExchangeRateResponseDto executeExchangeCurrency(CurrencyExchangeRateRequestDto dto) {
        String baseCurrency = dto.getBaseCurrency();
        String targetCurrency = dto.getTargetCurrency();
        double amount = dto.getAmount();
        try {
            if (isExchangeRateByStraightRate(baseCurrency, targetCurrency)) {
                ExchangeRate exchangeRate = instanceExchangeRateDao.findExchangeRate(baseCurrency, targetCurrency);
                BigDecimal rate = exchangeRate.getRate();
                BigDecimal convertedAmount = ((rate.multiply(BigDecimal.valueOf(dto.getAmount()))));
                convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_EVEN);
                return CurrencyExchangeRateResponseDto.builder()
                        .baseCurrency(instanceCurrencyDao.findById(exchangeRate.getBaseCurrencyId()))
                        .targetCurrency(instanceCurrencyDao.findById(exchangeRate.getTargetCurrencyId()))
                        .rate(rate)
                        .amount(dto.getAmount())
                        .convertedAmount(convertedAmount)
                        .build();
            } else if (isExchangeRateByReverseRate(targetCurrency, baseCurrency)) {
                ExchangeRate exchangeRate = instanceExchangeRateDao.findExchangeRate(targetCurrency, baseCurrency);
                CurrencyExchangeRateResponseDto result = buildDtoFromExchangeRate(exchangeRate, amount);
                return result;
            } else return buildDtoCalculateCrossRate(baseCurrency, targetCurrency, amount);
        } catch (RuntimeException e) {
            throw new ExchangeRateException("Произвести конвертацию для указанной пары валют невозможно");
        }
    }

    private CurrencyExchangeRateResponseDto buildDtoFromExchangeRate(ExchangeRate rate, double amount) {

        BigDecimal inverseRate = calculateInverseRate(rate);
        BigDecimal convertedAmount = ((inverseRate.multiply(BigDecimal.valueOf(amount))));
        convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_EVEN);
        return CurrencyExchangeRateResponseDto.builder()
                .baseCurrency(instanceCurrencyDao.findById(rate.getTargetCurrencyId()))
                .targetCurrency(instanceCurrencyDao.findById(rate.getBaseCurrencyId()))
                .rate(calculateReverseRate(rate))
                .amount(amount)
                .convertedAmount(convertedAmount)
                .build();
    }

    private BigDecimal calculateInverseRate(ExchangeRate rate) {
        BigDecimal exchangeRate = rate.getRate();
        return BigDecimal.ONE.divide(exchangeRate);
    }

    private BigDecimal calculateReverseRate(ExchangeRate exchangeRate) {
        BigDecimal one = new BigDecimal("1");
        BigDecimal target = exchangeRate.getRate();
        return one.divide(target, 6, RoundingMode.HALF_DOWN);
    }

    private boolean isExchangeRateByStraightRate(String baseCurrency, String targetCurrency) {
        try {
            ExchangeRate straightRate = instanceExchangeRateDao.findExchangeRate(baseCurrency, targetCurrency);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    private boolean isExchangeRateByReverseRate(String targetCurrency, String baseCurrency) {
        try {
            ExchangeRate reverseRate = instanceExchangeRateDao.findExchangeRate(targetCurrency, baseCurrency);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    private CurrencyExchangeRateResponseDto buildDtoCalculateCrossRate(String baseCode, String targetCode, double amount) {
        ExchangeRate straightCurrencyBaseExchangeRate = getStraightCurrencyExchangeRateToUSD(baseCode);
        ExchangeRate straightCurrencyTargetExchangeRate = getStraightCurrencyExchangeRateToUSD(targetCode);
        BigDecimal crossRate = calculateCrossRate(straightCurrencyBaseExchangeRate, straightCurrencyTargetExchangeRate);
        BigDecimal convertedAmount = (crossRate.multiply(BigDecimal.valueOf(amount)).setScale(2, RoundingMode.HALF_EVEN));
        return CurrencyExchangeRateResponseDto.builder()
                .baseCurrency(instanceCurrencyDao.findById(straightCurrencyBaseExchangeRate.getTargetCurrencyId()))
                .targetCurrency(instanceCurrencyDao.findById(straightCurrencyTargetExchangeRate.getTargetCurrencyId()))
                .rate(crossRate)
                .amount(amount)
                .convertedAmount(convertedAmount)
                .build();
    }

    private ExchangeRate getStraightCurrencyExchangeRateToUSD(String targetCurrencyCode) {
        String referenceCurrency = "USD";
        ExchangeRate exchangeRate = instanceExchangeRateDao.findExchangeRate(referenceCurrency, targetCurrencyCode);
        return exchangeRate;
    }

    private BigDecimal calculateCrossRate(ExchangeRate straightBaseRate, ExchangeRate straightTargetRate) {
        BigDecimal baseRate = straightBaseRate.getRate();
        BigDecimal targetRate = straightTargetRate.getRate();
        return targetRate.divide(baseRate, 6, RoundingMode.HALF_DOWN);
    }
}


