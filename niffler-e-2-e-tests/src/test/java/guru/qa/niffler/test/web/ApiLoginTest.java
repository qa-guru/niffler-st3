package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.auth.UserEntity;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.DBUser;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ApiLoginTest extends BaseWebTest {

    @DBUser
    @ApiLogin
    @Test
    @AllureId("12")
    @DisplayName("Проверка пустого списка друзей")
    public void checkingEmptyFriendsList(UserEntity user) {
        Selenide.open(CFG.nifflerFrontUrl()+"/main");

        $("a[href='/friends']").shouldBe(visible, Duration.ofSeconds(8)).click();

        $(".main-content__section").shouldHave(text("There are no friends yet!"));
    }
}
