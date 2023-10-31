package guru.qa.niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static guru.qa.grpc.niffler.grpc.CurrencyValues.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class NifflerCurrencyGrpcTest extends BaseGrpcTest {

    @Test
    void getAllCurrenciesTest() {
        CurrencyResponse allCurrencies = currencyStub.getAllCurrencies(EMPTY);
        final List<Currency> currencies = allCurrencies.getCurrenciesList();
        assertAll(
                () -> assertEquals(4, currencies.size()),
                () -> assertEquals(RUB, currencies.get(0).getCurrency()),
                () -> assertEquals(0.015, currencies.get(0).getCurrencyRate()),
                () -> assertEquals(KZT, currencies.get(1).getCurrency()),
                () -> assertEquals(0.0021, currencies.get(1).getCurrencyRate()),
                () -> assertEquals(EUR, currencies.get(2).getCurrency()),
                () -> assertEquals(1.08, currencies.get(2).getCurrencyRate()),
                () -> assertEquals(USD, currencies.get(3).getCurrency()),
                () -> assertEquals(1.0, currencies.get(3).getCurrencyRate())
        );
    }

    static Stream<Arguments> calculateRateTest() {
        return Stream.of(
                Arguments.of(KZT, EUR, 100.0, 0.19),
                Arguments.of(USD, USD, 0.0, 0.0),
                Arguments.of(RUB, USD, 1000.0, 15.0),
                Arguments.of(EUR, RUB, 100.0, 7200.0),
                Arguments.of(RUB, RUB, 0.0, 0.0),
                Arguments.of(KZT, RUB, 1000.0, 140.0),
                Arguments.of(EUR, USD, -100.0, -108.0),
                Arguments.of(EUR, KZT, 1000.0, 514285.71),
                Arguments.of(USD, KZT, 100.0, 47619.05),
                Arguments.of(USD, EUR, 1000.0, 925.93),
                Arguments.of(RUB, EUR, 100.0, 1.39),
                Arguments.of(USD, RUB, -100.0, -6666.67),
                Arguments.of(EUR, EUR, 0.0, 0.0),
                Arguments.of(KZT, KZT, 0.0, 0.0),
                Arguments.of(KZT, USD, 100.0, 0.21),
                Arguments.of(RUB, KZT, -100.0, -714.29),
                Arguments.of(KZT, EUR, -100.0, -0.19)
        );
    }

    @MethodSource
    @ParameterizedTest
    void calculateRateTest(CurrencyValues spendCurrency,
                           CurrencyValues desiredCurrency,
                           double amount,
                           double expectedAmount
    ) {
        final CalculateRequest cr = CalculateRequest.newBuilder()
                .setSpendCurrency(spendCurrency)
                .setDesiredCurrency(desiredCurrency)
                .setAmount(amount)
                .build();
        CalculateResponse response = currencyStub.calculateRate(cr);
        assertEquals(expectedAmount, response.getCalculatedAmount());
    }
}
