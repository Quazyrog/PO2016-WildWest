package dzikizachod;

import java.util.Random;

/**
 * Cierpliwa strategia bandyty
 */
public class StrategiaBandytyCierpliwa extends StrategiaBandyty {
    public StrategiaBandytyCierpliwa(Random rng) {
        super(rng);
    }

    public StrategiaBandytyCierpliwa() {}

    @Override
    public void patrzKolejnaTura(int numerTury) {}

    @Override
    public void patrzRuchGracza(StrategicznyWidokGracza ktoGra) {}

    @Override
    public void patrzDobralAkcje(StrategicznyWidokGracza ktoGra, Akcja a) {}

    @Override
    public void patrzNaDynamit(StrategicznyWidokGracza ktoGra, boolean wybuchl) {}

    @Override
    public void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {}

    @Override
    public void patrzSkonczylTure(StrategicznyWidokGracza ktoGra) {}

    @Override
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {}

    @Override
    public void patrzKoniecGry(Zakonczenie zakonczenie) {}

    @Override
    void graj() throws BladKonrtoleraWyjatek {
        super.graj();
        bijSzeryfaJakMozna();
    }
}
