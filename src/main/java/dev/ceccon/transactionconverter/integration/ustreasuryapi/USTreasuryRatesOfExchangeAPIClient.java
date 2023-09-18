package dev.ceccon.transactionconverter.integration.ustreasuryapi;

import dev.ceccon.transactionconverter.entities.Transaction;
import dev.ceccon.transactionconverter.exceptions.NoSuitableExchangeRateException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class USTreasuryRatesOfExchangeAPIClient {

    private static final String API_DOMAIN = "https://api.fiscaldata.treasury.gov";
    private static final String API_ENDPOINT = "/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";
    private static final String API_BASE_URL = API_DOMAIN + API_ENDPOINT;

    private final RestTemplate restTemplate;

    public USTreasuryRatesOfExchangeAPIClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String buildFiltersParam(LocalDate date, String currencyOption) {
        StringBuilder sb = new StringBuilder();
        sb.append("record_date:lte:"); sb.append(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        sb.append(",record_date:gte:"); sb.append(date.minus(6, ChronoUnit.MONTHS).format(DateTimeFormatter.ISO_LOCAL_DATE));
        sb.append(",");
        sb.append(currencyOption);
        return sb.toString();
    }

    private String filterOptionForBaseCurrency(String baseCurrency) {
        return "currency:eq:" + baseCurrency;
    }

    private String filterOptionForCountryCurrency(String countryCurrency) {
        return "country_currency_desc:eq:" + countryCurrency;
    }

    private String buildRequestPath(LocalDate date, String currencyOption) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(API_BASE_URL);
        uriComponentsBuilder.queryParam("fields", "exchange_rate,record_date");
        uriComponentsBuilder.queryParam("filter", buildFiltersParam(date, currencyOption));
        uriComponentsBuilder.queryParam("sort", "-record_date");

        return uriComponentsBuilder.toUriString();
    }

    private BigDecimal getConversionRate(Transaction transaction, String currencyOption) {
        String requestPath = buildRequestPath(transaction.getDate(), currencyOption);

        USTreasuryRatesOfExchangeAPIResponseDTO dto = restTemplate.getForObject(
                requestPath, USTreasuryRatesOfExchangeAPIResponseDTO.class
        );

        if (dto.getData().isEmpty()) {
            throw new NoSuitableExchangeRateException("No suitable exchange rate to convert transaction value was found");
        }

        return dto.getData().get(0).getExchangeRate();
    }

    public BigDecimal getConversationRateForCountryCurrency(Transaction transaction, String countryCurrency) {
        return getConversionRate(transaction, filterOptionForCountryCurrency(countryCurrency));
    }

    public BigDecimal getConversationRateForBaseCurrency(Transaction transaction, String baseCurrency) {
        return getConversionRate(transaction, filterOptionForBaseCurrency(baseCurrency));
    }
}
