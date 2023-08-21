package io.github.mboegers.dop.invoice;

/**
 * lksdj
 */
public sealed interface Kunde permits Privatkunde, Businesskunde {
    /**
     * adad
     *
     * @return adad
     */
    String name();

    /**
     * asdad
     * @return asdad
     */
    String mail();
}

