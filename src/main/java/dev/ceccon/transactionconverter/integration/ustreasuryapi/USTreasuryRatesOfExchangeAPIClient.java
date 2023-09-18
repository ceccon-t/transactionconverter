package dev.ceccon.transactionconverter.integration.ustreasuryapi;

import dev.ceccon.transactionconverter.entities.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class USTreasuryRatesOfExchangeAPIClient {

    private static final String API_DOMAIN = "https://api.fiscaldata.treasury.gov";
    private static final String API_ENDPOINT = "/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";
    private static final String API_BASE_URL = API_DOMAIN + API_ENDPOINT;

    public BigDecimal getConversationRateForCountryCurrency(Transaction transaction, String countryCurrency) {
        return null;
    }

    public BigDecimal getConversationRateForBaseCurrency(Transaction transaction, String baseCurrency) {
        return null;
    }
}
