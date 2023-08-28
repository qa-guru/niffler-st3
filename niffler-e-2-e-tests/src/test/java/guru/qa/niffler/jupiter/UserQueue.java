package guru.qa.niffler.jupiter;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ExtendWith(UserQueueExtension.class)
public @interface UserQueue {

    UserType userType();

    enum UserType {
        INVITE_SENT, INVITE_RECEIVED, FRIEND
    }
}
