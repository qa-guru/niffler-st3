package guru.qa.niffler.ws;

import guru.qa.niffler.userdata.wsdl.FriendsRequest;
import guru.qa.niffler.userdata.wsdl.FriendsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GatewayWebService {
    @POST("/ws")
    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8"
    })
    Call<FriendsResponse> friends(@Body FriendsRequest request);
}