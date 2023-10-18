package guru.qa.niffler.ws;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.userdata.wsdl.FriendsRequest;
import guru.qa.niffler.userdata.wsdl.FriendsResponse;

import java.io.IOException;

public class GatewayWebServiceClient extends SoapService {

    public static final Config CFG = Config.getInstance();

    public GatewayWebServiceClient() {
        super(CFG.nifflerUserdataUrl());
    }

    private final GatewayWebService gatewayWebService = retrofit.create(GatewayWebService.class);

    public FriendsResponse friends(FriendsRequest request) throws IOException {
        return gatewayWebService.friends(request)
                .execute()
                .body();
    }
}
