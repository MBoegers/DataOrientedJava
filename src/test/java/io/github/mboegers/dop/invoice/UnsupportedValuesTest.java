package io.github.mboegers.dop.invoice;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Check whether all implementations inside the {@linkplain MwStRechner} behave the same for unsupported values
 *
 * @see CalculateMwStMethodProvider
 * @see MwStRechner
 */
class UnsupportedValuesTest {

    @ParameterizedTest
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void privateCustomerNull(BiFunction<Kunde, Double, Double> sut) {
        Privatkunde privateCustomer = null;
        var invoiceAmount = 100d;

        assertThatThrownBy(() -> sut.apply(privateCustomer, invoiceAmount))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void businessCustomerNull(BiFunction<Kunde, Double, Double> sut) {
        Businesskunde privateCustomer = null;
        var invoiceAmount = 100d;

        assertThatThrownBy(() -> sut.apply(privateCustomer, invoiceAmount))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void amountPrivateKundeNull(BiFunction<Kunde, Double, Double> sut) {
        var privateCustomer = new Privatkunde("test", "test@dummy.de");
        Double invoiceAmount = null;

        assertThatThrownBy(() -> sut.apply(privateCustomer, invoiceAmount))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void amountBusinessKundeNullWithVorsteuerabzug(BiFunction<Kunde, Double, Double> sut) {
        var privateCustomer = new Businesskunde("test", "test@dummy.de", true);
        Double invoiceAmount = null;

        assertThatThrownBy(() -> sut.apply(privateCustomer, invoiceAmount))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void amountBusinessKundeNullWithoutVorsteuerabzug(BiFunction<Kunde, Double, Double> sut) {
        var privateCustomer = new Businesskunde("test", "test@dummy.de", false);
        Double invoiceAmount = null;

        assertThatThrownBy(() -> sut.apply(privateCustomer, invoiceAmount))
                .isInstanceOf(NullPointerException.class);
    }
}
