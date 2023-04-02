package io.github.mboegers.dop.invoice;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Check whether all implementations inside the {@linkplain MwStRechner} behave the same for combinations of
 * {@linkplain Businesskunde} and different invoice values
 *
 * @see CalculateMwStMethodProvider
 * @see MwStRechner
 * @see Businesskunde
 */
class BusinessKundeMwStTest {

    @ParameterizedTest(name = "Businesskunde with Vorsteuerabzugsberechtigung pay 0.0 MwSt for 100.0 invoices")
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void invoiceValue100WithVorsteuerabzug(BiFunction<Kunde, Double, Double> sut) {
        var businessCustomer = new Businesskunde("test", "test@dummy.de", true);
        var invoiceAmount = 100d;
        var expectedMwSt = 0d;

        var actualMwSt = sut.apply(businessCustomer, invoiceAmount);

        assertThat(actualMwSt).isEqualTo(expectedMwSt);
    }

    @ParameterizedTest(name = "Businesskunde with Vorsteuerabzugsberechtigung pay 0.0 MwSt for 500.0 invoices")
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void invoiceValue500WithVorsteuerabzug(BiFunction<Kunde, Double, Double> sut) {
        var businessCustomer = new Businesskunde("test", "test@dummy.de", true);
        var invoiceAmount = 500d;
        var expectedMwSt = 0d;

        var actualMwSt = sut.apply(businessCustomer, invoiceAmount);

        assertThat(actualMwSt).isEqualTo(expectedMwSt);
    }

    @ParameterizedTest(name = "Name has no effect to MwSt for Businesskunde with Vorsteuerabzugsberechtigung")
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void customerNameChangedWithVorsteuerabzug(BiFunction<Kunde, Double, Double> sut) {
        var testCustomer = new Businesskunde("Test", "other@dummy.de", true);
        var otherCustomer = new Businesskunde("Other", "Test@dummy.de", true);
        var invoiceAmount = 100d;

        var testCustomerMwSt = sut.apply(testCustomer, invoiceAmount);
        var otherCustomerMwSt = sut.apply(otherCustomer, invoiceAmount);

        assertThat(testCustomerMwSt)
                .withFailMessage("Businesskunde with name 'Test' has to pay different MwSt as with name 'Other'")
                .isEqualTo(otherCustomerMwSt);
    }

    @ParameterizedTest(name = "Businesskunde with Vorsteuerabzugsberechtigung pay 10.0 MwSt for 100.0 invoices")
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void invoiceValue100WithoutVorsteuerabzug(BiFunction<Kunde, Double, Double> sut) {
        var businessCustomer = new Businesskunde("test", "test@dummy.de", false);
        var invoiceAmount = 100d;
        var expectedMwSt = 10d;

        var actualMwSt = sut.apply(businessCustomer, invoiceAmount);

        assertThat(actualMwSt).isEqualTo(expectedMwSt);
    }

    @ParameterizedTest(name = "Businesskunde without Vorsteuerabzugsberechtigung pay 50.0 MwSt for 500.0 invoices")
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void invoiceValue500WithoutVorsteuerabzug(BiFunction<Kunde, Double, Double> sut) {
        var businessCustomer = new Businesskunde("test", "test@dummy.de", false);
        var invoiceAmount = 500d;
        var expectedMwSt = 50d;

        var actualMwSt = sut.apply(businessCustomer, invoiceAmount);

        assertThat(actualMwSt).isEqualTo(expectedMwSt);
    }

    @ParameterizedTest(name = "Name has no effect to MwSt for Businesskunde without Vorsteuerabzugsberechtigung")
    @ArgumentsSource(CalculateMwStMethodProvider.class)
    void customerNameChangedWithoutVorsteuerabzug(BiFunction<Kunde, Double, Double> sut) {
        var testCustomer = new Businesskunde("Test", "other@dummy.de", false);
        var otherCustomer = new Businesskunde("Other", "Test@dummy.de", false);
        var invoiceAmount = 100d;

        var testCustomerMwSt = sut.apply(testCustomer, invoiceAmount);
        var otherCustomerMwSt = sut.apply(otherCustomer, invoiceAmount);

        assertThat(testCustomerMwSt)
                .withFailMessage("Businesskunde with name 'Test' has to pay different MwSt as with name 'Other'")
                .isEqualTo(otherCustomerMwSt);
    }
}
