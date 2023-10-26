package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {

    // init state
    // - empty Cookies
    // - SS:
    // - codeChallenge	qPet2YMtOg9kueZraRTOsJiXNXBXuRsYDk8FS8UIK6k
    // - codeVerifier	3M6biEplhBEu1mLnlBB2edi2GkBKp1fDBbCWpo8RVco
    // - empty LS

    @GET("/oauth2/authorize")
    Call<Void> authorize(
            @Query("response_type") String responseType,
            @Query("client_id") String clientId,
            @Query("scope") String scope,
            @Query(value = "redirect_uri", encoded = true) String redirectUri,
            @Query("code_challenge") String codeChallenge,
            @Query("code_challenge_method") String codeChallengeMethod);

    // first request
    // GET http://127.0.0.1:9000/oauth2/authorize?
    // response_type=code&
    // client_id=client&
    // scope=openid&
    // redirect_uri=http://127.0.0.1:3000/authorized&
    // code_challenge=qPet2YMtOg9kueZraRTOsJiXNXBXuRsYDk8FS8UIK6k&
    // code_challenge_method=S256
    // RESULT:302, Set-Cookie: JSESSIONID=30D77030E679EBB8EEBC08DA18D5E5E5
    // location: http://127.0.0.1:9000/login (200 OK), Set-Cookie: XSRF-TOKEN=256c7ce8-2216-489d-91a5-c5fbce869537

    @POST("/login")
    @FormUrlEncoded
    Call<Void> login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("_csrf") String csrf
    );


    // SECOND REQUEST
    // POST http://127.0.0.1:9000/login (URL ENCODED)
    // _csrf: 256c7ce8-2216-489d-91a5-c5fbce869537
    // username: dima
    // password: 12345
    // REQ COOKIES: Cookie: JSESSIONID=30D77030E679EBB8EEBC08DA18D5E5E5; XSRF-TOKEN=256c7ce8-2216-489d-91a5-c5fbce869537
    // RESULT:302, Set-Cookie: JSESSIONID=94659DD5D0B9A29C6927CBC12C58088A; Set-Cookie: XSRF-TOKEN=
    // Location:
    //http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=qPet2YMtOg9kueZraRTOsJiXNXBXuRsYDk8FS8UIK6k&code_challenge_method=S256&continue

    // 2.1 REQUEST (REDIRECT)
    // GET http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=qPet2YMtOg9kueZraRTOsJiXNXBXuRsYDk8FS8UIK6k&code_challenge_method=S256&continue
    // REQ COOKIES: Cookie: JSESSIONID=94659DD5D0B9A29C6927CBC12C58088A
    // RESULT: 302
    // Location:
    // http://127.0.0.1:3000/authorized?code=g9SfSvOnl8Ob1o_GnkH5-0ZAuKaqiHBq8MmNAqFlT-RzSLKTLUO771DePHC5SLuddooWiJyhJ5zf4_U2uD3NseMx8yAtjAEf_o6vjto2ew8JS7mJyrK-K7VK6NhWmFL2

    // 2.2 REQUEST (REDIRECT)
    // GET http://127.0.0.1:3000/authorized?code=g9SfSvOnl8Ob1o_GnkH5-0ZAuKaqiHBq8MmNAqFlT-RzSLKTLUO771DePHC5SLuddooWiJyhJ5zf4_U2uD3NseMx8yAtjAEf_o6vjto2ew8JS7mJyrK-K7VK6NhWmFL2
    // REQ COOKIES: Cookie: JSESSIONID=94659DD5D0B9A29C6927CBC12C58088A
    // RESULT: 200


    @POST("oauth2/token")
    Call<JsonNode> token(
            @Header("Authorization") String basicAuthorization,
            @Query("client_id") String clientId,
            @Query(value = "redirect_uri", encoded = true) String redirectUri,
            @Query("grant_type") String grantType,
            @Query("code") String code,
            @Query("code_verifier") String codeVerifier
    );

    // THIRD REQUEST
    // POST http://127.0.0.1:9000/oauth2/token?client_id=client&redirect_uri=http://127.0.0.1:3000/authorized&grant_type=authorization_code&code=g9SfSvOnl8Ob1o_GnkH5-0ZAuKaqiHBq8MmNAqFlT-RzSLKTLUO771DePHC5SLuddooWiJyhJ5zf4_U2uD3NseMx8yAtjAEf_o6vjto2ew8JS7mJyrK-K7VK6NhWmFL2&code_verifier=3M6biEplhBEu1mLnlBB2edi2GkBKp1fDBbCWpo8RVco
    // Authorization: Basic Y2xpZW50OnNlY3JldA==
    // RESULT 200
    // {
    //    "access_token": "eyJraWQiOiIzNjIxZjU0NC1lYjgwLTRhYmEtYjI5Yi1mYmQ0NTA2M2E5YjAiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJkaW1hIiwiYXVkIjoiY2xpZW50IiwibmJmIjoxNjk1MDU4Njk0LCJzY29wZSI6WyJvcGVuaWQiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo5MDAwIiwiZXhwIjoxNjk1MDk0Njk0LCJpYXQiOjE2OTUwNTg2OTR9.upsor7ubhfYd4Qffffr_lNnSFXZ05wrf9xjK2uY1yX6if6V26qB5TbXEtoRRtuH45HHO491DD20FhRCdWE0V_tyk8lL67q9Tp2ygZejjWHdLHk1nqGzNdX7WIRQqvlLAyco0mocpDCDdAAoMigx4Irg4r6DSIHKshWuHeoSWR1EzVOJnT3-8QyAukxMS6ZicBi-Uh_ukJFWLZQG4vMibjXeGRSY1Et3HpIn1bEdUIvBpiUiZtV_xpxzJrrwE1Y48C_SafS2SWDNmVvtDfhvDNS9SU4EBnQX0153R2_VMAn_tAWLT2MOYsU1QHbZ7fNT77_M1-cWF6d79fgQv0CzaSQ",
    //    "refresh_token": "l6KmjFNYwEe5dzJ5GraoTPa6G-8eXoOS1TXh1zbA1EwOaq3a5MkQg-C3H_kDvRdzCKb3ZY6RMFYrmvbWDukGX68vQxlNoZtfd04bEvyLY1vdcl7CsjesntFBlf4aigBL",
    //    "scope": "openid",
    //    "id_token": "eyJraWQiOiIzNjIxZjU0NC1lYjgwLTRhYmEtYjI5Yi1mYmQ0NTA2M2E5YjAiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJkaW1hIiwiYXVkIjoiY2xpZW50IiwiYXpwIjoiY2xpZW50IiwiYXV0aF90aW1lIjoxNjk1MDU4Njk0LCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjkwMDAiLCJleHAiOjE2OTUwNjA0OTQsImlhdCI6MTY5NTA1ODY5NCwic2lkIjoicEoyMTczZ2JyWmwtajkxMEc0dXZsQ2VEMUNBYTN4NkRKeVVVYW9PTE4zdyJ9.Jom6EjX2P3pIp1PjO7Xn_X2yJQGxgPsYppYlUKz_T69dndQPt6ey0VdmsstvI3Q5AT1ZJAA6aJgFq4KqlFqWOBoMioNtMOgLR0_Fn-EOOz-10P3vbgSOVBflKUJLDDPmfJJab7Ddgp-z-DxyrX_wEh19Bx-smkobCgsFVNMwHcvwIEW4M6g7XEEdgBuLpoiAAXL2LUbgoVWx0Vlai59JXaRzl3KQ_zZp4bbIj98uRjntDF7g9tTFP0SHgctLYK1R3dOSknF5_sG3prLkOgWlMuz5ZDhagT3yenCh3oBhnMr8Q6u-bORdPV3zU5CtkbkhkchSqVrwzIryONbYh1B5oA",
    //    "token_type": "Bearer",
    //    "expires_in": 35999
    //}
    // SS: id_token (from json)


    @GET("/register")
    Call<Void> requestRegisterForm();

    @POST("/register")
    @FormUrlEncoded
    Call<Void> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("passwordSubmit") String passwordSubmit,
            @Field("_csrf") String csrf
    );
}
