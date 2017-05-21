package dzikizachod;


import java.util.Random;

/**
 * Zliczająca strategia szeryfa.
 * Do listy paskud z ogólnej strategii dodatkowo dodaje graczy, którzy zabili więcej pomocników niż bandytów.
 */
public class StrategiaSzeryfaZliczajaca extends StrategiaSzeryfa {
    /** Zliczacz używany przez te strategię */
    ZaawansowanyStrategicznyZliczacz zliczacz;

    public StrategiaSzeryfaZliczajaca(Random rng) {
        super(rng);
    }


    public StrategiaSzeryfaZliczajaca() {}


    @Override
    public void patrzKolejnaTura(int numerTury) {
        if (numerTury == 1)
             zliczacz = new ZaawansowanyStrategicznyZliczacz(ja(), rng, this::akcjaStrzel);
    }


    @Override
    public void patrzRuchGracza(StrategicznyWidokGracza ktoGra) {}


    @Override
    public void patrzDobralAkcje(StrategicznyWidokGracza ktoGra, Akcja a) {}


    @Override
    public void patrzNaDynamit(StrategicznyWidokGracza ktoGra, boolean wybuchl) {}


    @Override
    public void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {
        zliczacz.patrzWykonalAkcje(ktoGra, a, naKim);
    }


    @Override
    public void patrzSkonczylTure(StrategicznyWidokGracza ktoGra) {}


    @Override
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {
        zliczacz.patrzZabojstwo(ofiara, zabojca);
    }


    @Override
    public void patrzKoniecGry(Zakonczenie zakonczenie) {}


    @Override
    void graj() throws BladKonrtoleraWyjatek {
        super.graj();
        zliczacz.zwalczPaskudy();
    }
}
