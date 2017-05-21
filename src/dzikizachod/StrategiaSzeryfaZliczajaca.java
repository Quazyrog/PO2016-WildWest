package dzikizachod;


import java.util.Map;
import java.util.TreeMap;

/**
 * Zliczająca strategia szeryfa.
 * Do listy paskud z ogólnej strategii dodatkowo dodaje graczy, którzy zabili więcej pomocników niż bandytów.
 */
public class StrategiaSzeryfaZliczajaca extends StrategiaSzeryfa {
    Map<Integer, Integer> licznikZuuaaa = new TreeMap<>();

    @Override
    public void patrzKolejnaTura(int numerTury) {}


    @Override
    public void patrzRuchGracza(StrategicznyWidokGracza ktoGra) {}


    @Override
    public void patrzDobralAkcje(StrategicznyWidokGracza ktoGra, Akcja a) {}


    @Override
    public void patrzNaDynamit(StrategicznyWidokGracza ktoGra, boolean wybuchl) {}


    @Override
    public void patrzSkonczylTure(StrategicznyWidokGracza ktoGra) {}


    @Override
    public void patrzKoniecGry(Zakonczenie zakonczenie) {}


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
