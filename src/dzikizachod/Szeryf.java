package dzikizachod;

/**
 * Gracz typu szeryf
 */
public class Szeryf extends Gracz {
    /**
     * Inicjalizuje szeryfa z podaną strategia
     * @param kontroler
     */
    public Szeryf(StrategiaSzeryfa kontroler) {
        super(kontroler, 5);
    }


    /**
     * Inicjalizuje szeryfa z dmyślną mu strategią
     */
    public Szeryf() {
        super(new StrategiaSzeryfaDomyslna(), 5);
    }


    @Override
    public TozsamoscGracza tozsamosc() {
        return TozsamoscGracza.SZERYF;
    }
}
