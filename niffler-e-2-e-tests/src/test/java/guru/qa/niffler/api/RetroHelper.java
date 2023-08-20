package guru.qa.niffler.api;

import guru.qa.niffler.jupiter.category.CategoryExtension;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetroHelper {


  public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

  private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
  public static final Retrofit RETROFIT = new Retrofit.Builder().client(httpClient)
      .baseUrl("http://127.0.0.1:8093")
      .addConverterFactory(JacksonConverterFactory.create())
      .build();
}
