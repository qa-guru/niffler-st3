package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.UserdataUserDAOHibernate;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.List;

public class FriendRepositoryHibernate extends AbstractUserRepository {
    public FriendRepositoryHibernate() {
        super(new AuthUserDAOHibernate(), new UserdataUserDAOHibernate());
    }

    void addFriendForUser(UserDataUserEntity user, UserDataUserEntity friend, boolean pending){

    }

    @Override
    public void setFriendsForUser(List<UserJson> friends) {

    }


    @Override
    public UserDataUserEntity getUserData(AuthUserEntity user) {
        return null;
    }

}