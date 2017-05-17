package dzikizachod;

/**
 * Strategia kontrolująca gracza.
 * Zawiera wszystkie metody, których strategie powinny używać do grania.
 */
abstract public class Strategia {
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

    //POCZĄTEK METOD DELEGOWANYCH Z REPREZENTOWANEGO GRAZCZA
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
    //KONIEC METOD DELEGOWANYCH Z REPREZENTOWANEGO GRAZCZA
    }

    /** Gracz kontrolowany przez tę strategię. */
    private Gracz marionetka;

    final void przypiszGracza(Gracz gracz) {
        marionetka = gracz;
    }

    final protected ReprezentacjaGracza ja() {

        return new ReprezentacjaGracza(marionetka);
    }

//POCZĄTEK METOD DELEGOWANYCH Z KONTROLOWANEGO GRAZCZA
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

    final protected void akcjaUlecz(ReprezentacjaGracza cel) throws PozaZasiegiemWyjatek, BrakAkcjiWyjatek {
        marionetka.akcjaUlecz(cel.gracz);
    }

    final protected void akcjaStrzel(ReprezentacjaGracza cel) throws PozaZasiegiemWyjatek, BrakAkcjiWyjatek {
        marionetka.akcjaStrzel(cel.gracz);
    }

    final protected void akcjaZasiegPlusJeden() throws BrakAkcjiWyjatek {
        marionetka.akcjaZasiegPlusJeden();
    }

    final protected void akcjaZasiegPlusDwa() throws BrakAkcjiWyjatek {
        marionetka.akcjaZasiegPlusDwa();
    }

    final protected void akcjaDynamit() throws BrakAkcjiWyjatek {
        marionetka.akcjaDynamit();
    }

    final protected int ileAkcji(Akcja akcja) {
        return marionetka.ileAkcji(akcja);
    }
//KONIEC METOD DELEGOWANYCH Z KONTROLOWANEGO GRACZA

    abstract void graj();
}
