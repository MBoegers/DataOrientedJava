package io.github.mboegers.dop.invoice;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Check whether all implementations inside the {@linkplain MwStRechner} behave the same for combinations of
 * {@linkplain Privatkunde} and different invoice values
 *
 * @see CalculateMwStMethodProvider
 * @see MwStRechner
 * @see Privatkunde
 */
class PrivateKundeMwStTest {

    @ParameterizedTest(name = "Privatekunde pay 10.0 for invoices of 100.0")
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void invoiceValue100(BiFunction<Kunde, Double, Double> sut) {
        var privateCustomer = new Privatkunde("test", "test@dummy.de");
        var invoiceAmount = 100d;
        var expectedMwSt = 10d;

        var actualMwSt = sut.apply(privateCustomer, invoiceAmount);

        assertThat(actualMwSt).isEqualTo(expectedMwSt);
    }

    @ParameterizedTest(name = "Privatekunde pay 50.0 for invoices of 500.0")
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void invoiceValue500(BiFunction<Kunde, Double, Double> sut) {
        var privateCustomer = new Privatkunde("test", "test@dummy.de");
        var invoiceAmount = 500d;
        var expectedMwSt = 50d;

        var actualMwSt = sut.apply(privateCustomer, invoiceAmount);

        assertThat(actualMwSt).isEqualTo(expectedMwSt);
    }

    @ParameterizedTest(name = "Name of Privatekunde has no effect to MwSt")
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void customerNameChanged(BiFunction<Kunde, Double, Double> sut) {
        var testCustomer = new Privatkunde("Test", "Test@dummy.de");
        var privateCustomer = new Privatkunde("Other", "other@dummy.de");
        var invoiceAmount = 100d;

        var testKundeMwSt = sut.apply(testCustomer, invoiceAmount);
        var otherKundeMwSt = sut.apply(privateCustomer, invoiceAmount);

        assertThat(testKundeMwSt)
                .withFailMessage("Privatkunde with name 'Test' has to pay different MwSt as with name 'Other'")
                .isEqualTo(otherKundeMwSt);
    }
}
