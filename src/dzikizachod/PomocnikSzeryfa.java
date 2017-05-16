package dzikizachod;

/**
 * Gracz typu pomocnik szeryfa
 */
public class PomocnikSzeryfa extends Gracz {
    public PomocnikSzeryfa(Strategia kontroler) {
        super(kontroler);
    }

    @Override
    public TozsamoscGracza tozsamosc() {
        return TozsamoscGracza.POMOCNIK_SZERYFA;
    }
}
