package guru.qa.niffler.jupiter.annotations;

import guru.qa.niffler.jupiter.extention.UserQueueExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(UserQueueExtension.class)
public @interface User {

    UserType userType();

    enum UserType {
        WITH_FRIENDS,WITH_SEND_INVITE,WITH_APPROVE_INVITE
    }

}
