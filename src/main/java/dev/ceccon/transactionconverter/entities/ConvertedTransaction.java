package dev.ceccon.transactionconverter.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class ConvertedTransaction {

    private Long id;
    private String description;
    private LocalDate date;
    private BigDecimal amountInUSDollars;
    private String targetCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;

    public ConvertedTransaction() {

    }

    public ConvertedTransaction(Transaction transaction, String targetCurrency, BigDecimal exchangeRate) {
        this.id = transaction.getId();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.amountInUSDollars = transaction.getAmountInUSDollars();
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
        this.convertedAmount = this.amountInUSDollars.multiply(new BigDecimal(String.valueOf(exchangeRate)))
                                                        .setScale(2, RoundingMode.HALF_EVEN);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmountInUSDollars() {
        return amountInUSDollars;
    }

    public void setAmountInUSDollars(BigDecimal amountInUSDollars) {
        this.amountInUSDollars = amountInUSDollars;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConvertedTransaction that = (ConvertedTransaction) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(date, that.date)) return false;
        if (!Objects.equals(amountInUSDollars, that.amountInUSDollars))
            return false;
        if (!Objects.equals(targetCurrency, that.targetCurrency))
            return false;
        if (!Objects.equals(exchangeRate, that.exchangeRate)) return false;
        return Objects.equals(convertedAmount, that.convertedAmount);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (amountInUSDollars != null ? amountInUSDollars.hashCode() : 0);
        result = 31 * result + (targetCurrency != null ? targetCurrency.hashCode() : 0);
        result = 31 * result + (exchangeRate != null ? exchangeRate.hashCode() : 0);
        result = 31 * result + (convertedAmount != null ? convertedAmount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConvertedTransaction{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", amountInUSDollars=" + amountInUSDollars +
                ", targetCurrency='" + targetCurrency + '\'' +
                ", exchangeRate=" + exchangeRate +
                ", convertedAmount=" + convertedAmount +
                '}';
    }
}
