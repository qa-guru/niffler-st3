package guru.qa.niffler.test.web;

import guru.qa.grpc.niffler.grpc.CalculateRequest;
import guru.qa.grpc.niffler.grpc.CalculateResponse;
import guru.qa.grpc.niffler.grpc.CurrencyValues;
import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.config.Config;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.AllureId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NifflerCurrencyGrpcTest {

    protected static final Config CFG = Config.getInstance();

    private static Channel channel = ManagedChannelBuilder.forAddress(
            CFG.nifflerCurrencyUrl(),
            CFG.nifflerCurrencyPort()
    ).build();

    private NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub stub = NifflerCurrencyServiceGrpc
            .newBlockingStub(channel);

    static Stream<Arguments> calculateRateTest() {
        return Stream.of(
                Arguments.of(CurrencyValues.USD, CurrencyValues.RUB, 100.0, 6666.67),
                Arguments.of(CurrencyValues.USD, CurrencyValues.USD, 100.0, 100.0),
                Arguments.of(CurrencyValues.RUB, CurrencyValues.USD, 100.0, 1.5)
        );
    }

    @AllureId("300002")
    @MethodSource
    @ParameterizedTest(name = "При пересчете из {0} в {1} суммы {2} должен возвращаться результат {3}")
    void calculateRateTest(CurrencyValues spendCurrency,
                           CurrencyValues desiredCurrency,
                           double amount,
                           double expected) {
        CalculateRequest request = CalculateRequest.newBuilder()
                .setAmount(amount)
                .setSpendCurrency(spendCurrency)
                .setDesiredCurrency(desiredCurrency)
                .build();

        final CalculateResponse calculateResponse = step("Calculate rate", () ->
                stub.calculateRate(request)
        );

        step("Check calculated rate", () ->
                assertEquals(expected, calculateResponse.getCalculatedAmount())
        );
    }
}
