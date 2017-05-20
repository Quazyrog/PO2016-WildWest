package dzikizachod;

/**
 * Strategia na potrzeby testów, która nic nie robi.
 */
public class StrategiaWygrywajaca extends Strategia {
    @Override
    void graj() {}

    @Override
    public void patrzKolejnaTura(int numerTury) {}

    @Override
    public void patrzKoniecGry(Zakonczenie czyDobroWygralo) {}

    @Override
    public void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {}

    @Override
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {}
}
