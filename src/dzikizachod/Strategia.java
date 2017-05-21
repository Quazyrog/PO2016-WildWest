package dzikizachod;


import java.util.Random;

/**
 * Strategia kontrolująca gracza.
 * Zawiera całość implementacji, której konkretne strategie powinny używać do komunikowania się ze wszystkimi
 * elementami gry.
 *
 * Pozwala podklasom sterowac graczem oraz odpytywac go oraz innych graczy o ich stan.
 *
 * Jedna instancja może kontrolowac dokładnie jednego gracza.
 */
abstract public class Strategia implements IObserwator {
    protected Random rng = new Random();

    /** Gracz kontrolowany przez tę strategię. */
    private Gracz marionetka;

    /** jest często używany, więc nie mnóżmy aż tak instancji */
    private StrategicznyWidokGracza mojWidok;

    /** Szeryf */
    private StrategicznyWidokGracza widokSzeryfa;


    /**
     * Podczas losowań strategia powinna się posługiwac tym generatorem liczb losowych.
     * @param rng RGN do użycia przez strategię
     */
    public Strategia(Random rng) {
        this.rng = rng;
    }


    /**
     * Strategia stworzy sobie własny RNG.
     */
    public Strategia() {
        this(new Random());
    }


    @Override
    final public void patrzPoczatekGry(StrategicznyWidokGracza[] gracze, StrategicznyWidokGracza szeryf, int liczbaBandytów, int liczbaPomocników) {
        widokSzeryfa = szeryf;
    }


    /**
     * Delegacja metody z kontrolowanego gracza
     */
    public void akcjaUlecz(StrategicznyWidokGracza cel) throws BladKonrtoleraWyjatek {
        marionetka.akcjaUlecz(cel.gracz);
    }


    /**
     * Delegacja metody z kontrolowanego gracza
     */
    public void akcjaStrzel(StrategicznyWidokGracza cel) throws BladKonrtoleraWyjatek {
        marionetka.akcjaStrzel(cel.gracz);
    }


    /**
     * Delegacja metody z kontrolowanego gracza
     */
    public void akcjaZasiegPlusJeden() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        marionetka.akcjaZasiegPlusJeden();
    }


    /**
     * Delegacja metody z kontrolowanego gracza
     */
    public void akcjaZasiegPlusDwa() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        marionetka.akcjaZasiegPlusDwa();
    }


    /**
     * Delegacja metody z kontrolowanego gracza
     */
    public void akcjaDynamit() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        marionetka.akcjaDynamit();
    }


    /**
     * Zwraca widok na kontrolowanego gracza
     * @return widok na kontrolowanego gracza
     */
    final protected StrategicznyWidokGracza ja() {
        return mojWidok;
    }


    /**
     * Zwraca widok na szeryfa
     * @return widok na szeryfa
     */
    final protected StrategicznyWidokGracza szeryf() {
        return widokSzeryfa;
    }


    /**
     * Przypisuje strategii gracza, którego będzie kontrolować
     * @param gracz gracz do kontrolowania
     */
    final void przypiszGracza(Gracz gracz) {
        if (marionetka != null)
            throw new IllegalStateException("Tak strategia już kontroluje gracza");
        marionetka = gracz;
        mojWidok = new StrategicznyWidokGracza(gracz, gracz);
    }


    /**
     * W tej metodzie strategia kontroluje gracza podczas jego tury.
     * Kontrolująca strategia powinna sprawdzać, czy jest już koniec gry i należy zakończyć sterowanie graczem.
     * @throws BladKonrtoleraWyjatek jakby nie złapała, to gracz obsłuży
     */
    abstract void graj() throws BladKonrtoleraWyjatek;
}
