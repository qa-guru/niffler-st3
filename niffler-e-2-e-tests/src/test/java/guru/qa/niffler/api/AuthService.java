package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.*;

public interface AuthService {

	// init state
	// - empty cookies
	// - SS:
	// - codeChallenge HwyoXXnRlzuTECqR57K1GEUgM26H1L9sKJEpuFtcDEg
	// - codeVerifier KMHGGF35QRYVA14wpVNYgNnd_W7d2F3DuWphOu-JoU0
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
	// client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&
	// code_challenge=KMHGGF35QRYVA14wpVNYgNnd_W7d2F3DuWphOu-JoU0&
	// code_challenge_method=S256
	// result:302, Set-Cookie:JSESSIONID=7C9DC2762B4B4BB57AEDE5E3565D7FA5
	// location:http://127.0.0.1:9000/login (200), Set-Cookie:XSRF-TOKEN=4e8e68d0-cae3-4568-8154-557f138865a9

	@POST("/login")
	@FormUrlEncoded
	Call<Void> login(
//			@Header("Cookie") String jsessionIdCookie,
//			@Header("Cookie") String xsrfTokenIdCookie,
			@Field("username") String username,
			@Field("password") String password,
			@Field("_csrf") String csrf);


	// second request
	// POST http://127.0.0.1:9000/login (URL ENCODED)
	// _csrf: 4e8e68d0-cae3-4568-8154-557f138865a9
	// username: ivanov
	// password: 12345678
	// Req Cookie: JSESSIONID=7C9DC2762B4B4BB57AEDE5E3565D7FA5; XSRF-TOKEN=4e8e68d0-cae3-4568-8154-557f138865a9
	// result:302, Set-Cookie:JSESSIONID=832B86390C61E1CCD1BBD3EE899773FA; Set-Cookie:XSRF-TOKEN=
	// Location: http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=KMHGGF35QRYVA14wpVNYgNnd_W7d2F3DuWphOu-JoU0&code_challenge_method=S256&continue

	// 2.1 request (redirect)
	// GET http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=KMHGGF35QRYVA14wpVNYgNnd_W7d2F3DuWphOu-JoU0&code_challenge_method=S256&continue
	// Req Cookie:JSESSIONID=832B86390C61E1CCD1BBD3EE899773FA
	// Result: 302
	// Location: http://127.0.0.1:3000/authorized?code=0kPJiFvP9paUjgDwBwDU3Pjrt5tflpgJoeDI9S0frLiIIYxbEUfUgcDx_FMW3bDydchiO485cC5iuKpeUCfPPOePKIgcKjeHh5KJz4l93rckEpBbWA5OlNtCxAoQrEkv

	// 2.2 request (redirect)
	// GET http://127.0.0.1:3000/authorized?code=0kPJiFvP9paUjgDwBwDU3Pjrt5tflpgJoeDI9S0frLiIIYxbEUfUgcDx_FMW3bDydchiO485cC5iuKpeUCfPPOePKIgcKjeHh5KJz4l93rckEpBbWA5OlNtCxAoQrEkv
	// Req Cookie:JSESSIONID=832B86390C61E1CCD1BBD3EE899773FA
	// Result: 200
	// Location: http://127.0.0.1:3000/authorized?code=0kPJiFvP9paUjgDwBwDU3Pjrt5tflpgJoeDI9S0frLiIIYxbEUfUgcDx_FMW3bDydchiO485cC5iuKpeUCfPPOePKIgcKjeHh5KJz4l93rckEpBbWA5OlNtCxAoQrEkv


	@POST("oauth2/token")
	Call<JsonNode> token(
			@Header("Authorization") String basicAuthorization,
			@Query("client_id") String clientId,
			@Query(value = "redirect_uri", encoded = true) String redirectUri,
			@Query("grant_type") String grantType,
			@Query("code") String code,
			@Query("code_verifier") String codeVerifier);


	// Third request
	// POST http://127.0.0.1:9000/oauth2/token?client_id=client&redirect_uri=http://127.0.0.1:3000/authorized&grant_type=authorization_code&code=0kPJiFvP9paUjgDwBwDU3Pjrt5tflpgJoeDI9S0frLiIIYxbEUfUgcDx_FMW3bDydchiO485cC5iuKpeUCfPPOePKIgcKjeHh5KJz4l93rckEpBbWA5OlNtCxAoQrEkv&code_verifier=HwyoXXnRlzuTECqR57K1GEUgM26H1L9sKJEpuFtcDEg	//
	// Authorization:Basic Y2xpZW50OnNlY3JldA==
	// Result: 200
	// {
	//    "access_token": "eyJraWQiOiJhNmM5NDdiOS0wN2RmLTQ4OGYtOWU2NC1hZWY0NTA4YTkxNWEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJpdmFub3YiLCJhdWQiOiJjbGllbnQiLCJuYmYiOjE2OTUxNDQ0NTUsInNjb3BlIjpbIm9wZW5pZCJdLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjkwMDAiLCJleHAiOjE2OTUxODA0NTUsImlhdCI6MTY5NTE0NDQ1NX0.nlhUCKS0dE-NoBS7bXk6UO7CSp2k97GXAW09Gj7mAnEfeyozey1NIGh7pmg55Sq9zAbWSNsbEYNyVrHY3TFIsn-ijOOLCVsr97qEgze_ommeRxX1Gh4jsLWQ49y5-madh13qJiob2Cey0smQ3K27YYRmsB5XqJebqjeok-P9yb4VJzob5G1HacjSefifgQzV7hf_sE4lzMqdH4CLctdVUYDsJW5SgY6JXhFxDtgso8eWM34ruAPIAOd-4adGLHK5xBChnbGufE-2Y51T5seOYssoPJ6DPGD7gfSblsgZlX9ZfnS8gYrfvRyDs6ISkE4VPCyIjWYnqC-MK3xNZ8vlqw",
	//    "refresh_token": "4TwFrkDLmHJ3SiT3EdTK06wA1Z5xzXDczj4zrghzGUHX_HkX6_Ys8gzcwwU-kkn6pQaI5zMAW5-tVPkZbtshER2H8xhJr1O5QuI-VBjSrZuJccuUNH9p_lergZDZ5xhA",
	//    "scope": "openid",
	//    "id_token": "eyJraWQiOiJhNmM5NDdiOS0wN2RmLTQ4OGYtOWU2NC1hZWY0NTA4YTkxNWEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJpdmFub3YiLCJhdWQiOiJjbGllbnQiLCJhenAiOiJjbGllbnQiLCJhdXRoX3RpbWUiOjE2OTUxNDQ0NTUsImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6OTAwMCIsImV4cCI6MTY5NTE0NjI1NSwiaWF0IjoxNjk1MTQ0NDU1LCJzaWQiOiJHS2VhT3M4bVYwcnhJSFJLbllPOTFlX1NxNlpVa2NfT3FXVmFCVFo3NWs4In0.U-BieGkKApkRE8V_GY8_LvF0VCHFpObezqs1XIOc3cLk6nEVpB5U2jjmYom-jAV6a4Zc21zLvKGspdTaJqmjs_klxQTJtyrepGtjyXOEHjusTmQVvHinDSeewdn-V84-BPtlZLQdrtsBzu29bPwwhgV0OMq8-GxWWquYEeUlCEYpu_hinYeNtotIdDLUG8AqFIkp71HPylySchsanTo21THbkt3ePKckJOky071-8Oqyq5wcjwTCWgdaFINq22cxFcAUx1u6FEwSiYTrc7tU0XWz-qz57MPyAHDHiPRJHDy3utqXnApxfkYDKuzRwayGehlpd9mKfh3V3cYe1QqSVA",
	//    "token_type": "Bearer",
	//    "expires_in": 35999
	// }
	// SS: id_token (from json)
	//
	//



}
