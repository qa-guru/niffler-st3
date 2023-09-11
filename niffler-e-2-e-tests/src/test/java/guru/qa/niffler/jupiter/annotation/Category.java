package guru.qa.niffler.jupiter.annotation;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.UUID;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(CategoryExtension.class)
public @interface Category {
    String category();

    String username();


}
