package guru.qa.niffler.page.components;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.PeoplePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class HeaderComponent {

    private final SelenideElement
            friendsButton = $x("//a[@href = '/friends']"),
            peopleButton = $x("//a[@href = '/people']");

    @Step("Открыть страницу \"Друзей\"")
    public FriendsPage goToFriendsPage() {
        friendsButton.click();
        return new FriendsPage();
    }

    @Step("Открыть страницу \"Все люди\"")
    public PeoplePage goToPeoplePage() {
        peopleButton.click();
        return new PeoplePage();
    }
}
