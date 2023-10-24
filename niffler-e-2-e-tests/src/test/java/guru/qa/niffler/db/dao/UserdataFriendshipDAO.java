package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

public interface UserdataFriendshipDAO {

	UserDataUserEntity addIncomeInvitation(UserDataUserEntity user, UserDataUserEntity incomeFriend);

	UserDataUserEntity addOutcomeInvitation(UserDataUserEntity user, UserDataUserEntity outcomeFriend);
}
