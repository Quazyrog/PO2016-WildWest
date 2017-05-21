package dzikizachod;

import java.util.*;

/**
 * Klasa utrzymuje listę graczy, którzy strzelili do szeryfa.
 * Potrafi również przejąć kontrolę nad graczem, żeby w miarę możliwości strzelac do graczy z tej czarnej listy.
 *
 * Nie da się zrobić wielodzidziczenia, więc trzaba wyodrębnić zachowanie odpowiedzialne za strategię zliczajacą.
 * A taki piękny DDoD by był.
 */
public class StrategicznyZliczacz {
    private Random rng;
    private StrategicznyWidokGracza gracz;

    /** Lista żywych graczy, którzy zaatakowali tego gracza (lub inaczej mu podpalki). */
    private Collection<StrategicznyWidokGracza> paskudniBandyci = new ArrayList<>();

    private IPistoletStrategicznegoZliczacza pistolet;


    public StrategicznyZliczacz(StrategicznyWidokGracza gracz, Random rng, IPistoletStrategicznegoZliczacza pistolet) {
        if (gracz == null || rng == null || pistolet == null)
            throw new NullPointerException();
        this.gracz = gracz;
        this.rng = rng;
        this.pistolet = pistolet;
    }


    public void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {
        if (a == Akcja.STRZEL && naKim.tozsamosc() == TozsamoscGracza.SZERYF)
            dodajPaskude(ktoGra);
    }


    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {
        usunPaskude(ofiara);
    }


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


    public void zwalczPaskudy() throws BladKonrtoleraWyjatek {
        ArrayList<StrategicznyWidokGracza> paskudy = paskudyWZasiegu();
        while (gracz.ileAkcji(Akcja.STRZEL) > 0 && paskudy.size() > 0 && !gracz.czyKoniecGry()) {
            StrategicznyWidokGracza paskuda = paskudy.get(rng.nextInt(paskudy.size()));
            pistolet.strzel(paskuda);
            if (paskuda.pz() == 0)
                paskudy.remove(paskuda);
        }
    }


    protected void usunPaskude(StrategicznyWidokGracza juzNiePaskuda) {
        paskudniBandyci.remove(juzNiePaskuda);
    }


    protected void dodajPaskude(StrategicznyWidokGracza paskuda) {
        if (!paskudniBandyci.contains(paskuda))
            paskudniBandyci.add(paskuda);
    }
}
