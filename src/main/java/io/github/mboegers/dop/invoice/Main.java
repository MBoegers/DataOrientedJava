package io.github.mboegers.dop.invoice;

import java.util.List;

import static io.github.mboegers.dop.invoice.Dummy.sendViaMail;
import static io.github.mboegers.dop.invoice.Dummy.storeInDB;
import static java.util.FormatProcessor.FMT;

class Main {

    /**
     * Behandle eine Rechnung abhing davon, ob sie intern oder extern ist.
     * Verwendet
     * JEP 440: Record Patterns https://openjdk.org/jeps/440
     * JEP 443: Unnamed Patterns and Variables (Preview) https://openjdk.org/jeps/443
     * JEP 430: String Templates https://openjdk.org/jeps/430
     */
    static void sendInvoiceFor(Rechnung rechnung) {
        switch (rechnung) {
            case InterneVerechnung(var abt, double wert) -> storeInDB(abt, wert);
            case ExternVersandt(var kunde, var wert) -> {
                double mwst = MwStRechner.SwitchExpressionWhenClauseUnnamed.calculateMwSt(kunde, wert);

                switch (kunde) {
                    case Privatkunde(String name, String address) ->
                            sendViaMail(address, formatInvoiceText(name, wert, mwst));
                    case Businesskunde(var name, var address, _) ->
                            sendViaMail(address, formatInvoiceText(name, wert, mwst));
                }
                ;
            }
        }
    }

    private static String formatInvoiceText(String name, double wert, double mwst) {
        var txt = FMT. """
            Hallo \{ name },
            Bitte senden Sie uns den Rechnungsbetrag in Höhe von %.2f\{ wert }€ plus %.2f\{ mwst }€ MwSt.

            Mit freundlichen Grüßen
            Merlin Bögershausen
        """ ;
        return txt;
    }

    public static void main(String[] args) {
        System.out.println("Behandle Rechnung");
        var rechnungen = List.of(
                new InterneVerechnung(
                        "HR",
                        10),
                new ExternVersandt(
                        new Businesskunde("adesso SE", "", false),
                        10),
                new ExternVersandt(
                        new Businesskunde("Eureg JUG", "", true),
                        10),
                new ExternVersandt(
                        new Privatkunde("Merlin", ""),
                        10));

        rechnungen.forEach(Main::sendInvoiceFor);
    }
}
