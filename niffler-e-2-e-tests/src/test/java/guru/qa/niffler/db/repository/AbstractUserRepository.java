package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserdataUserDAO;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

public abstract class AbstractUserRepository implements UserRepository {
    private final AuthUserDAO authUserDAO;
    private final UserdataUserDAO udUserDAO;

    protected AbstractUserRepository(AuthUserDAO authUserDAO, UserdataUserDAO udUserDAO) {
        this.authUserDAO = authUserDAO;
        this.udUserDAO = udUserDAO;
    }

    @Override
    public void createUserForTest(AuthUserEntity user) {
        authUserDAO.createUser(user);
        udUserDAO.createUserInUserData(fromAuthUser(user));
    }

    @Override
    public void removeAfterTest(AuthUserEntity user) {
        UserDataUserEntity userInUd = udUserDAO.getUserData(user.getUsername());
        udUserDAO.deleteUser(userInUd);
        authUserDAO.deleteUser(user);
    }

    private UserDataUserEntity fromAuthUser(AuthUserEntity user) {
        UserDataUserEntity userdataUser = new UserDataUserEntity();
        userdataUser.setUsername(user.getUsername());
        userdataUser.setCurrency(CurrencyValues.RUB);
        return userdataUser;
    }

    public void makeUsersFriends(UserDataUserEntity firstUser, UserDataUserEntity secondUser) {
        firstUser.addFriends(false, secondUser);
        secondUser.addFriends(false, firstUser);
        udUserDAO.updateUserInUserData(firstUser);
        udUserDAO.updateUserInUserData(secondUser);
    }

    public void updateUserForTest(UserDataUserEntity user) {
        udUserDAO.updateUserInUserData(user);
    }

    @Override
    public UserDataUserEntity getUserData(AuthUserEntity user) {
        return udUserDAO.getUserData(user.getUsername());
    }

    @Override
    public void addFriendForUser(UserDataUserEntity user, UserDataUserEntity friend) {
        udUserDAO.addFriendForUser(user, friend);
    }

    @Override
    public void addInvitation(UserDataUserEntity user, UserDataUserEntity friend) {
        udUserDAO.addInvitationForFriend(user, friend);
    }
}