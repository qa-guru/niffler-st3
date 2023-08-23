package guru.qa.niffler.jupiter;

import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)                                                                                             // тут мы определяем что аннотацию можно поставить над методом
@Retention(RetentionPolicy.RUNTIME)                                                                                     // доступна в рантайме
@ExtendWith({SpendExtension.class})                                                                                     // умеет обрабатываться junit экстеншеном
public @interface Spend {                                                                                               // аннотации похожи на поля в классе
    String username();                                                                                                  // они выглядят как методы

    String description();

    String category();

    double amount();

    CurrencyValues currency();
}