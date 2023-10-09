package io.github.mboegers.dop.invoice;

sealed interface Kunde permits Privatkunde, Businesskunde {
}

record Privatkunde(String name, String mail) implements Kunde {
}

record Businesskunde(String name, String mail, boolean isVorsteuerAbzugsberechtigt) implements Kunde {
}
