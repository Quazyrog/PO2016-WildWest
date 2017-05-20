package dzikizachod;

/**
 * Ogólna strategia pomocnikmów szeryfa
 */
public abstract class StrategiaPomocnikaSzeryfa extends StrategiaOgolna {
    @Override
    void graj() throws BladKonrtoleraWyjatek {
        super.graj();
        if (ja().ileAkcji(Akcja.DYNAMIT) > 0 && ja().odlegloscSkierowanOd(1, szeryf()) > 4)
            akcjaDynamit();
    }
}
