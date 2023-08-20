package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CategoryJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("category")
    private String category;
    @JsonProperty("username")
    private String username;

    public UUID getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getUsername() {
        return username;
    }


    /** Setters */
    public CategoryJson category(String category) {
        this.category = category;
        return this;
    }
    public CategoryJson username(String username) {
        this.username = username;
        return this;
    }

}
