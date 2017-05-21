package dzikizachod;

import java.util.*;

/**
 * Klasa utrzymuje listę graczy, którzy strzelili do szeryfa.
 * Ponieważ strategie zliczające szeryfa i pomocnika sa takie same, to ich zachowanie zostało wyodrębnione w klasie tej
 * oraz podklasie {@link ZaawansowanyStrategicznyZliczacz}.
 *
 * Potrafi również przejąć kontrolę nad graczem, żeby w miarę możliwości strzelac do graczy z tej czarnej listy przy
 * pomocy pistoletu ({@link IPistoletStrategicznegoZliczacza}).
 *
 * Nie da się zrobić wielodzidziczenia, więc trzaba wyodrębnić zachowanie odpowiedzialne za strategię zliczajacą.
 * A taki piękny DDoD by był.
 */
public class StrategicznyZliczacz {
    /** Używany RNG */
    private Random rng;

    /** Widok na gracza, używającego tego zliczacza */
    private StrategicznyWidokGracza gracz;

    /** Lista żywych graczy, którzy zaatakowali tego gracza (lub inaczej mu podpalki). */
    private Collection<StrategicznyWidokGracza> paskudniBandyci = new ArrayList<>();

    /** Możliwosć oddania strzału do gracza, wyrażona w Javowym wyobrażeniu o lambdach. */
    private IPistoletStrategicznegoZliczacza pistolet;


    /**
     * Tworzy nowego strategocznego zliczacza, kontrolowanego przez gracza, używajacego podanego RNG i strzelajacego z pistoletu.
     * @param gracz gracz wykorzystujący tego zliczacza
     * @param rng generator liczb losowych
     * @param pistolet lambda używana do strzelania do graczy (generalnie <code>this::skacjaStrzel</code>)
     */
    public StrategicznyZliczacz(StrategicznyWidokGracza gracz, Random rng, IPistoletStrategicznegoZliczacza pistolet) {
        if (gracz == null || rng == null || pistolet == null)
            throw new NullPointerException();
        this.gracz = gracz;
        this.rng = rng;
        this.pistolet = pistolet;
    }


    /**
     * Informuje zliczacza o wykonaniu akcji.
     * Musi być wywołane w metodzie ze stgrategii używającej tego obiektu, aby aktualizowac stan zliczacza.
     * Parametry analogicznie do metody z interfejsu {@link IObserwator}
     */
    public void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {
        if (a == Akcja.STRZEL && naKim.tozsamosc() == TozsamoscGracza.SZERYF)
            dodajPaskude(ktoGra);
    }


    /**
     * Informuje zliczacza o śmierci gracza.
     * Musi być wywołane w metodzie ze stgrategii używającej tego obiektu, aby aktualizowac stan zliczacza.
     * Parametry analogicznie do metody z interfejsu {@link IObserwator}
     */
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {
        usunPaskude(ofiara);
    }


    /**
     * Zwraca listę paskud aktualnie znajdujących się w zasięgu posiadajacego gracza.
     * @return listę paskud w zasięgu
     */
    public ArrayList<StrategicznyWidokGracza> paskudyWZasiegu() {
        ArrayList<StrategicznyWidokGracza> wynik = new ArrayList<>();
        for (StrategicznyWidokGracza paskuda : paskudniBandyci) {
            try {
                if (gracz.odlegloscOd(paskuda) <= gracz.zasieg())
                    wynik.add(paskuda);
            } catch (NieInteresujSieTrupemWyjatek e) {
                System.err.println("To się nie powinno zdażyć");
                e.printStackTrace();
            }
        }
        return wynik;
    }


    /**
     * Strzela losowo do paskud znajdujących się w zasięgu.
     */
    public void zwalczPaskudy() throws BladKonrtoleraWyjatek {
        ArrayList<StrategicznyWidokGracza> paskudy = paskudyWZasiegu();
        while (gracz.ileAkcji(Akcja.STRZEL) > 0 && paskudy.size() > 0 && !gracz.czyKoniecGry()) {
            StrategicznyWidokGracza paskuda = paskudy.get(rng.nextInt(paskudy.size()));
            pistolet.strzel(paskuda);
            if (paskuda.pz() == 0)
                paskudy.remove(paskuda);
        }
    }


    /**
     * Usuwa gracza z listy paskud
     * @param juzNiePaskuda do usunięcia
     */
    protected void usunPaskude(StrategicznyWidokGracza juzNiePaskuda) {
        paskudniBandyci.remove(juzNiePaskuda);
    }


    /**
     * Dodaje gracza do listy paskud
     * @param paskuda gracz do dodania
     */
    protected void dodajPaskude(StrategicznyWidokGracza paskuda) {
        if (!paskudniBandyci.contains(paskuda))
            paskudniBandyci.add(paskuda);
    }
}
