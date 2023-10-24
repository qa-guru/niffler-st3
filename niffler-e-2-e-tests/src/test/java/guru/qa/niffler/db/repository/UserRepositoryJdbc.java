package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.dao.impl.AuthUserDAOJdbc;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.List;

public class UserRepositoryJdbc extends AbstractUserRepository {
    public UserRepositoryJdbc() {
        super(new AuthUserDAOJdbc(), new AuthUserDAOJdbc());
    }

    @Override
    public void setFriendsForUser(List<UserJson> friends) {

    }

    @Override
    public UserDataUserEntity getUserData(AuthUserEntity user) {
        return null;
    }

    @Override
    public void addFriendForUser(UserDataUserEntity user, UserDataUserEntity friend) {

    }
}