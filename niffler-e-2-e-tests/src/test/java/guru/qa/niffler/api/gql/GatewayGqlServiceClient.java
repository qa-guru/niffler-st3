package guru.qa.niffler.api.gql;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.RestService;
import guru.qa.niffler.api.context.SessionStorageContext;
import io.qameta.allure.Step;

import java.io.IOException;

public class GatewayGqlServiceClient extends RestService {
    public GatewayGqlServiceClient() {
        super(CFG.nifflerGatewayUrl());
    }

    private final GatewayGqlService gqlService = retrofit.create(GatewayGqlService.class);

    @Step("Send /graphql request")
    public JsonNode graphql(JsonNode body) throws IOException {
        return gqlService.graphql(
                        "Bearer " + SessionStorageContext.getInstance().getToken(),
                        body
                )
                .execute()
                .body();
    }
}
