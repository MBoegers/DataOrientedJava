package io.github.mboegers.dop.invoice;

final class Dummy {
    /**
     * Simuliert einen Versand per Mail.
     */
    static void sendViaMail(String empf, String txt) {
        System.out.printf("Sende E-Mail an %s mit Inhalt:%n%s%n", empf, txt);
    }

    /**
     * Simuliert ein Abspeichern in einer Datenbank
     */
    static void storeInDB(String abt, double value) {
        System.out.printf("Speicher %s€ zur Verrechnung mit Abteilung %s%n", value, abt);
    }
}
