package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.dao.impl.UsersDAOJdbc;

public class UserRepositoryJdbc extends AbstractUserRepository {
    public UserRepositoryJdbc() {
        super(new UsersDAOJdbc(), new UsersDAOJdbc());
    }
}
