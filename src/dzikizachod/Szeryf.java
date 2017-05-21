package dzikizachod;

/**
 * Gracz typu szeryf
 */
public class Szeryf extends Gracz {
    public Szeryf(StrategiaSzeryfa kontroler) {
        super(kontroler, 5);
    }


    public Szeryf() {
        super(new StrategiaSzeryfaDomyslna(), 5);
    }


    @Override
    public TozsamoscGracza tozsamosc() {
        return TozsamoscGracza.SZERYF;
    }
}
