package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.ClearContextExtension;
import guru.qa.niffler.jupiter.extension.DbCreateUserExtension;
import guru.qa.niffler.jupiter.extension.JpaExtension;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({
        DbCreateUserExtension.class,
        AllureJunit5.class,
        ClearContextExtension.class,
        JpaExtension.class
})
public @interface SoapTest {
}
