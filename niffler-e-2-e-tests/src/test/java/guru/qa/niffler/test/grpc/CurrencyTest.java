package guru.qa.niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.*;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static guru.qa.grpc.niffler.grpc.CurrencyValues.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CurrencyTest extends BaseGrpcTest{
	private final int ONE_HUNDRED = 100;

	@Test
	void getAllCurrenciesTest(){
		final CurrencyResponse allCurrencies = currencyStub.getAllCurrencies(EMPTY);
		List<Currency> currencyList = allCurrencies.getCurrenciesList();
		assertEquals(RUB, currencyList.get(0).getCurrency());
		assertEquals(4, allCurrencies.getCurrenciesList().size());
	}

	@MethodSource("currencyCourse")
	@ParameterizedTest(name = "[{index}] Conversion from 100{0} to {1}")
	void calculateRateTest(CurrencyValues valuesIn, CurrencyValues valuesTo, double course){
		final CalculateRequest cr = CalculateRequest.newBuilder()
				.setAmount(ONE_HUNDRED)
				.setSpendCurrency(valuesIn)
				.setDesiredCurrency(valuesTo)
				.build();
		final CalculateResponse response = currencyStub.calculateRate(cr);
		assertEquals(course, response.getCalculatedAmount());
	}

	@MethodSource("currency")
	@ParameterizedTest(name = "[{index}] Conversion 100{0} to UNSPECIFIED")
	void calculateRateToUnspecifiedTest(CurrencyValues value){
		final CalculateRequest cr = CalculateRequest.newBuilder()
				.setAmount(ONE_HUNDRED)
				.setSpendCurrency(value)
				.setDesiredCurrency(UNSPECIFIED)
				.build();
		assertThrows(StatusRuntimeException.class, () -> currencyStub.calculateRate(cr));
	}

	@MethodSource("currency")
	@ParameterizedTest(name = "[{index}] Conversion from UNSPECIFIED to {0}")
	void calculateRateFromUnspecifiedTest(CurrencyValues value){
		final CalculateRequest cr = CalculateRequest.newBuilder()
				.setAmount(ONE_HUNDRED)
				.setSpendCurrency(UNSPECIFIED)
				.setDesiredCurrency(value)
				.build();
		assertThrows(StatusRuntimeException.class, () -> currencyStub.calculateRate(cr));
	}

	@MethodSource("currency")
	@ParameterizedTest(name = "[{index}] Conversion from {0} to {0}")
	void calculateRateBySameCurrencyTest(CurrencyValues value){
		final CalculateRequest cr = CalculateRequest.newBuilder()
				.setAmount(ONE_HUNDRED)
				.setSpendCurrency(value)
				.setDesiredCurrency(value)
				.build();
		final CalculateResponse response = currencyStub.calculateRate(cr);
		assertEquals(ONE_HUNDRED, response.getCalculatedAmount());
	}

	private static Stream<Arguments> currencyCourse() {
		return Stream.of(
				arguments(USD, RUB, 6666.67),
				arguments(USD, EUR, 92.59),
				arguments(USD, KZT, 47619.05),
				arguments(EUR, USD, 108.0),
				arguments(EUR, RUB, 7200.0),
				arguments(EUR, KZT, 51428.57),
				arguments(RUB, USD, 1.5),
				arguments(RUB, EUR, 1.39),
				arguments(RUB, KZT, 714.29),
				arguments(KZT, USD, 0.21),
				arguments(KZT, EUR, 0.19),
				arguments(KZT, RUB, 14.0)
		);
	}

	private static Stream<Arguments> currency() {
		return Stream.of(
				arguments(RUB),
				arguments(USD),
				arguments(EUR),
				arguments(KZT)
		);
	}
}
