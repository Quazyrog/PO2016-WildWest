package dzikizachod;

import java.util.Random;

/**
 * Domyślna strategia pomocnika szeryfa
 */
public class StrategiaPomocnikaSzeryfaDomyslna extends StrategiaPomocnikaSzeryfa {
    private Random rng = new Random();

    @Override
    public void patrzKolejnaTura(int numerTury) {}

    @Override
    public void patrzKoniecGry(Zakonczenie zakonczenie) {}

    @Override
    public void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {}

    @Override
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {}

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
