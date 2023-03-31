package io.github.mboegers.dop.invoice;

/**
 * Berechne MwSt für verschiedene Arten von Kunden
 */
final class MwStRechner {
    private MwStRechner() {
    }

    /**
     * Verwende Plain OOP Mittel
     */
    final class PlainOOP {
        private PlainOOP() {
        }

        /**
         * ermöglicht einen einheitlichen Zugriff
         */
        public static double calculateMwSt(Kunde kunde, double wert) {
            if (kunde instanceof Privatkunde) {
                return calculateMwSt((Privatkunde) kunde, wert);
            } else if (kunde instanceof Businesskunde) {
                return calculateMwSt((Businesskunde) kunde, wert);
            } else {
                throw new IllegalArgumentException("Typ %s nicht implementiert".formatted(kunde));
            }
        }

        /**
         * immer 10% des Kaufwertes für Privatkunden
         */
        private static double calculateMwSt(Privatkunde p, double wert) {
            return wert * 0.1d;
        }

        /**
         * 0% wenn Vorsteuerabzugsberechtigt, sonst 10%
         */
        private static double calculateMwSt(Businesskunde b, double wert) {
            return b.isVorsteuerAbzugsberechtigt() ? 0d : wert * 0.1d;
        }
    }

    /**
     * Verwendung JEP 394: Pattern Matching for instanceof https://openjdk.org/jeps/394
     */
    final class InstanceOfPattern {
        private InstanceOfPattern() {
        }

        public static double calculateMwSt(Kunde kunde, double wert) {
            if (kunde instanceof Businesskunde b) { // abhängig von dem Vorsteuerabzug
                if (b.isVorsteuerAbzugsberechtigt()) return 0.0d;
                else return wert * 0.1d;
            } else if (kunde instanceof Privatkunde) { // immer 10% des Kaufwertes
                return wert * 0.1d;
            } else { // behandle fehlende Implementierung
                throw new IllegalArgumentException("Typ %s nicht implementiert".formatted(kunde));
            }
        }
    }

    /**
     * Verwendung von JEP 433: Pattern Matching for switch (Fourth Preview) https://openjdk.org/jeps/433
     */
    final class SwichExpression {
        private SwichExpression() {
        }

        public static double calculateMwSt(Kunde kunde, double wert) {
            return switch (kunde) {
                case Businesskunde b -> {
                    if (b.isVorsteuerAbzugsberechtigt()) yield 0.0d;
                    else yield wert * 0.1d;
                } // alternativ b.vorsteuerAbzug() ? 0.0d : wert * 0.1d;
                case Privatkunde p -> wert * 0.1d;
            };
        }
    }

    /**
     * Verwendung von JEP 433: Pattern Matching for switch (Fourth Preview) https://openjdk.org/jeps/433 und when-Clause
     */
    final class SwitchExpressionWhenClause {
        private SwitchExpressionWhenClause() {
        }

        public static double calculateMwSt(Kunde kunde, double wert) {
            return switch (kunde) {
                case Businesskunde b when b.isVorsteuerAbzugsberechtigt() -> 0.0d;
                case Businesskunde b -> wert * 0.1d;
                case Privatkunde p -> wert * 0.1d;
            };
        }

    }
}

