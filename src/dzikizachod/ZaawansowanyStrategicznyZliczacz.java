package dzikizachod;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Ten strategiczny zliczacz dodatkowo utrzymuje tych, którzy zabili więcej pomocników, niż bandytów.
 *
 * Jest to poprostu zliczacz wykorzystywany przez strategie zliczajace.
 */
public class ZaawansowanyStrategicznyZliczacz extends StrategicznyZliczacz {
    private Map<Integer, Integer> licznikZuuaaa = new TreeMap<>();


    public ZaawansowanyStrategicznyZliczacz(StrategicznyWidokGracza gracz, Random rng, IPistoletStrategicznegoZliczacza pistolet) {
        super(gracz, rng, pistolet);
    }


    @Override
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {
        super.patrzZabojstwo(ofiara, zabojca);

        if (ofiara.tozsamosc() == TozsamoscGracza.POMOCNIK_SZERYFA)
            zwiekszLicznikZuuaaa(zabojca);
        else if (ofiara.tozsamosc() == TozsamoscGracza.BANDYTA)
            zmniejszLicznikZuuaaa(zabojca);
    }


    protected void zwiekszLicznikZuuaaa(StrategicznyWidokGracza komu) {
        int startaWartosc = 0;
        if (licznikZuuaaa.containsKey(komu.identyfikator()))
            startaWartosc = licznikZuuaaa.get(komu.identyfikator());
        licznikZuuaaa.put(komu.identyfikator(), startaWartosc + 1);
        if (startaWartosc == 0)
            dodajPaskude(komu);
    }


    protected void zmniejszLicznikZuuaaa(StrategicznyWidokGracza komu) {
        int startaWartosc = 0;
        if (licznikZuuaaa.containsKey(komu.identyfikator()))
            startaWartosc = licznikZuuaaa.get(komu.identyfikator());
        licznikZuuaaa.put(komu.identyfikator(), startaWartosc - 1);
        if (startaWartosc == 1)
            usunPaskude(komu);
    }
}
