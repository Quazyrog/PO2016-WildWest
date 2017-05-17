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
    protected void
    ogarnijPoczatekGry(ReprezentacjaGracza[] gracze, ReprezentacjaGracza szeryf,
                       int liczbaBandytów, int liczbaPomocników) {}

    @Override
    protected void ogarnijRuchGracza(ReprezentacjaGracza ktoGra) {}

    @Override
    protected void ogarnijWykonalAkcje(ReprezentacjaGracza ktoGra, Akcja a, ReprezentacjaGracza naKim) {}

    @Override
    protected void ogarnijZabojstwo(ReprezentacjaGracza ofiara, ReprezentacjaGracza zabojca) {}
}
