package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CategoryJson {

    @JsonProperty("category")
    private String category;

    @JsonProperty("username")
    private String username;

    @JsonProperty("id")
    private UUID id;

    public CategoryJson() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String description) {
        this.category = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
