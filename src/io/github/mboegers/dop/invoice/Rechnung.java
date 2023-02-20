package io.github.mboegers.dop.invoice;

sealed interface Rechnung permits InterneVerechnung, ExternVersandt {
}

record InterneVerechnung(String abteilung, double wert) implements Rechnung {
}

record ExternVersandt(Kunde kunde, double wert) implements Rechnung {
}

