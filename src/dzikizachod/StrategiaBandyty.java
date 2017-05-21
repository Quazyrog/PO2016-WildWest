package dzikizachod;

import java.util.ArrayList;
import java.util.Random;

/**
 * Klasa abstrakcyjna na strategię bandyty
 */
public abstract class StrategiaBandyty extends StrategiaOgolna {
    public StrategiaBandyty(Random rng) {
        super(rng);
    }


    public StrategiaBandyty() {}


    protected StrategicznyWidokGracza losowyPomocnikWZasieguNaLuku(int kierunek) {
        StrategicznyWidokGracza iter = ja().przeskocz(kierunek);
        ArrayList<StrategicznyWidokGracza> znalezieniPomocnicy = new ArrayList<>();

        for (int odleglosc = 1; odleglosc < ja().zasieg(); ++odleglosc) {
            if (iter.tozsamosc() == TozsamoscGracza.POMOCNIK_SZERYFA)
                znalezieniPomocnicy.add(iter);
            iter = iter.przeskocz(kierunek);
        }

        if (znalezieniPomocnicy.size() == 0)
            return null;
        return znalezieniPomocnicy.get(rng.nextInt(znalezieniPomocnicy.size()));
    }


    @Override
    void graj() throws BladKonrtoleraWyjatek {
        super.graj();
        if (ja().ileAkcji(Akcja.DYNAMIT) > 0 && ja().odlegloscSkierowanOd(1, szeryf()) < 4)
            akcjaDynamit();
    }
}
