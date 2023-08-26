package guru.qa.niffler.api;

import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("/register")
    Call<UserEntity> addUser(@Body UserEntity user);
}