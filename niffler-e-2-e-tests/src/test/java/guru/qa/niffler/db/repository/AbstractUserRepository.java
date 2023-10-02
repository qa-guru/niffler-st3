package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.auth.UserEntity;
import guru.qa.niffler.db.model.userdata.UserDataEntity;

public abstract class AbstractUserRepository implements UserRepository {
    private final AuthUserDAO authUserDAO;
    private final UserDataUserDAO udUserDAO;

    protected AbstractUserRepository(AuthUserDAO authUserDAO, UserDataUserDAO udUserDAO) {
        this.authUserDAO = authUserDAO;
        this.udUserDAO = udUserDAO;
    }

    @Override
    public void createUserForTest(UserEntity user) {
        authUserDAO.createUser(user);
        udUserDAO.createUserInUserData(fromAuthUser(user));
    }

    @Override
    public void removeAfterTest(UserEntity user) {
        UserDataEntity userInUd = udUserDAO.getUserInUserDataByUsername(user.getUsername());
        udUserDAO.deleteUserByUsernameInUserData(userInUd.getUsername());
        authUserDAO.deleteUserById(user.getId());
    }

    private UserDataEntity fromAuthUser(UserEntity user) {
        UserDataEntity userdataUser = new UserDataEntity();
        userdataUser.setUsername(user.getUsername());
        userdataUser.setCurrency(CurrencyValues.RUB);
        return userdataUser;
    }
}
