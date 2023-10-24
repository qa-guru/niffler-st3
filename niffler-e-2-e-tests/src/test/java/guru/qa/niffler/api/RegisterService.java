package guru.qa.niffler.api;

import retrofit2.*;
import retrofit2.http.*;

public interface RegisterService {

    @POST("/register")
    @FormUrlEncoded
    Call<Void> register(
            @Header("Cookie") String cookie,
            @Field("_csrf") String csrf,
            @Field("username") String username,
            @Field("password") String password,
            @Field("passwordSubmit") String passwordSubmit);

}
