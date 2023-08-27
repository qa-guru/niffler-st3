package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotations.WebTest;
import guru.qa.niffler.pages.FriendsPage;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.PeoplePage;

@WebTest
public class BaseWebTest {

    MainPage mainPage = new MainPage();
    FriendsPage friendsPage = new FriendsPage();
    PeoplePage peoplePage = new PeoplePage();

}
