package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(UserQueueExtension.class)
public @interface User {

    UserType userType();

    enum UserType {
        WITH_FRIENDS, INVITATION_SENT, INVITATION_RECEIVED
    }
}
