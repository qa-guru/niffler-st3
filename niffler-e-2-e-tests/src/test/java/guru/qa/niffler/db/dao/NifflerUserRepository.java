package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.impl.UserdataUserDAOHibernate;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

import static guru.qa.niffler.db.model.CurrencyValues.RUB;

public class NifflerUserRepository {

	private final AuthUserDAO authUserDAO = new AuthUserDAOHibernate();
	private final UserDataUserDAO userDataUserDAO= new UserdataUserDAOHibernate();

	public void createUserForTest(AuthUserEntity user){
		authUserDAO.createUser(user);
		userDataUserDAO.createUserInUserData(fromAuthUser(user));
	}

	public void removeUserAfterTest(AuthUserEntity user){
		UserDataUserEntity userDataUser = userDataUserDAO.getUserData(user.getUsername());
		userDataUserDAO.deleteUser(userDataUser);
		authUserDAO.deleteUser(user);
	}

	private UserDataUserEntity fromAuthUser(AuthUserEntity authUser){
		UserDataUserEntity user = new UserDataUserEntity();
		user.setUsername(authUser.getUsername());
		user.setCurrency(RUB);
		return user;
	}

}
