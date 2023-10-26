package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.ClearContextExtension;
import guru.qa.niffler.jupiter.extension.KafkaExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({KafkaExtension.class, ClearContextExtension.class})
public @interface KafkaTest {
}
