package io.github.mboegers.dop.invoice;

import static java.util.Objects.requireNonNull;

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
         * Java 7 kompatibel
         */
        public static double calculateMwSt(Kunde kunde, double wert) {
            requireNonNull(kunde);
            if (kunde instanceof Privatkunde) {
                return calculateMwSt((Privatkunde) kunde, wert);
            } else if (kunde instanceof Businesskunde) {
                return calculateMwSt((Businesskunde) kunde, wert);
            } else {
                throw new IllegalArgumentException(String.format("Typ %s nicht implementiert", kunde));
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
     * Java 16 Final
     */
    final class InstanceOfPattern {
        private InstanceOfPattern() {
        }

        public static double calculateMwSt(Kunde kunde, double wert) {
            requireNonNull(kunde);
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
     * Verwendung von JEP 441: Pattern Matching for switch https://openjdk.org/jeps/441
     * Java 21 Final
     */
    final class SwichExpression {
        private SwichExpression() {
        }

        public static double calculateMwSt(Kunde kunde, double wert) {
            requireNonNull(kunde);
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
     * Verwendung von JEP 441: Pattern Matching for switch https://openjdk.org/jeps/441 und when-Clause
     * Java 21 Final
     */
    final class SwitchExpressionWhenClause {
        private SwitchExpressionWhenClause() {
        }

        public static double calculateMwSt(Kunde kunde, double wert) {
            requireNonNull(kunde);
            return switch (kunde) {
                case Businesskunde b when b.isVorsteuerAbzugsberechtigt() -> 0.0d;
                case Businesskunde b -> wert * 0.1d;
                case Privatkunde p -> wert * 0.1d;
            };
        }

    }

    /**
     * Verwendung von JEP 440: Record Patterns https://openjdk.org/jeps/440 Dekonstruktion
     * Java 21 Final
     */
    final class SwitchExpressionWhenClauseDeconstruct {
        private SwitchExpressionWhenClauseDeconstruct() {
        }

        public static double calculateMwSt(Kunde kunde, double wert) {
            requireNonNull(kunde);
            return switch (kunde) {
                case Businesskunde(String name, String mail, boolean isVorsteuerAbzugsberechtigt)
                        when isVorsteuerAbzugsberechtigt -> 0.0d;
                case Businesskunde b -> wert * 0.1d;
                case Privatkunde p -> wert * 0.1d;
            };
        }

    }

    /**
     * Verwendung von JEP 440: Record Patterns https://openjdk.org/jeps/440 Dekonstruktion mit Inference
     * Java 21 Final
     */
    final class SwitchExpressionWhenClauseDeconstructVar {
        private SwitchExpressionWhenClauseDeconstructVar() {
        }

        public static double calculateMwSt(Kunde kunde, double wert) {
            requireNonNull(kunde);
            return switch (kunde) {
                case Businesskunde(var name, var mail, var isVorsteuerAbzugsberechtigt)
                        when isVorsteuerAbzugsberechtigt -> 0.0d;
                case Businesskunde b -> wert * 0.1d;
                case Privatkunde p -> wert * 0.1d;
            };
        }

    }

    /**
     * Verwendung von JEP 443: Unnamed Patterns and Variables (Preview) https://openjdk.org/jeps/443 Unnamed Variable Pattern
     * Java 21 Preview
     */
    /*
    final class SwitchExpressionWhenClauseUnnamed {
        private SwitchExpressionWhenClauseUnnamed() {
        }

        public static double calculateMwSt(Kunde kunde, double wert) {
            requireNonNull(kunde);
            return switch (kunde) {
                case Businesskunde(var _, var _, var isVorsteuerAbzugsberechtigt)
                        when isVorsteuerAbzugsberechtigt -> 0.0d;
                case Businesskunde _ -> wert * 0.1d;
                case Privatkunde _ -> wert * 0.1d;
            };
        }

    }
    */
}

