package dzikizachod;

/**
 * Proxy dostępu do gracza, które pozawala tylko na odczytywanie jego stanu, bez modyfikacji.
 */
public class StrategicznyWidokGracza {
    protected Gracz gracz;
    protected Gracz podgladacz;

    public StrategicznyWidokGracza(Gracz podgladajacy, Gracz podgladanyGracz) {
        if (podgladajacy == null)
            throw new NullPointerException();
        if (podgladanyGracz == null)
            throw new NullPointerException();
        this.gracz = podgladanyGracz;
        podgladacz = podgladajacy;
    }

    public StrategicznyWidokGracza przeskocz(int wKierunku) {
        return new StrategicznyWidokGracza(gracz, gracz.przeskocz(wKierunku));
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
                || podgladacz.tozsamosc() == TozsamoscGracza.BANDYTA
                || gracz.pz() == 0
                || gracz == podgladacz)
            return tozsamosc;
        return TozsamoscGracza.NIEZNANA;
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

    public int numer() {
        return gracz.numer();
    }

    public int identyfikator() {
        return gracz.identyfikator();
    }

    public boolean czyKoniecGry() {
        return gracz.czyKoniecGry();
    }

    public int liczbaGraczy() {
        return gracz.liczbaGraczy();
    }

    public boolean czyWykonujeRuch() {
        return gracz.czyWykonujeRuch();
    }

    public StrategicznyWidokGracza dalekiSasiad(int odlegloscSkierowana) {
        return new StrategicznyWidokGracza(gracz, gracz.dalekiSasiad(odlegloscSkierowana));
    }

    public int odlegloscOd(StrategicznyWidokGracza gracz) {
        return this.gracz.odlegloscOd(gracz.gracz);
    }

    public int odlegloscIKierunekOd(StrategicznyWidokGracza gracz) {
        return this.gracz.odlegloscIKierunekOd(gracz.gracz);
    }

    public int odlegloscSkierowanOd(int kierunek, StrategicznyWidokGracza gracz) {
        return this.gracz.odlegloscSkierowanOd(kierunek, gracz.gracz);
    }

    public int ileAkcji(Akcja akcja) throws BladKonrtoleraWyjatek {
        if (podgladacz == gracz)
            return gracz.ileAkcji(akcja);
        throw new BladKonrtoleraWyjatek("Nie wolno podglądać akcji innych graczy");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StrategicznyWidokGracza))
            return false;
        StrategicznyWidokGracza other = (StrategicznyWidokGracza)o;
        return podgladacz == other.podgladacz && gracz == other.gracz;
    }
}
