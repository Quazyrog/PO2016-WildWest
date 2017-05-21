package dzikizachod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Ogólna strategia każdego gracza.
 * W swojej metodzie <code>graj()</code> robi tylko to, co jest wspólne dla wszystkich graczy.
 */
public abstract class StrategiaOgolna extends Strategia {
    public StrategiaOgolna(Random rng) {
        super(rng);
    }

    public StrategiaOgolna() {
    }

    protected List<StrategicznyWidokGracza> graczeWZasieguKtorzySa(TozsamoscGracza kim, int kierunek) {
        if (kierunek != 0) {
            StrategicznyWidokGracza iter = ja().przeskocz(kierunek);
            List<StrategicznyWidokGracza> znalezieni = new ArrayList<>();

            for (int odleglosc = 1; odleglosc <= ja().zasieg(); ++odleglosc) {
                if (iter.tozsamosc() == kim)
                    znalezieni.add(iter);
                iter = iter.przeskocz(kierunek);
            }
            return znalezieni;
        }

        List<StrategicznyWidokGracza> znalezieni = graczeWZasieguKtorzySa(kim, -1);
        znalezieni.addAll(graczeWZasieguKtorzySa(kim, 1));
        return znalezieni;
    }

    /**
     * Ogólna metoda graj, zawierająca wspólne elementy strategii.
     * Najpierw zwiększa zasięg, tak długo jak może. Później leczy się, o ile może i to potrzebne.
     */
    @Override
    void graj() throws BladKonrtoleraWyjatek {
        while (ja().ileAkcji(Akcja.ZASIEG_PLUS_JEDEN) > 0)
            akcjaZasiegPlusJeden();
        while (ja().ileAkcji(Akcja.ZASIEG_PLUS_DWA) > 0)
            akcjaZasiegPlusDwa();
        while (ja().ileAkcji(Akcja.ULECZ) > 0 && ja().pz() < ja().maksymalnePZ())
            akcjaUlecz(ja());
    }
}
