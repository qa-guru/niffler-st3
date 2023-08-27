package guru.qa.niffler.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class FriendsPage {

    public FriendsPage shouldBeFriend(String nameFriend){
        $x("//td[text()='"+nameFriend+"']/following::td//div").shouldBe(text("You are friends"));
        return this;
    }
    public FriendsPage searchAndDeleteFriend (String nameFriend){
        $x("//td[text()='"+nameFriend+"']//following-sibling::td//button").click();
        return this;
    }


    public FriendsPage checkList(){
        step("Check list", () ->
                $$(".abstract-table tbody tr").
                        shouldBe(CollectionCondition.sizeGreaterThan(0)));

        return this;
    }


    public FriendsPage checkShouldBeFriends(String userName){
        step("Check friends", () ->
                $(byText(userName)).shouldBe(visible));
        return this;
    }
}
