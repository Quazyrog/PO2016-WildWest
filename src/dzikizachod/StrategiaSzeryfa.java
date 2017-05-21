package dzikizachod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Strategia dla szeryfa.
 * Utrzymuje listę graczy, którzy go zaatakowali. Utrzymuje listę paskud na, które go zaatakowały. Nic jednak nie stoi
 * na przeszkodzie, aby dodawać do niej własne psakudy w podklasach. Trzeba natomiast wywoływac metodę patrzZabojstwo,
 * aby usuwać paskudy.
 */
public abstract class StrategiaSzeryfa extends StrategiaOgolna {
    /** Lista żywych graczy, którzy zaatakowali tego gracza (lub inaczej mu podpalki). */
    protected Collection<StrategicznyWidokGracza> paskudniBandyci = new ArrayList<>();


    public StrategiaSzeryfa(Random rng) {
        super(rng);
    }


    public StrategiaSzeryfa() {}


    @Override
    public void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {
        if (a == Akcja.STRZEL && naKim.equals(ja()))
            dodajPaskude(ktoGra);
    }


    @Override
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {
        usunPaskude(ofiara);
    }


    protected void usunPaskude(StrategicznyWidokGracza juzNiePaskuda) {
        paskudniBandyci.remove(juzNiePaskuda);
    }


    protected void dodajPaskude(StrategicznyWidokGracza paskuda) {
        if (!paskudniBandyci.contains(paskuda))
            paskudniBandyci.add(paskuda);
    }


    protected ArrayList<StrategicznyWidokGracza> paskudyWZasiegu() {
        ArrayList<StrategicznyWidokGracza> wynik = new ArrayList<>();
        for (StrategicznyWidokGracza paskuda : paskudniBandyci) {
            try {
                if (ja().odlegloscOd(paskuda) <= ja().zasieg())
                    wynik.add(paskuda);
            } catch (NieInteresujSieTrupemWyjatek e) {
                System.err.println("To się nie powinno zdażyć");
                e.printStackTrace();
            }
        }
        return wynik;
    }


    protected void zwalczPaskudy() throws BladKonrtoleraWyjatek {
        ArrayList<StrategicznyWidokGracza> paskudy = paskudyWZasiegu();
        while (ja().ileAkcji(Akcja.STRZEL) > 0 && paskudy.size() > 0 && !ja().czyKoniecGry()) {
            StrategicznyWidokGracza paskuda = paskudy.get(rng.nextInt(paskudy.size()));
            akcjaStrzel(paskuda);
            if (paskuda.pz() == 0)
                paskudy.remove(paskuda);
        }
    }
}

