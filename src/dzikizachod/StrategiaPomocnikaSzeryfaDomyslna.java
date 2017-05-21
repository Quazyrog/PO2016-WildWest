package dzikizachod;

import java.util.Random;

/**
 * Domyślna strategia pomocnika szeryfa
 */
public class StrategiaPomocnikaSzeryfaDomyslna extends StrategiaPomocnikaSzeryfa {
    /**
     * Podczas losowań strategia powinna się posługiwac tym generatorem liczb losowych.
     * @param rng RGN do użycia przez strategię
     */
    public StrategiaPomocnikaSzeryfaDomyslna(Random rng) {
        super(rng);
    }


    public StrategiaPomocnikaSzeryfaDomyslna() {}


    @Override
    public void patrzKolejnaTura(int numerTury) {}


    @Override
    public void patrzKoniecGry(Zakonczenie zakonczenie) {}


    @Override
    public void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {}


    @Override
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {}


    @Override
    public void patrzRuchGracza(StrategicznyWidokGracza ktoGra) {}


    @Override
    public void patrzDobralAkcje(StrategicznyWidokGracza ktoGra, Akcja a) {}


    @Override
    public void patrzNaDynamit(StrategicznyWidokGracza ktoGra, boolean wybuchl) {}


    @Override
    public void patrzSkonczylTure(StrategicznyWidokGracza ktoGra) {}


    @Override
    void graj() throws BladKonrtoleraWyjatek {
        super.graj();
        int zasiegLosowania = Math.min(ja().liczbaGraczy() - 1, ja().zasieg());
        while (ja().ileAkcji(Akcja.STRZEL) > 0 && !ja().czyKoniecGry()) {
            int polozenieCelu = (rng.nextInt(2) * 2 - 1) * (rng.nextInt(zasiegLosowania) + 1);
            StrategicznyWidokGracza cel = ja().dalekiSasiad(polozenieCelu);
            if (cel.tozsamosc() == TozsamoscGracza.SZERYF)
                continue;
            akcjaStrzel(cel);
        }
    }
}
