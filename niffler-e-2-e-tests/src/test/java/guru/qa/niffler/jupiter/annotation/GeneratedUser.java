package guru.qa.niffler.jupiter.annotation;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface GeneratedUser {

    Selector selector() default Selector.NESTED;


    enum Selector {
        NESTED, OUTER
    }
}
