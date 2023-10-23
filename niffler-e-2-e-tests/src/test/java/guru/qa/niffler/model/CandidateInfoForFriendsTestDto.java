package guru.qa.niffler.model;


import guru.qa.niffler.jupiter.annotation.User;

import java.util.Objects;


public class CandidateInfoForFriendsTestDto {


    private User.UserType userType;

    private String parameterName;

    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CandidateInfoForFriendsTestDto that = (CandidateInfoForFriendsTestDto) o;
        return userType == that.userType && Objects.equals(parameterName, that.parameterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userType, parameterName);
    }

}
