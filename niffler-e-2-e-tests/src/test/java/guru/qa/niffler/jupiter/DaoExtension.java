package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.AuthUserDAOHibernate;
import guru.qa.niffler.db.dao.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.AuthUserDAOSpringJdbc;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class DaoExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            if ((field.getType().isAssignableFrom(AuthUserDAO.class) || field.getType().isAssignableFrom(UserDataUserDAO.class))
                    && field.isAnnotationPresent(Dao.class)) {
                field.setAccessible(true);

                AuthUserDAO dao;

                if ("hibernate".equals(System.getProperty("db.impl"))) {
                    dao = new AuthUserDAOHibernate();
                } else if ("spring".equals(System.getProperty("db.impl"))) {
                    dao = new AuthUserDAOSpringJdbc();
                } else {
                    dao = new AuthUserDAOJdbc();
                }

                field.set(testInstance, dao);
            }

        }

    }
}
