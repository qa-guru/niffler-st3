package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Step;

import java.io.IOException;

public class FriendsServiceClient extends RestService{

    private final FriendsService friendsService = retrofit.create(FriendsService.class);

    public FriendsServiceClient() {
        super(CFG.nifflerUserDataUrl());
    }

    @Step("Added friend")
    public void addFriend(String token, UserJson userJson) throws IOException {
        friendsService.addFriend("Bearer " + token, userJson).execute();
    }
    @Step("Accept invitation")
    public void acceptInvitation(String token, UserJson userJson) throws IOException {
        friendsService.acceptInvitation("Bearer " + token, userJson).execute();
    }
}
