package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotations.User;
import guru.qa.niffler.jupiter.extention.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.annotations.User.UserType.WITH_FRIENDS;

public class FriendsWebTest extends BaseWebTest{
    // dima LEON - Received INVITE
    //alina igor - SEND INVITE
    //UNO nik -FRIENDS


    /**
     *
     * 1) Написать тесты (полностью логику тестов) для проверки списка друзей, отправленного и входящего инвайта, и
     * этими тестами доказать рабочий механизм очереди UsersQueueExtension
     * 2) Доработать UsersQueueExtension:
     * 2.1) предусмотреть, что параметр @User(userType = WITH_FRIENDS) UserJson userForTest может присутствовать только в методе @BeforeEach
     * и отсутствовать в тесте (частный случай). Подсказка: все методы в тестовом классе (в том числе и BeforeEach) можно
     * получить через context.getRequiredTestClass().getDeclaredMethods(), а далее фильтровать по isAnnotationPresent.
     * То есть допустимы следующие варианты:
     * - параметр @User есть и в beforeEach, и в тестовом методах;
     * - параметр @User есть только в beforeEach методе;
     * - параметр @User есть только в тестовом методе;
     * - параметра @User нет нигде.
     * 2.2) Предусмотреть возможность написания теста, использующего сразу двух пользователей разного типа. Например void test(@User(userType = WITH_FRIENDS) UserJson userForTest, @User(userType = INVITATION_SENT) UserJson userForTestAnother)
     */

    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("102")
    void friendsShouldBeDisplayedInTable(@User(userType = WITH_FRIENDS)  UserJson anotherUserForTest){
        mainPage.clickButton("friends");
        friendsPage.checkList();
        friendsPage.checkShouldBeFriends(anotherUserForTest.getUsername()+"1");

    }

}
