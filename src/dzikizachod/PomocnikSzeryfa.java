package dzikizachod;

/**
 * Gracz typu pomocnik szeryfa
 */
public class PomocnikSzeryfa extends Gracz {
    /**
     * Pomocnik szeryfa sterowany podana strategią
     * @param kontroler strategia sterujaca
     */
    public PomocnikSzeryfa(StrategiaPomocnikaSzeryfa kontroler) {
        super(kontroler);
    }


    /**
     * Pomocnik sterowany domyślną strategią
     */
    public PomocnikSzeryfa() {
        super(new StrategiaPomocnikaSzeryfaDomyslna());
    }


    @Override
    public TozsamoscGracza tozsamosc() {
        return TozsamoscGracza.POMOCNIK_SZERYFA;
    }
}
