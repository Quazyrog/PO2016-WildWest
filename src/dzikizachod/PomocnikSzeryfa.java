package dzikizachod;

/**
 * Gracz typu pomocnik szeryfa
 */
public class PomocnikSzeryfa extends Gracz {
    public PomocnikSzeryfa(StrategiaPomocnikaSzeryfa kontroler) {
        super(kontroler);
    }


    public PomocnikSzeryfa() {
        super(new StrategiaPomocnikaSzeryfaDomyslna());
    }


    @Override
    public TozsamoscGracza tozsamosc() {
        return TozsamoscGracza.POMOCNIK_SZERYFA;
    }
}
