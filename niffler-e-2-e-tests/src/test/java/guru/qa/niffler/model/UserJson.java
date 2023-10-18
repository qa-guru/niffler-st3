package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.User;

import java.util.List;
import java.util.UUID;

public class UserJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("friendState")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FriendState friendState;

    transient String password;
    transient User.UserType userType;
    transient List<UserJson> friends;
    transient List<UserJson> incomeInvitations;
    transient List<UserJson> outcomeInvitations;

    public UserJson() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public FriendState getFriendState() {
        return friendState;
    }

    public void setFriendState(FriendState friendState) {
        this.friendState = friendState;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }

    public List<UserJson> getFriends() {
        return friends;
    }

    public void setFriends(List<UserJson> friends) {
        this.friends = friends;
    }

    public List<UserJson> getIncomeInvitations() {
        return incomeInvitations;
    }

    public void setIncomeInvitations(List<UserJson> incomeInvitations) {
        this.incomeInvitations = incomeInvitations;
    }

    public List<UserJson> getOutcomeInvitations() {
        return outcomeInvitations;
    }

    public void setOutcomeInvitations(List<UserJson> outcomeInvitations) {
        this.outcomeInvitations = outcomeInvitations;
    }

    public static UserJson fromEntity(AuthUserEntity entity) {
        UserJson usr = new UserJson();
        usr.setId(entity.getId());
        usr.setUsername(entity.getUsername());
        return usr;
    }
}
