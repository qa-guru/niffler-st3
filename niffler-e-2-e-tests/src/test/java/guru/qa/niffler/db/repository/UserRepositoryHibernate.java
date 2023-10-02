package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.dao.impl.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.impl.UserdataUserDAOHibernate;

public class UserRepositoryHibernate extends AbstractUserRepository {
    public UserRepositoryHibernate() {
        super(new AuthUserDAOHibernate(), new UserdataUserDAOHibernate());
    }
}
