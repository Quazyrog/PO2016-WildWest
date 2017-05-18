package dzikizachod;

/**
 * Ogólna strategia każdego gracza.
 * W swojej metodzie <code>graj()</code> robi tylko to, co jest wspólne dla wszystkich graczy.
 */
public abstract class StrategiaOgolna extends Strategia {
    /**
     * Ogólna metoda graj, zawierająca wspólne elementy strategii.
     * Najpierw zwiększa zasięg, tak długo jak może. Później leczy się, o ile może i to potrzebne.
     */
    @Override
    void graj() {
        try {
            while (ileAkcji(Akcja.ZASIEG_PLUS_JEDEN) > 0)
                akcjaZasiegPlusJeden();
            while (ileAkcji(Akcja.ZASIEG_PLUS_DWA) > 0)
                akcjaZasiegPlusDwa();
            while (ileAkcji(Akcja.ULECZ) > 0 && pz() < maksymalnePZ())
                akcjaUlecz(ja());
        } catch (BladKonrtoleraWyjatek e) {
            e.printStackTrace();
            throw new Error("To się nie powinno zdażyc!", e);
        }
    }
}
