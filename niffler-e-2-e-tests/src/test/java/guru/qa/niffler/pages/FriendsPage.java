package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.model.UserJson;

import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {

    public boolean isFriendDisplayed(UserJson userForTest, String expectedStatus) {
        navigateToFriendsPage();

        ElementsCollection tableRows = $$(".main-content__section");
        List<String> rowTexts = tableRows.texts();

        return rowTexts.stream()
                .map(text -> text.split("\n"))
                .anyMatch(values -> Arrays.asList(values).contains(userForTest.getUsernamePeek())
                        && Arrays.asList(values).contains(expectedStatus));
    }

    public boolean isSubmitInvitationButtonDisplayed(UserJson userForTest) {
        navigateToFriendsPage();

        SelenideElement userRow = $$("table tbody tr").find(text(userForTest.getUsernamePeek()));
        SelenideElement submitButton = userRow.$("[data-tooltip-content='Submit invitation']");

        return userRow.exists() && submitButton.exists();
    }

    private void navigateToFriendsPage() {
        $("a[href='/friends']").click();
    }
}
