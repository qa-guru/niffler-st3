package guru.qa.niffler.api;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("/register")
    Call<AuthUserEntity> addUser(@Body AuthUserEntity user);
}