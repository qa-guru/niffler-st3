package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.dao.impl.AuthUserDAOJdbc;

public class UserRepositoryJdbc extends AbstractUserRepository {
    public UserRepositoryJdbc() {
        super(new AuthUserDAOJdbc(), new AuthUserDAOJdbc());
    }
}
