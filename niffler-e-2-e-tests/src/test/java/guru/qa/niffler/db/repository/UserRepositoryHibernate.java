package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.UserdataUserDAOHibernate;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.List;

public class UserRepositoryHibernate extends AbstractUserRepository {
    public UserRepositoryHibernate() {
        super(new AuthUserDAOHibernate(), new UserdataUserDAOHibernate());
    }

    public void addFriendForUser(UserDataUserEntity user, UserDataUserEntity friend){

    }

    @Override
    public void setFriendsForUser(List<UserJson> friends) {

    }

    @Override
    public UserDataUserEntity getUserData(AuthUserEntity user) {
        return null;
    }
}