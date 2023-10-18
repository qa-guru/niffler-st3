package guru.qa.niffler.test.soap;

import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GeneratedUser;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.rest.BaseRestTest;
import guru.qa.niffler.userdata.wsdl.FriendsRequest;
import guru.qa.niffler.userdata.wsdl.FriendsResponse;
import guru.qa.niffler.ws.GatewayWebServiceClient;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static guru.qa.niffler.jupiter.annotation.GeneratedUser.Selector.OUTER;

public class UsersSoapTest extends BaseRestTest {


    private final GatewayWebServiceClient gatewayWebServiceClient = new GatewayWebServiceClient();

    @Test
    @AllureId("23424")
    @GenerateUser()
    void userShouldHaveEmptyFriendsListAfterRegistration(
            @GeneratedUser(selector = OUTER) UserJson generatedUser) throws IOException {
        FriendsRequest request = new FriendsRequest();
        request.setUsername(generatedUser.getUsername());


        final FriendsResponse response = gatewayWebServiceClient.friends(request);
        System.out.println("");
    }
}
