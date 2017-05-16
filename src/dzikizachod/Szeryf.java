package dzikizachod;

/**
 * Gracz typu szeryf
 */
public class Szeryf extends Gracz {
    public Szeryf(Strategia kontroler) {
        super(kontroler, 5);
    }

    @Override
    public TozsamoscGracza tozsamosc() {
        return TozsamoscGracza.SZERYF;
    }
}
