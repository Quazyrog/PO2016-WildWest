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
    public void patrzKoniecGry(boolean czyDobroWygralo) {}

    @Override
    protected void ogarnijWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {}

    @Override
    protected void ogarnijZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {}
}
