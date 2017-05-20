package dzikizachod;

/**
 * Wyjątek rzucany, kiedy strategia kontrolująca gracza zrobi coś, czego nie powinna.
 * W najgorszym razie zostanie przechwycony w taki sposób, że tylko zakończy turę gracza (i nic nie wybuchnie).
 */
class BladKonrtoleraWyjatek extends Exception {
    BladKonrtoleraWyjatek(String wiadomosc) {
        super(wiadomosc);
    }
}

/**
 * Wyjątek rzucany, kiedy gracz jest w zbyt dużej odległości, by wykonac akcję na celu.
 */
class PozaZasiegiemWyjatek extends BladKonrtoleraWyjatek {
    PozaZasiegiemWyjatek() {
        super("Gracz jest zbyt dalego, aby wykonac akcję");
    }
}

/**
 * Wyjątek rzucany, kiedy gracz nie posiada na ręce akcji, którą chce wykonać.
 */
class BrakAkcjiWyjatek extends BladKonrtoleraWyjatek {
    BrakAkcjiWyjatek() {
        super("Gracz nie posiada tej akcji");
    }
}

/**
 * Rzucany, kiedy gracz próbuje wykonac akcję w nieswoim ruchu.
 */
class NieTwojRochWyjatek extends BladKonrtoleraWyjatek {
    NieTwojRochWyjatek() {
        super("Gracz nie może teraz wykonać akcji, bo to nie jego ruch");
    }
}

/**
 * Rzucany, gdy strategia spróbujezrobić coś, czego nie może.
 * Na przykład kiedy odpyta widok gracza o liczbe akcji.
 */
class NieInteresujSieTrupemWyjatek extends BladKonrtoleraWyjatek {
    NieInteresujSieTrupemWyjatek() {
        super("Strzał do trupa");
    }
}