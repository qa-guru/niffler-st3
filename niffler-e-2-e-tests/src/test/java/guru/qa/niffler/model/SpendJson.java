package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

public class SpendJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("spendDate")
    private Date spendDate;
    @JsonProperty("category")
    private String category;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("username")
    private String username;

    public SpendJson() {
    }

    public String getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }
    public String getUsername() {
        return username;
    }


    /** Setters */
    public SpendJson spendDate(Date spendDate) {
        this.spendDate = spendDate;
        return this;
    }
    public SpendJson category(String category) {
        this.category = category;
        return this;
    }
    public SpendJson currency(CurrencyValues currency) {
        this.currency = currency;
        return this;
    }
    public SpendJson amount(Double amount) {
        this.amount = amount;
        return this;
    }
    public SpendJson description(String description) {
        this.description = description;
        return this;
    }
    public SpendJson username(String username) {
        this.username = username;
        return this;
    }
}
