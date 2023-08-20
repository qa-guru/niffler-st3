package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.jupiter.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SpendingWebTest {

    //Дописать тест на таблицу спендингов (@Test void spendingShouldBeDeletedAfterDeleteAction())
    // с первого занятия, перед тестом должен выполниться еще один, новый Extension (он должен также реализовывать интерфейс BeforeEachCallback).
    // Он должен создавать категории для заданного пользователя. Их можно создать напрямую через REST API сервиса niffler-spend - метод @PostMapping("/category")
    // – используйте любой знакомый Вам http клиент (RA, Retrofit, Apache http, java http и так далее, но для единообразия лучше retrofit).
    // Пользователя для теста создайте просто руками через функционал регистрации в ниффлере (по аналогии с моим пользователем dima).
    // Над тестом должно быть двe аннотации – одна создает категории, вторая – спендинги. Для категорий завести свою аннотацию @Category

    private final String USERNAME = "dima";
    private final String PASSWORD = "12345";
    private final String CATEGORY = "Спорт";
    private final String DESCRIPTION = "Бег по Ладоге";
    private final double AMOUNT = 12000.00;


    static {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1980x1024";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(USERNAME);
        $("input[name='password']").setValue(PASSWORD);
        $("button[type='submit']").click();
    }
    @Category(
            username = USERNAME,
            category = CATEGORY
    )


    @Spend(
            username = USERNAME,
            description = DESCRIPTION,
            category = CATEGORY,
            amount = AMOUNT,
            currency = CurrencyValues.RUB
    )
    @Test
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {
        $(".spendings__content tbody")
                .$$("tr")
                .find(text(createdSpend.getDescription()))
                .$("td")
                .scrollTo()
                .click();

        $(byText("Delete selected")).click();

        $(".spendings__content tbody")
                .$$("tr")
                .shouldHave(size(0));
    }
}
