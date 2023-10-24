package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.dao.impl.AuthUserDAOSpringJdbc;
import guru.qa.niffler.db.dao.impl.UserDataUserDAOSpringJdbc;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

import static guru.qa.niffler.db.model.CurrencyValues.RUB;

public class NifflerUserRepository {

	private final AuthUserDAO authUserDAO = new AuthUserDAOSpringJdbc();
	private final UserdataUserDAO userDataUserDAO= new UserDataUserDAOSpringJdbc();

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

	private void addFriendForUser(UserDataUserEntity user, UserDataUserEntity friend){
		userDataUserDAO.addFriendForUser(user, friend);
	}
}
