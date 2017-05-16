package dzikizachod;

/**
 * Gracz typu Bandyta
 */
public class Bandyta extends Gracz {
    public Bandyta(Strategia kontroler) {
        super(kontroler);
    }

    @Override
    public TozsamoscGracza tozsamosc() {
        return TozsamoscGracza.BANDYTA;
    }
}
