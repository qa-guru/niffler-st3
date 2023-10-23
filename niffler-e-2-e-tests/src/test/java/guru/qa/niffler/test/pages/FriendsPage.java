package guru.qa.niffler.test.pages;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.annotation.Page;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;

@Page(url = "/friends")
public class FriendsPage {


    SelenideElement tableOfFriends = $x(".//tbody");
    ElementsCollection rowsCollectionOfFriends = tableOfFriends
            .$$x("tr");


    @Step("Проверить, что в таблице Друзей отображается есть запись о друге")
    public void checkTableOfFriendsHasRecordWithCorrectFriendStatus() {
        rowsCollectionOfFriends
                .filter(text("You are friends"))
                .shouldHave(size(1));
    }

    @Step("Проверить, что в таблице Друзей отображается запись о потенциальном друге")
    public void checkTableOfFriendsHasRecordWithCorrectPotentialFriendStatus() {
        rowsCollectionOfFriends
                .shouldHave(size(1));

        rowsCollectionOfFriends
                .first()
                .$x(".//div[@data-tooltip-content = 'Submit invitation']").shouldBe(exist);

        rowsCollectionOfFriends
                .first()
                .$x(".//div[@data-tooltip-content = 'Decline invitation']").shouldBe(exist);
    }

    @Step("Проверить, что в таблице Друзей отображается запись о потенциальном друге")
    public void checkTableOfFriendsHasRecordWithCorrectPotentialFriendStatus(String potentialFriendName) {
        rowsCollectionOfFriends
          .filter(text(potentialFriendName))
                .shouldHave(size(1));

        rowsCollectionOfFriends
                .first()
                .$x(".//div[@data-tooltip-content = 'Submit invitation']").shouldBe(exist);

        rowsCollectionOfFriends
                .first()
                .$x(".//div[@data-tooltip-content = 'Decline invitation']").shouldBe(exist);
    }


}