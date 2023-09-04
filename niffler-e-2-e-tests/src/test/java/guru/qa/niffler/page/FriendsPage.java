package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$x;

public class FriendsPage {

    private final ElementsCollection tableRows = $$x("//table//tr");

    @Step("Проверяет, что в списке друзей есть друг")
    public FriendsPage checkUserHaveFriend() {
        tableRows.filter(text("You are friends")).shouldHave(size(1));
        return this;
    }

    @Step("Проверяет, что в таблице присутствует запрос на добавление в друзья от пользователя с именем {name}")
    public FriendsPage checkUserHaveFriendInvitation(String name) {
        tableRows.filter(text(name)).shouldHave(size(1));
        return this;
    }
}
