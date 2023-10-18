package guru.qa.niffler.test.gql;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.gql.GatewayGqlServiceClient;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GeneratedUser;
import guru.qa.niffler.jupiter.annotation.GqlRequest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.rest.BaseRestTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static guru.qa.niffler.jupiter.annotation.GeneratedUser.Selector.OUTER;

public class UsersGraphQlTest extends BaseRestTest {

    private final GatewayGqlServiceClient graphQlClient = new GatewayGqlServiceClient();

    @Test
    @AllureId("23424")
    @GenerateUser(friends = )
    void userShouldHaveEmptyFriendsListAfterRegistration(
            @GqlRequest("gql/getFriendsQuery.json") JsonNode request,
            @GeneratedUser(selector = OUTER) UserJson generatedUser) throws IOException {
       doLogin(generatedUser.getUsername(), generatedUser.getPassword());
       final JsonNode response = graphQlClient.graphql(request);
       System.out.println("");
    }
}
