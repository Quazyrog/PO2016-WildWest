package dzikizachod;

import java.util.Random;

/**
 * Zliczająca strategia pomocnika szeryfa
 */
public class StrategiaPomocnikaSzeryfaZliczajaca extends StrategiaPomocnikaSzeryfa {
    /** Zliczacz wykorzystywany przez tę strategię */
    ZaawansowanyStrategicznyZliczacz zliczacz;


    public StrategiaPomocnikaSzeryfaZliczajaca(Random rng) {
        super(rng);
    }


    public StrategiaPomocnikaSzeryfaZliczajaca() {}


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
