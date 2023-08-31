package guru.qa.niffler.db.dao.impl;

import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.jdbc.DataSourceProvider;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;

public class UserDataUserDAOSpringJdbc implements UserDataUserDAO {

    private final JdbcTemplate userdataJdbcTemplate;

    public UserDataUserDAOSpringJdbc(JdbcTemplate userdataJdbcTemplate) {
        JdbcTransactionManager userdataTm = new JdbcTransactionManager(
                DataSourceProvider.INSTANCE.getDataSource(ServiceDB.USERDATA));
        this.userdataJdbcTemplate = new JdbcTemplate(userdataTm.getDataSource());
    }

    @Override
    public int createUserInUserData(UserDataUserEntity user) {
        return userdataJdbcTemplate.update(
                "INSERT INTO users (username, currency) VALUES (?, ?)",
                user.getUsername(),
                CurrencyValues.RUB.name()
        );
    }

    @Override
    public void deleteUserByNameInUserData(String username) {
        userdataJdbcTemplate.update("DELETE FROM users WHERE username = ?", username);
    }
}
