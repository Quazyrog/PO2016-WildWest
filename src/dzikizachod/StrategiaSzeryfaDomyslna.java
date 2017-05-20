package dzikizachod;

import java.util.ArrayList;
import java.util.Random;

public class StrategiaSzeryfaDomyslna extends StrategiaSzeryfa {
    protected Random rng = new Random();

    @Override
    public void patrzKolejnaTura(int numerTury) {}

    @Override
    public void patrzKoniecGry(boolean czyDobroWygralo) {}

    protected ArrayList<StrategicznyWidokGracza> paskudyWZasiegu() {
        ArrayList<StrategicznyWidokGracza> wynik = new ArrayList<>();
        for (StrategicznyWidokGracza paskuda : paskudniBandyci) {
            if (ja().odlegloscOd(paskuda) <= ja().zasieg())
                wynik.add(paskuda);
        }
        return wynik;
    }

    protected void zwalczPaskudy() throws BladKonrtoleraWyjatek {
        ArrayList<StrategicznyWidokGracza> paskudy = paskudyWZasiegu();
        while (ja().ileAkcji(Akcja.STRZEL) > 0 && paskudy.size() > 0) {
            StrategicznyWidokGracza paskuda = paskudy.get(rng.nextInt(paskudy.size()));
            akcjaStrzel(paskuda);
            if (paskuda.pz() == 0)
                paskudy.remove(paskuda);
        }
    }

    protected void zwalczRandomy() throws BladKonrtoleraWyjatek {
        while (ja().ileAkcji(Akcja.STRZEL) > 0) {
            int zasiegRandomowania = Math.min(ja().zasieg(), ja().liczbaGraczy() - 1);
            int polozenieWzgledneCelu = (rng.nextInt(1) * 2 - 1) * (rng.nextInt(zasiegRandomowania) + 1);
            akcjaStrzel(ja().dalekiSasiad(polozenieWzgledneCelu));
        }
    }

    @Override
    void graj() throws BladKonrtoleraWyjatek {
        super.graj();
        if (ja().ileAkcji(Akcja.STRZEL) == 0)
            return; //Smutny szeryf nie ma naboi

        try {
            zwalczPaskudy();
            zwalczRandomy();
        } catch (BladKonrtoleraWyjatek e) {
            if (!ja().czyKoniecGry()) {
                e.printStackTrace();
                throw new Error("To się nie powinno zdażyc!", e);
            }
        }
    }
}
