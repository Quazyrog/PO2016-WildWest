package dzikizachod;

/**
 * Gracz typu Bandyta
 */
public class Bandyta extends Gracz {
    /**
     * Tworzy bandytę zarządzanego przez strategię podana jako argument
     * @param kontroler
     */
    public Bandyta(StrategiaBandyty kontroler) {
        super(kontroler);
    }


    /**
     * Tworzy bandytę sterowanego przez strategię domyslną dla bandyty.
     */
    public Bandyta() {
        super(new StrategiaBandytyDomyslna());
    }


    @Override
    public TozsamoscGracza tozsamosc() {
        return TozsamoscGracza.BANDYTA;
    }
}
