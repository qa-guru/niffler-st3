package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

    private final SelenideElement statistics = $x("//section[contains(@class, 'main-content__section-stats')]");

    @Step("Проверяет наличие элемента \"Statistics\"")
    public MainPage checkVisibleStatistics() {
        statistics.shouldBe(visible);
        return this;
    }

    @Step("Выбрать spending с текстом {text}")
    public MainPage setCheckboxForSpendingWithDescription(String description) {
        $(".spendings__content tbody")
                .$$("tr")
                .find(text(description))
                .$("td")
                .scrollTo()
                .click();
        return this;
    }

    @Step("Нажать кнопку \"Удалить\"")
    public MainPage clickDeleteBtn() {
        $(byText("Delete selected")).click();
        return this;
    }

    @Step("Проверяет, что таблица spending отсутствует spendin")
    public MainPage checkTableSpendingIsEmpty(String description) {
        $(".spendings__content tbody")
                .$$("tr")
                .filter(text(description))
                .shouldHave(size(0));
        return this;
    }
}
