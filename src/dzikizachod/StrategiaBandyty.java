package dzikizachod;

import java.util.List;
import java.util.Random;

/**
 * Klasa abstrakcyjna na strategię bandyty
 */
public abstract class StrategiaBandyty extends StrategiaOgolna {
    /**
     * Podczas losowań strategia powinna się posługiwac tym generatorem liczb losowych.
     * @param rng RGN do użycia przez strategię
     */
    public StrategiaBandyty(Random rng) {
        super(rng);
    }


    public StrategiaBandyty() {}


    /**
     * Zwrca wszystkich pomocników w zasiegu mierzonym w podanym kierunku.
     * @param kierunek kierunek, jak w <code>{@link StrategiaOgolna#graczeWZasieguKtorzySa(TozsamoscGracza, int)}</code>
     * @return pomocników szeryfa w zasięgu mierzonym w podanym kierunku
     */
    protected StrategicznyWidokGracza losowyPomocnikWZasieguNaLuku(int kierunek) {
        List<StrategicznyWidokGracza> znalezieniPomocnicy =
                graczeWZasieguKtorzySa(TozsamoscGracza.POMOCNIK_SZERYFA, kierunek);

        if (znalezieniPomocnicy.size() == 0)
            return null;
        return znalezieniPomocnicy.get(rng.nextInt(znalezieniPomocnicy.size()));
    }


    /**
     * O ile to możliwe, wywala magazynek w szeryfa
     * @return <code>true</code>, kiedy było to możliwe
     */
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
