package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;

public class PeoplePage {

    private final ElementsCollection tableRows = $$x("//table//tr");

    @Step("Проверяет, что в таблице содержится отправка запроса на добавление в друзья")
    public PeoplePage checkInvitationToFriendSent() {
        tableRows.filter(text("Pending invitation")).shouldHave(size(1));
        return this;
    }

    @Step("Проверяет, что в таблице содержится запрос на добавление в друзья от пользователя {name}")
    public PeoplePage checkInvitationToFriendSent(String name) {
        var row = tableRows.find(text(name));
        row.$x(".//div[@data-tooltip-content = 'Submit invitation']").shouldBe(visible);
        row.$x(".//div[@data-tooltip-content = 'Decline invitation']").shouldBe(visible);
        return this;
    }

    @Step("Проверяет, что в списке людей есть друг")
    public PeoplePage checkUserHaveFriend() {
        tableRows.filter(text("You are friends")).shouldHave(size(1));
        return this;
    }
}
