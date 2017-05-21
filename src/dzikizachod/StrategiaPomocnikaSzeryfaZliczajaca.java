package dzikizachod;

import java.util.Random;

/**
 * Created by saphir on 21/05/17.
 */
public class StrategiaPomocnikaSzeryfaZliczajaca extends StrategiaPomocnikaSzeryfa {
    public StrategiaPomocnikaSzeryfaZliczajaca(Random rng) {
        super(rng);
    }


    public StrategiaPomocnikaSzeryfaZliczajaca() {}

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
}
