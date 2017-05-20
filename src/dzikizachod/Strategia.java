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
    private Gracz szeryf;


    @Override
    final public void patrzPoczatekGry(Gracz[] gracze, Gracz szeryf, int liczbaBandytów, int liczbaPomocników) {
        this.szeryf = szeryf;
    }

    @Override
    final public void patrzRuchGracza(Gracz ktoGra) {}

    @Override
    final public void patrzDobralAkcje(Gracz ktoGra, Akcja a) {}

    @Override
    final public void patrzNaDynamit(Gracz ktoGra, boolean wybuchl) {}

    @Override
    final public void patrzWykonalAkcje(Gracz ktoGra, Akcja a, Gracz naKim) {
        ogarnijWykonalAkcje(new StrategicznyWidokGracza(marionetka, ktoGra), a, new StrategicznyWidokGracza(marionetka, naKim));
    }

    @Override
    final public void patrzSkonczylTure(Gracz ktoGra) {}

    @Override
    final public void patrzZabojstwo(Gracz ofiara, Gracz zabojca) {
        ogarnijZabojstwo(new StrategicznyWidokGracza(marionetka, ofiara), new StrategicznyWidokGracza(marionetka, zabojca));
    }

    public void akcjaUlecz(StrategicznyWidokGracza cel) throws PozaZasiegiemWyjatek, BrakAkcjiWyjatek, NieTwojRochWyjatek {
        marionetka.akcjaUlecz(cel.gracz);
    }

    public void akcjaStrzel(StrategicznyWidokGracza cel) throws PozaZasiegiemWyjatek, BrakAkcjiWyjatek, NieTwojRochWyjatek {
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
        return new StrategicznyWidokGracza(marionetka, szeryf);
    }

    abstract protected void ogarnijWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim);

    abstract protected void ogarnijZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca);


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
