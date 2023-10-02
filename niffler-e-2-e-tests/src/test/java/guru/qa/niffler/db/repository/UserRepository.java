package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.auth.UserEntity;
import guru.qa.niffler.db.model.userdata.UserDataEntity;

public interface UserRepository {
    void createUserForTest(UserEntity user);

    void removeAfterTest(UserEntity user);
}
