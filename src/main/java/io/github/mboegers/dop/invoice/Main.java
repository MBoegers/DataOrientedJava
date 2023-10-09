package io.github.mboegers.dop.invoice;

import java.util.List;

import static io.github.mboegers.dop.invoice.Dummy.sendViaMail;
import static io.github.mboegers.dop.invoice.Dummy.storeInDB;
import static java.util.FormatProcessor.FMT;

class Main {

    //region One method style

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
            case ExternVersandt(Privatkunde(var name, var address), var wert) -> {
                var mwst = wert * 0.1;
                var text = formatInvoiceText(name, wert, mwst);
                sendViaMail(address, text);
            }
            case ExternVersandt(Businesskunde(var name, var address, var isAbzugBerechtigt), double wert)
                    when isAbzugBerechtigt -> sendViaMail(address, formatInvoiceText(name, wert, 0d));
            case ExternVersandt(Businesskunde(var name, var address, _), double wert) ->
                    sendViaMail(address, formatInvoiceText(name, wert, wert * 0.1));
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

    // endregion

    //region Delegate Style
    static void sendInvoiceFor2(Rechnung rechnung) {
        switch (rechnung) {
            case InterneVerechnung(var abt, double wert) -> storeInDB(abt, wert);
            case ExternVersandt(Kunde kunde, var wert) -> {
                var mwst = MwStRechner.SwitchExpressionWhenClauseUnnamed.calculateMwSt(kunde, wert);

                var text = produceInvoiceText(kunde, wert, mwst);

                switch (kunde) {
                    case Privatkunde(_, var address) -> sendViaMail(address, text);
                    case Businesskunde(_, var address, _) -> sendViaMail(address, text);
                }
            }
        }
    }

    private static String produceInvoiceText(Kunde kunde, double wert, double mwst) {
        var kundeName = switch (kunde) {
            case Privatkunde(String name, var _) -> name;
            case Businesskunde(var name, _, _) -> name;
        };

        return FMT. """
            Hallo \{ kundeName },
            Bitte senden Sie uns den Rechnungsbetrag in Höhe von %.2f\{ wert }€ plus %.2f\{ mwst }€ MwSt.

            Mit freundlichen Grüßen
            Merlin Bögershausen
        """ ;
    }
    //endregion

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
