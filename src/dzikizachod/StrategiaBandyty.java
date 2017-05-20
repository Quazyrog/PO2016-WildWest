package dzikizachod;

import java.util.ArrayList;
import java.util.Random;

/**
 * Klasa abstrakcyjna na strategię bandyty
 */
public abstract class StrategiaBandyty extends StrategiaOgolna {
    protected Random rng = new Random();

    protected ReprezentacjaGracza losowyPomocnikWZasieguNaLuku(int kierunek) {
        ReprezentacjaGracza iter = ja().przeskocz(kierunek);
        ArrayList<ReprezentacjaGracza> znalezieniPomocnicy = new ArrayList<>();

        for (int odleglosc = 1; odleglosc < zasieg(); ++odleglosc) {
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
    }
}
