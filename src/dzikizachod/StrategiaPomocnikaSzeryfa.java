package dzikizachod;

import java.util.Random;

/**
 * Ogólna strategia pomocnikmów szeryfa
 */
public abstract class StrategiaPomocnikaSzeryfa extends StrategiaOgolna {
    /**
     * Podczas losowań strategia powinna się posługiwac tym generatorem liczb losowych.
     * @param rng RGN do użycia przez strategię
     */
    public StrategiaPomocnikaSzeryfa(Random rng) {
        super(rng);
    }


    public StrategiaPomocnikaSzeryfa() {}


    @Override
    void graj() throws BladKonrtoleraWyjatek {
        if (ja().odlegloscOd(szeryf()) == 1) {
            while (szeryf().pz() < szeryf().maksymalnePZ() && ja().ileAkcji(Akcja.ULECZ) > 0)
                akcjaUlecz(szeryf());
        }
        super.graj();
        if (ja().ileAkcji(Akcja.DYNAMIT) > 0 && ja().odlegloscSkierowanOd(1, szeryf()) > 4)
            akcjaDynamit();
    }
}
