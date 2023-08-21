package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    @Step("Select spending")
    public MainPage selectSpending(String containsText) {
        getRowWithSpending(containsText)
                .$("td")
                .scrollTo()
                .click();

        return this;
    }

    @Step("Click Delete selected")
    public MainPage removeSelectedSpending() {
        $(byText("Delete selected")).click();

        return this;
    }

    @Step("Verify spending is present")
    public void spendingIsPresent(String containsText, boolean isPresent) {
        if (isPresent) {
            getRowWithSpending(containsText).should(exist);
        } else {
            getRowWithSpending(containsText).should(not(exist));
        }
    }

    private SelenideElement getRowWithSpending(String containsText) {
        return $(".spendings__content tbody")
                .$$("tr")
                .find(text(containsText));
    }
}
