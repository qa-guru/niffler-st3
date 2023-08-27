package guru.qa.niffler.db.jpa;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.db.ServiceDB;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EntityManagerFactoryProvider {
    INSTANCE;

    private static final Config cfg = Config.getInstance();

    private final Map<ServiceDB, EntityManagerFactory> dataSourceStore = new ConcurrentHashMap<>();

    public EntityManagerFactory getDataSource(ServiceDB db) {
        return dataSourceStore.computeIfAbsent(db, key -> {
            Map<String, Object> props = new HashMap<>();
            props.put("hibernate.connection.url", db.getUrl());
            props.put("hibernate.connection.user", cfg.databaseUser());
            props.put("hibernate.connection.password", cfg.databasePassword());
            props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

            EntityManagerFactory entityManagerFactory =
                    new ThreadLocalEntityManagerFactory(
                            Persistence.createEntityManagerFactory("niffler-st3", props)
                    );
            return entityManagerFactory;
        });


    }
}
