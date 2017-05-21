package dzikizachod;

import java.util.List;

/**
 * Obserwator Disboardu, który przekazuje infromacje dalej do strategii kierującej danym graczem.
 * Celem tej klasy jest pośredniczenie w wymianie informacji pomiędzy strategią, a Disboardem. Disboard wysyła
 * do wszystkich obserwatorów komunikaty dające im pełną możliwość sprawdzania stanu graczy w grze, co byłoby
 * niedopuszczalne w wypadku strategii. Tak klasa ogranicza informacje zawarte w komunikatach i przekazuje je dalej do
 * strategii sterującej graczem.
 */
class FiltrObserwatora implements IObserwator {
    /** Gracz, do którego strategii przekazywane są informacje */
    private Gracz perspektywa;

    /** Strategia sterująca tymże graczem */
    private IObserwator docelowy;

    /** Widoki wszystkich graczy z perspektywy danego gracza. To one są przekazywane w komunikatach wysyłanych do
     * strategii zamiast oryginalnych widoków podawanych przez Disboard. */
    StrategicznyWidokGracza[] widokiGraczy;


    /**
     * Tworzy nowy filtr dla obserwatora sterującego graczem.
     * @param docelowy strategia-obserwator sterujący graczem (może byc także zwyczajny obserwator, ale obecnie nie
     *                 jest to stosowane). Tam są przekazywane dalej komunikaty od Disboardu
     * @param prespektywa gracz, którym steruje obserwator (lub z którego widzi obserwator). Używany jako podglądający
     *                    do tworzenia widoków przekazywanych dalej
     * @param gracze wszyscy gracze, zaczynajacy grę
     */
    public FiltrObserwatora(IObserwator docelowy, Gracz prespektywa, List<Gracz> gracze) {
        this.docelowy = docelowy;
        this.perspektywa = prespektywa;

        widokiGraczy = new StrategicznyWidokGracza[gracze.size()];
        for (int i = 0; i < gracze.size(); ++i)
            widokiGraczy[i] = new StrategicznyWidokGracza(perspektywa, gracze.get(i));
    }


    @Override
    public void patrzPoczatekGry(StrategicznyWidokGracza[] gracze, StrategicznyWidokGracza szeryf, int liczbaBandytów, int liczbaPomocników) {
        docelowy.patrzPoczatekGry(widokiGraczy, wewnetrznyWidokGracza(szeryf), liczbaBandytów, liczbaPomocników);
    }


    @Override
    public void patrzKolejnaTura(int numerTury) {
        docelowy.patrzKolejnaTura(numerTury);
    }


    @Override
    public void patrzRuchGracza(StrategicznyWidokGracza ktoGra) {
        docelowy.patrzRuchGracza(wewnetrznyWidokGracza(ktoGra));
    }


    @Override
    public void patrzDobralAkcje(StrategicznyWidokGracza ktoGra, Akcja a) {}


    @Override
    public void patrzNaDynamit(StrategicznyWidokGracza ktoGra, boolean wybuchl) {
        docelowy.patrzNaDynamit(wewnetrznyWidokGracza(ktoGra), wybuchl);
    }


    @Override
    public void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {
        docelowy.patrzWykonalAkcje(wewnetrznyWidokGracza(ktoGra), a, wewnetrznyWidokGracza(naKim));
    }


    @Override
    public void patrzSkonczylTure(StrategicznyWidokGracza ktoGra) {
        docelowy.patrzSkonczylTure(wewnetrznyWidokGracza(ktoGra));
    }


    @Override
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {
        docelowy.patrzZabojstwo(wewnetrznyWidokGracza(ofiara), wewnetrznyWidokGracza(zabojca));
    }


    @Override
    public void patrzKoniecGry(Zakonczenie zakonczenie) {
        docelowy.patrzKoniecGry(zakonczenie);
    }


    /**
     * Zwraca zawężony widok odpowiadajacy podanemu lub <code>null</code>
     * @param pelnyWidok pełny widok, przekazany przez Disboard
     * @return Jeśli <code>pelnyWidok != null</code>:
     *         <code>{@link StrategicznyWidokGracza}(pelnyWidok.podgladacz, pelnyWidok.gracz)</code>
     *         albo <code>null</code>
     */
    protected StrategicznyWidokGracza wewnetrznyWidokGracza(StrategicznyWidokGracza pelnyWidok) {
        if (pelnyWidok != null)
            return widokiGraczy[pelnyWidok.identyfikator()];
        return null;
    }
}
