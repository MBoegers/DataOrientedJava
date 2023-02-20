package io.github.mboegers.dop.invoice;

import java.util.List;

class Main {

    /**
     * Behandlet eine Rechnung abhing davon, ob sie intern oder extern ist.
     * Verwendet JEP 432: Record Patterns (Second Preview) https://openjdk.org/jeps/432
     */
    static void processRechnung(Rechnung rechnung) {
        switch (rechnung) {
            case InterneVerechnung(var abt, double wert) -> Dummy.storeInDB(abt, wert);
            case ExternVersandt(Kunde kunde, var wert) -> {
                double mwst = MwStRechner.SwitchExpressionWhenClause.calculateMwSt(kunde, wert);
                var txt = formatMail(kunde.name(), wert, mwst);
                Dummy.sendViaMail(kunde.mail(), txt);
            }
        }
    }

    /**
     * Formatiert eine Mail Text
     */
    static String formatMail(String name, double wert, double mwst) {
        return """
                    Hallo %s,
                    Bitte senden Sie uns den Rechnungsbetrag in Höhe von %.2f€ plus %.2f€ MwSt.

                    Mit freundlichen Grüßen
                    Merlin Bögershausen
                """.formatted(name, wert, mwst);
    }

    /**
     * Formtiert eine Mail mit JEP 430: String Templates (Preview) https://openjdk.org/jeps/430
     */
    /*
    static String formatMail_Template(String name, double wert, double mwst) {
        return FMT."""
        Hallo \{name},
        Bitte senden Sie uns den Rechnungsbetrag in Höhe von %.2f\{wert}€ plus %.2f\{mwst}€ MwSt.

        Mit freundlichen Grüßen
        Merlin Bögershausen
    """; // anstelle String.formatted(name, wert, mwst) mit Format
    }
     */
    public static void main(String[] args) {
        var pk = new Privatkunde("Merlin", "");
        var bk1 = new Businesskunde("adesso SE", "", false);
        var bk2 = new Businesskunde("Eureg JUG", "", true);

        System.out.println("Behandle Rechnung");
        var rechnungen = List.of(new InterneVerechnung("HR", 10), new ExternVersandt(bk1, 10), new ExternVersandt(bk2, 10), new ExternVersandt(pk, 10));

        rechnungen.forEach(Main::processRechnung);
    }
}
