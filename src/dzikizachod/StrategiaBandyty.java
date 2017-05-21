package dzikizachod;

import java.util.List;
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
        List<StrategicznyWidokGracza> znalezieniPomocnicy =
                graczeWZasieguKtorzySa(TozsamoscGracza.POMOCNIK_SZERYFA, kierunek);

        if (znalezieniPomocnicy.size() == 0)
            return null;
        return znalezieniPomocnicy.get(rng.nextInt(znalezieniPomocnicy.size()));
    }


    protected boolean bijSzeryfaJakMozna() throws BladKonrtoleraWyjatek {
        if (ja().odlegloscOd(szeryf()) <= ja().zasieg()) {
            while (ja().ileAkcji(Akcja.STRZEL) > 0)
                akcjaStrzel(szeryf());
            return true;
        }
        return false;
    }


    @Override
    void graj() throws BladKonrtoleraWyjatek {
        super.graj();
        if (ja().ileAkcji(Akcja.DYNAMIT) > 0 && ja().odlegloscSkierowanOd(1, szeryf()) < 4)
            akcjaDynamit();
    }
}
