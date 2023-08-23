package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.jupiter.DBUser;
import guru.qa.niffler.jupiter.Dao;
import guru.qa.niffler.jupiter.DaoExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ExtendWith(DaoExtension.class)
public class UserCreateTest extends BaseWebTest {

    private final static String user = "kirill_super1";
    private final static String password = "12345";

    @Dao
    private AuthUserDAO authUserDAO;
    @Dao
    private UserDataUserDAO userDataUserDAO;

    @BeforeEach
    void createUser(UserEntity currentUser) {
        System.out.println(currentUser.getUsername() + " before each");
    }

    @DBUser(username = user,
            password = password)

    @Test
    void mainPageShouldBeVisibleAfterLogin(UserEntity currentUser) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(currentUser.getUsername());
        $("input[name='password']").setValue(currentUser.getPassword());
        $("button[type='submit']").click();
        $(".main-content__section-stats").shouldBe(visible);
    }

    @AfterEach
    void deleteUser(UserEntity currentUser) {
        System.out.println(currentUser.getUsername() + " after each");
        userDataUserDAO.deleteUserByIdInUserData(currentUser.getId());
        authUserDAO.deleteUserById(currentUser.getId());
    }
}
