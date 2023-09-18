package dev.ceccon.transactionconverter.integration.ustreasuryapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class ExchangeRateRecordDTO {

    @JsonProperty(value = "exchange_rate")
    private BigDecimal exchangeRate;
    @JsonProperty(value = "record_date")
    private LocalDate recordDate;

    public ExchangeRateRecordDTO() {

    }

    public ExchangeRateRecordDTO(BigDecimal exchangeRate, LocalDate recordDate) {
        this.exchangeRate = exchangeRate;
        this.recordDate = recordDate;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExchangeRateRecordDTO that = (ExchangeRateRecordDTO) o;

        if (!Objects.equals(exchangeRate, that.exchangeRate)) return false;
        return Objects.equals(recordDate, that.recordDate);
    }

    @Override
    public int hashCode() {
        int result = exchangeRate != null ? exchangeRate.hashCode() : 0;
        result = 31 * result + (recordDate != null ? recordDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExchangeRateRecordDTO{" +
                "exchangeRate=" + exchangeRate +
                ", recordDate=" + recordDate +
                '}';
    }
}
