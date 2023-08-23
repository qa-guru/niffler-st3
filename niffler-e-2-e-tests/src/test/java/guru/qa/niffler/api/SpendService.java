package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SpendService {                                                                                         // в папке api создаем интерфейс ретрофита, описываем как наши тесты будут ходить в наш SpendService
    @POST("/addSpend")
    Call<SpendJson> addSpend(@Body SpendJson spend);                                                                    // будет возвращать ретрофитовский тип Call<SpendJson - тип возвращаемого значения>
}