package dzikizachod;

import java.util.List;

/**
 * Created by saphir on 21/05/17.
 */
class FiltrObserwatora implements IObserwator {
    private Gracz perspektywa;
    private IObserwator docelowy;
    StrategicznyWidokGracza[] widokiGraczy;


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


    protected StrategicznyWidokGracza wewnetrznyWidokGracza(StrategicznyWidokGracza pelnyWidok) {
        if (pelnyWidok != null)
            return widokiGraczy[pelnyWidok.identyfikator()];
        return null;
    }
}
