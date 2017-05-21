package dzikizachod;


/**
 * Strategia kontrolująca gracza.
 * Zawiera całość implementacji, której konkretne strategie powinny używać do komunikowania się ze wszystkimi
 * elementami gry.
 *
 * Pozwala
 */
abstract public class Strategia implements IObserwator {
    /** Gracz kontrolowany przez tę strategię. */
    private Gracz marionetka;

    /** jest często używany, więc nie mnóżmy aż tak instancji */
    private StrategicznyWidokGracza mojWidok;

    /** Szeryf */
    private StrategicznyWidokGracza widokSzeryfa;


    @Override
    final public void patrzPoczatekGry(StrategicznyWidokGracza[] gracze, StrategicznyWidokGracza szeryf, int liczbaBandytów, int liczbaPomocników) {
        widokSzeryfa = szeryf;
    }


    public void akcjaUlecz(StrategicznyWidokGracza cel) throws BladKonrtoleraWyjatek {
        marionetka.akcjaUlecz(cel.gracz);
    }


    public void akcjaStrzel(StrategicznyWidokGracza cel) throws BladKonrtoleraWyjatek {
        marionetka.akcjaStrzel(cel.gracz);
    }


    public void akcjaZasiegPlusJeden() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        marionetka.akcjaZasiegPlusJeden();
    }


    public void akcjaZasiegPlusDwa() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        marionetka.akcjaZasiegPlusDwa();
    }


    public void akcjaDynamit() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        marionetka.akcjaDynamit();
    }


    final protected StrategicznyWidokGracza ja() {
        return mojWidok;
    }


    final protected StrategicznyWidokGracza szeryf() {
        return widokSzeryfa;
    }


    final void przypiszGracza(Gracz gracz) {
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
