package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.auth.UserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.jupiter.extension.DbUserExtension.defaultPassword;

public class ParallelTest extends BaseWebTest {
    Faker faker = new Faker();

    @DBUser
    @Test
    @AllureId("1")
    @DisplayName("Проверка успешной авторизации пользователя")
    public void checkingSuccessfulUserAuthorization(UserEntity user) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();

        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(defaultPassword);
        $("button[type='submit']").click();

        $(".main-content__section-add-spending").should(visible,Duration.ofSeconds(8));
        $(".main-content__section-history").should(visible);
    }

    @DBUser
    @Test
    @AllureId("2")
    @DisplayName("Проверка неудачной авторизации пользователя")
    public void checkingForFailedUserAuthorization(UserEntity user) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();

        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(faker.internet().password());
        $("button[type='submit']").click();

        $(".form__error").shouldHave(text("Неверные учетные данные пользователя"));
    }

    @Test
    @AllureId("3")
    @DisplayName("Проверка успешной регистрации пользователя")
    public void checkingSuccessfulUserRegistration() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href='http://127.0.0.1:9000/register']").click();

        $("input[name='username']").setValue(faker.name().username());
        $("input[name='password']").setValue(defaultPassword);
        $("input[name='passwordSubmit']").setValue(defaultPassword);
        $("button[type='submit']").click();

        $(".form__paragraph").shouldHave(text("Congratulations! You've registered!"));
    }

    @Test
    @AllureId("4")
    @DisplayName("Проверка неудачной регистрации пользователя")
    public void checkingForFailedUserRegistration() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href='http://127.0.0.1:9000/register']").click();

        $("input[name='username']").setValue(faker.name().username());
        $("input[name='password']").setValue(defaultPassword);
        $("input[name='passwordSubmit']").setValue(faker.internet().password());
        $("button[type='submit']").click();

        $(".form__error").shouldHave(text("Passwords should be equal"));
    }

    @DBUser
    @Test
    @AllureId("5")
    @DisplayName("Проверка пустого списка друзей")
    public void checkingEmptyFriendsList(UserEntity user) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();

        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(defaultPassword);
        $("button[type='submit']").click();

        $("a[href='/friends']").shouldBe(visible, Duration.ofSeconds(8)).click();

        $(".main-content__section").shouldHave(text("There are no friends yet!"));
    }

    @DBUser
    @Test
    @AllureId("6")
    @DisplayName("Проверка наличие других пользователей, в разделе All people")
    public void checkingForOtherUsers(UserEntity user) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();

        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(defaultPassword);
        $("button[type='submit']").shouldBe(visible, Duration.ofSeconds(5)).click();

        $("a[href='/people']").shouldBe(visible, Duration.ofSeconds(5)).click();

        $x("//table//tbody//tr")
                .shouldBe(not(empty));
    }

    @DBUser
    @Test
    @AllureId("7")
    @DisplayName("Проверка добавления пустой категории в профиле юзера")
    public void checkingAdditionEmptyCategoryInUserProfile(UserEntity user) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();

        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(defaultPassword);
        $("button[type='submit']").shouldBe(visible, Duration.ofSeconds(5)).click();

        $("a[href='/profile']").shouldBe(visible, Duration.ofSeconds(5)).click();

        $x("//button[text()='Create']").click();
        $x("//div[@class='Toastify__toast-body']").shouldHave(text("New category added!"));
    }

    @DBUser
    @Test
    @AllureId("8")
    @DisplayName("Проверка отображения USERNAME в профиле юзера")
    public void checkingShouldBeVisibleUsernameInUserProfile(UserEntity user) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();

        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(defaultPassword);
        $("button[type='submit']").click();

        $("a[href='/profile']").shouldBe(visible, Duration.ofSeconds(5)).click();

        $x("//figure[@class='avatar-container']").shouldHave(text(user.getUsername()));
    }
}
