package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.SpendService;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Date;

public class SpendExtension implements BeforeEachCallback, ParameterResolver {                                          // BeforeEachCallback - будет выполнен перед методом BeforeEach // SpendExtension это имплементация экстеншена.
    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendExtension.class);       // У каждого экстеншена должен быть NAMESPACE
    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();                                  // Создаем httpClient
    private static final Retrofit retrofit = new Retrofit.Builder()                                                     // retrofit с помощью SpendService интерфейса будет отправлять запросы.
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    private SpendService spendService = retrofit.create(SpendService.class);                                            // Генерим реализацию rest клиента

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Spend annotation = context.getRequiredTestMethod().getAnnotation(Spend.class);                                  // Это аннотация которую мы достали через механизм рефлекшн

        if (annotation != null) {                                                                                       // Добавляем условие, чтоб не получить null pointer эксепшн
            SpendJson spend = new SpendJson();
            spend.setUsername(annotation.username());
            spend.setDescription(annotation.description());
            spend.setAmount(annotation.amount());
            spend.setCategory(annotation.category());
            spend.setSpendDate(new Date());
            spend.setCurrency(annotation.currency());
            SpendJson createdSpend = spendService.addSpend(spend).execute().body();
            context.getStore(NAMESPACE).put("spend", createdSpend);
        }
    }

    @Override
    public boolean supportsParameter(                                                                                   // вернет true если тип SpendJson, для любого другого типа он вернет false
                                    ParameterContext parameterContext,
                                    ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(SpendJson.class);                                                                     // Проверяем тип параметра SpendJson.class
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext,                                                // Поменяли Object на SpendJson
                                      ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(SpendExtension.NAMESPACE)
                .get("spend", SpendJson.class);                                                                         // тут как в хэшмапе (ключ, значение) - SpendJson.class, тип который мы от-туда достаем
    }
}
