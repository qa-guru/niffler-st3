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

    public String getCategory() {
        return category;
    }

    public CategoryJson setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public CategoryJson setUsername(String username) {
        this.username = username;
        return this;
    }
}
