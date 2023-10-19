package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.dao.impl.AuthUserDAOSpringJdbc;

public class UserRepositorySpringJdbc extends AbstractUserRepository {
    public UserRepositorySpringJdbc() {
        super(new AuthUserDAOSpringJdbc(), new AuthUserDAOSpringJdbc());
    }
}
