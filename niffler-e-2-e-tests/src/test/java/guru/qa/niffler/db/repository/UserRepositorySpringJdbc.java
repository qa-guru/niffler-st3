package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.dao.impl.UsersDAOSpringJdbc;

public class UserRepositorySpringJdbc extends AbstractUserRepository {
    public UserRepositorySpringJdbc() {
        super(new UsersDAOSpringJdbc(), new UsersDAOSpringJdbc());
    }
}
