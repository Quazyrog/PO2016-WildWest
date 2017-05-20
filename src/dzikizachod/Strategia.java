package dzikizachod;

/**
 * Strategia kontrolująca gracza.
 * Zawiera całość implementacji, której konkretne strategie powinny używać do komunikowania się ze wszystkimi
 * elementami gry.
 *
 * Pozwala
 */
abstract public class Strategia implements IObserwator {
    /**
     * Proxy, przez które podklasy strategii widzą innyuch graczy.
     * Deleguje pewne metody Gracza, do których powinny mieć dostęp strategie kontrolujące innych graczy.
     */
    final protected class ReprezentacjaGracza {
        /** Gracz reprrezentowany przez ten obiekt */
        private Gracz gracz; //Podklasy nie mają dostępu do gracza, jak jest private

        ReprezentacjaGracza(Gracz g) {
            gracz = g;
        }

        /**
         * Zwraca być może utajnioną tożsamość gracza reprezentowanego przez ten obiekt.
         * Prawdziwa tożsamość gracza może być zwrócona w następujących sytuacjach:
         *   - kiedy odpytywany gracz jest szeryfem
         *   - kiedy pytajacy jest bandytą (zna pozostałych bandytów, szeryf jest jawny, więc zna tez pomocników)
         *   - kiedy odpytywany gracz jest martwy
         *   - kiedy strategia pyta o siebie
         * W każdym innym przypadku zostaje zwrócone <code>TozsamoscGracza.NIEZNANA</code>
         * @return tożsamośc gracza, z perspektywy zadajacego pytanie
         */
        public TozsamoscGracza tozsamosc() {
            TozsamoscGracza tozsamosc = gracz.tozsamosc();
            if (tozsamosc == TozsamoscGracza.SZERYF
                    || marionetka.tozsamosc() == TozsamoscGracza.BANDYTA
                    || gracz.pz() == 0
                    || gracz == marionetka)
                return tozsamosc;
            return TozsamoscGracza.NIEZNANA;
        }

        /**
         * Sprawdza, czy ta reprezentacja gracza faktycznie odnosi się do jakiegoś gracza.
         * Na przykład podczas rzucenia dynamitu, reprezentacja gracza-celu powinna być pusta.
         * @return <code>true</code> gdy reprezentuje gracza; <code>false</code> w przeciwnym przypadku
         */
        public boolean czyNieIstnieje() {
            return gracz == null;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof ReprezentacjaGracza))
                return false;
            return ((ReprezentacjaGracza)other).gracz == gracz;
        }

        public int pz() {
            return gracz.pz();
        }

        public int maksymalnePZ() {
            return gracz.maksymalnePZ();
        }

        public int zasieg() {
            return gracz.zasieg();
        }

        public int odlegloscOd(ReprezentacjaGracza repr) {
            return this.gracz.odlegloscOd(repr.gracz);
        }

        public int numer() {
            return gracz.numer();
        }

        public int odlegloscIKierunekOd(ReprezentacjaGracza gracz) {
            return this.gracz.odlegloscIKierunekOd(gracz.gracz);
        }

        public ReprezentacjaGracza przeskocz(int kierunek) {
            return new ReprezentacjaGracza(gracz.przeskocz(kierunek));
        }
    }

    /** Gracz kontrolowany przez tę strategię. */
    private Gracz marionetka;

    /** Szeryf */
    protected ReprezentacjaGracza szeryf;


    public int odlegloscIKierunekOd(ReprezentacjaGracza gracz) {
        return marionetka.odlegloscIKierunekOd(gracz.gracz);
    }

    @Override
    final public void patrzPoczatekGry(Gracz[] gracze, Gracz szeryf, int liczbaBandytów, int liczbaPomocników) {
        this.szeryf = new ReprezentacjaGracza(szeryf);
        ReprezentacjaGracza reprezentacja[] = new ReprezentacjaGracza[gracze.length];
        ogarnijPoczatekGry(reprezentacja, this.szeryf, liczbaBandytów, liczbaPomocników);
    }

    @Override
    final public void patrzRuchGracza(Gracz ktoGra) {
        ogarnijRuchGracza(new ReprezentacjaGracza(ktoGra));
    }

    @Override
    final public void patrzDobralAkcje(Gracz ktoGra, Akcja a) {}

    @Override
    final public void patrzNaDynamit(Gracz ktoGra, boolean wybuchl) {}

    @Override
    final public void patrzWykonalAkcje(Gracz ktoGra, Akcja a, Gracz naKim) {
        ogarnijWykonalAkcje(new ReprezentacjaGracza(ktoGra), a, new ReprezentacjaGracza(naKim));
    }

    @Override
    final public void patrzSkonczylTure(Gracz ktoGra) {}

    @Override
    final public void patrzZabojstwo(Gracz ofiara, Gracz zabojca) {
        ogarnijZabojstwo(new ReprezentacjaGracza(ofiara), new ReprezentacjaGracza(zabojca));
    }

    public boolean czyKoniecGry() {
        return marionetka.czyKoniecGry();
    }

    public boolean czyWykonujeRuch() {
        return marionetka.czyWykonujeRuch();
    }

    final protected ReprezentacjaGracza ja() {

        return new ReprezentacjaGracza(marionetka);
    }

    final protected TozsamoscGracza tozsamosc() {
        return marionetka.tozsamosc();
    }

    final protected int pz() {
        return marionetka.pz();
    }

    final protected int maksymalnePZ() {
        return marionetka.maksymalnePZ();
    }

    final protected int zasieg() {
        return marionetka.zasieg();
    }

    final protected ReprezentacjaGracza dalekiSasiad(int odlegloscSkierowana) {
        return new ReprezentacjaGracza(marionetka.dalekiSasiad(odlegloscSkierowana));
    }

    final protected int odlegloscOd(ReprezentacjaGracza gracz) {
        return marionetka.odlegloscOd(gracz.gracz);
    }

    final protected void akcjaUlecz(ReprezentacjaGracza cel)
            throws PozaZasiegiemWyjatek, BrakAkcjiWyjatek, NieTwojRochWyjatek {
        marionetka.akcjaUlecz(cel.gracz);
    }

    final protected void akcjaStrzel(ReprezentacjaGracza cel)
            throws PozaZasiegiemWyjatek, BrakAkcjiWyjatek, NieTwojRochWyjatek {
        marionetka.akcjaStrzel(cel.gracz);
    }

    final protected void akcjaZasiegPlusJeden() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        marionetka.akcjaZasiegPlusJeden();
    }

    final protected void akcjaZasiegPlusDwa() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        marionetka.akcjaZasiegPlusDwa();
    }

    final protected void akcjaDynamit() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        marionetka.akcjaDynamit();
    }

    final protected int ileAkcji(Akcja akcja) {
        return marionetka.ileAkcji(akcja);
    }

    abstract protected void
    ogarnijPoczatekGry(ReprezentacjaGracza[] gracze, ReprezentacjaGracza szeryf,
                       int liczbaBandytów, int liczbaPomocników);

    abstract protected void ogarnijRuchGracza(ReprezentacjaGracza ktoGra);

    abstract protected void ogarnijWykonalAkcje(ReprezentacjaGracza ktoGra, Akcja a, ReprezentacjaGracza naKim);

    abstract protected void ogarnijZabojstwo(ReprezentacjaGracza ofiara, ReprezentacjaGracza zabojca);


    final void przypiszGracza(Gracz gracz) {
        marionetka = gracz;
    }

    /**
     * W tej metodzie strategia kontroluje gracza podczas jego tury.
     * Kontrolująca strategia powinna sprawdzać, czy jest już koniec gry i należy zakończyć sterowanie graczem.
     * @throws BladKonrtoleraWyjatek jakby nie złapała, to gracz obsłuży
     */
    abstract void graj() throws BladKonrtoleraWyjatek;
}
