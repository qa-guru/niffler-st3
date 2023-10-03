package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.jpa.EntityManagerFactoryProvider;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.extension.ExtensionContext;

public class JpaExtension implements SuiteExtension {

    @Override
    public void beforeAllTests(ExtensionContext extensionContext) {
        System.out.println("#### BEFORE ALL TESTS! ####");
    }

    @Override
    public void afterAllTests() {
        System.out.println("#### AFTER ALL TESTS! ####");
        EntityManagerFactoryProvider.INSTANCE.allStoredEntityManagerFactories()
                .forEach(EntityManagerFactory::close);
    }
}
