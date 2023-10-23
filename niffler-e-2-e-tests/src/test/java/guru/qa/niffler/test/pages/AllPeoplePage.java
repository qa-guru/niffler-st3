package guru.qa.niffler.test.pages;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.annotation.Page;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;

@Page(url = "/people")
public class AllPeoplePage {


    SelenideElement tableOfFriends = $x(".//tbody");
    ElementsCollection rowsCollectionOfFriends = tableOfFriends
            .$$x("tr");


    @Step("Проверить, что в таблице Пользователей отображается запись о потенциальном друге")
    public void checkTableOfFriendsHasRecordWithCorrectPotentialFriendStatus() {
        rowsCollectionOfFriends
                .filter(text("Pending invitation"))
                .shouldHave(size(1));
    }
}
