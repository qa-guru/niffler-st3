package guru.qa.niffler.test;

import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.jupiter.Spend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;

public class SpendingWebTest extends BaseWebTest{

    private final ElementsCollection rows = $(".spendings__content tbody").$$("tr");

    @Category(
            username = "sergey",
            description = "Рыбалка"
    )
    @Spend(
            username = "sergey",
            description = "Рыбалка",
            category = "Рыбалка",
            amount = 14000.00,
            currency = CurrencyValues.RUB
    )
    @Test
    @DisplayName("Тест на удаление трат")
    void spendingShouldBeDeletedAfterDeleteAction(SpendJson createdSpend) {
        final int requiredRows = 1;

        doLoginStep("sergey", "12345");

        step("[Предусловие]: Таблица трат должна содержать "+requiredRows+" строку", ()->{
            rows.shouldHave(sizeGreaterThan(0));
            Assumptions.assumeTrue(rows.size()==requiredRows, "В таблице кол-во строк больше "+requiredRows);
        });

        step("Выбрать чекбокс для первой строки в таблице трат", ()->{
            Assertions.assertDoesNotThrow(()->rows
                    .find(text(createdSpend.getDescription()))
                    .$$("td")
                    .first()
                    .scrollTo()
                    .click(), "Не удалось выбрать чекбокс для первой строки в таблице трат");
        });

        step("Нажать кнопку \"Delete selected\"", ()->{
            $(byText("Delete selected")).click();
        });

        step("Количество строк в таблице должно быть равно нулю", ()->{
            rows.shouldHave(size(0));
        });
    }
}
