package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.dao.impl.AuthUserDAOSpringJdbc;
import guru.qa.niffler.db.dao.impl.UserDataUserDAOSpringJdbc;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.List;

import static guru.qa.niffler.db.model.CurrencyValues.RUB;

public class UserRepositorySpringJdbc extends AbstractUserRepository {
    public UserRepositorySpringJdbc() {
        super(new AuthUserDAOSpringJdbc(), new UserDataUserDAOSpringJdbc());
    }

    @Override
    public void setFriendsForUser(List<UserJson> friends) {

    }

    @Override
    public UserDataUserEntity getUserData(AuthUserEntity user) {
        UserDataUserEntity userDataUserEntity = new UserDataUserEntity();
        userDataUserEntity.setUsername(user.getUsername());
        userDataUserEntity.setCurrency(RUB);
        return userDataUserEntity;
    }

}