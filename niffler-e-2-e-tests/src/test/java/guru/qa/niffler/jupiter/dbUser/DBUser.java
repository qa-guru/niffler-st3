package guru.qa.niffler.jupiter.dbUser;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({DBUserExtension.class, DBUserExtensionParameterResolver.class})
public @interface DBUser {

	String username();

	String password();
}
