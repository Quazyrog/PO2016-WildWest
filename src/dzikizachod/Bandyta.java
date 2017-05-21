package dzikizachod;

/**
 * Gracz typu Bandyta
 */
public class Bandyta extends Gracz {
    public Bandyta(StrategiaBandyty kontroler) {
        super(kontroler);
    }


    public Bandyta() {
        super(new StrategiaBandytyDomyslna());
    }


    @Override
    public TozsamoscGracza tozsamosc() {
        return TozsamoscGracza.BANDYTA;
    }
}
