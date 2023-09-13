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
        UserDataUserEntity userInUd = udUserDAO.getUserInUserDataByUsername(user.getUsername());
        udUserDAO.deleteUserInUserData(userInUd);
        authUserDAO.deleteUser(user);
    }

    private UserDataUserEntity fromAuthUser(AuthUserEntity user) {
        UserDataUserEntity userdataUser = new UserDataUserEntity();
        userdataUser.setUsername(user.getUsername());
        userdataUser.setCurrency(CurrencyValues.RUB);
        return userdataUser;
    }
}
