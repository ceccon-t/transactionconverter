package dev.ceccon.transactionconverter.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Validated
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Size(max=50)
    @Column(length = 50)
    private String description;

    private LocalDate date;

    @DecimalMin(value = "0.0")
    @Digits(integer = 38, fraction = 2)
    private BigDecimal amountInUSDollars;

    public Transaction() {

    }

    public Transaction(Long id, String description, LocalDate date, BigDecimal amountInUSDollars) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.amountInUSDollars = amountInUSDollars;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(date, that.date)) return false;
        return Objects.equals(amountInUSDollars, that.amountInUSDollars);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (amountInUSDollars != null ? amountInUSDollars.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", amountInUSDollars=" + amountInUSDollars +
                '}';
    }
}
