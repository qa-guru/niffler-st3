package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {

    private final SelenideElement categoryFld = $x("//input[@name = 'category']");
    private final SelenideElement createCategoryBtn = $x("//button[text() = 'Create']");
    private final ElementsCollection categories = $$x("//li[@class = 'categories__item']");
    private final SelenideElement firstNameFld = $x("//input[@name = 'firstname']");
    private final SelenideElement surnameFld = $x("//input[@name = 'surname']");
    private final SelenideElement submitBtn = $x("//button[text() = 'Submit']");
    private final SelenideElement currencyFld = $x("//div[contains (@class, 'singleValue')]");

    @Step("Заполнить поле \"Категория\"")
    public ProfilePage fillCategoryName(String categoryName) {
        categoryFld.setValue(categoryName);
        return this;
    }

    @Step("Заполнить поле \"Name\"")
    public ProfilePage fillName(String name) {
        firstNameFld.setValue(name);
        return this;
    }

    @Step("Заполнить поле \"Surname\"")
    public ProfilePage fillSurname(String surname) {
        surnameFld.setValue(surname);
        return this;
    }

    @Step("Нажать кнопку \"Create\"")
    public ProfilePage clickCreateButton() {
        createCategoryBtn.click();
        return this;
    }

    @Step("Нажать кнопку \"Submit\"")
    public ProfilePage clickSubmitButton() {
        submitBtn.scrollTo().click();
        return this;
    }

    @Step("Проверяет, что отображается категория с именем {name}")
    public ProfilePage checkCategoryWithName(String name) {
        categories.shouldBe(sizeGreaterThan(0),itemWithText(name));
        return this;
    }

    @Step("Проверяет, что поле \"Name\" заполненоно значением {name}")
    public ProfilePage checkValueName(String name) {
        firstNameFld.shouldBe(value(name));
        return this;
    }

    @Step("Проверяет, что поле \"Surname\" заполненоно значением {surname}")
    public ProfilePage checkValueSurname(String surname) {
        surnameFld.shouldBe(value(surname));
        return this;
    }

    @Step("Выбрать Currency")
    public ProfilePage setCurrency(String currency) {
        $x("//div[contains (@class, 'indicatorContainer')]").click();
        $$x("//div[contains (@class, 'option')]")
                .filter(Condition.text(currency))
                .first()
                .click();
        clickSubmitButton();
        return this;
    }

    @Step("Проверяет, что \"Currency\" заполнено значением {currency}")
    public ProfilePage checkCurrencyValue(String currency) {
        currencyFld.shouldBe(exactText(currency));
        return this;
    }
}
