package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class RestService {

    protected static final Config config = Config.getInstance();
    protected static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(new AllureOkHttp3())
            .build();

    protected final Retrofit retrofit;

    public RestService(String baseUrl) {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }


}
