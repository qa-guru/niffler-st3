package guru.qa.niffler.jupiter.spend;

import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({SpendExtension.class})
public @interface Spend {

    String username();
    String description();
    String category();
    double amount();
    CurrencyValues currency();
}