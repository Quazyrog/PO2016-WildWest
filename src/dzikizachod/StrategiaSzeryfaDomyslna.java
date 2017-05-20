package dzikizachod;

import java.util.ArrayList;
import java.util.Random;

public class StrategiaSzeryfaDomyslna extends StrategiaSzeryfa {
    protected Random rng = new Random();

    @Override
    public void patrzKolejnaTura(int numerTury) {}

    @Override
    public void patrzKoniecGry(Zakonczenie zakonczenie) {}

    protected ArrayList<StrategicznyWidokGracza> paskudyWZasiegu() {
        ArrayList<StrategicznyWidokGracza> wynik = new ArrayList<>();
        for (StrategicznyWidokGracza paskuda : paskudniBandyci) {
            try {
                if (ja().odlegloscOd(paskuda) <= ja().zasieg())
                    wynik.add(paskuda);
            } catch (NieInteresujSieTrupemWyjatek e) {
                System.err.println("To się nie powinno zdażyć");
                e.printStackTrace();
            }
        }
        return wynik;
    }

    protected void zwalczPaskudy() throws BladKonrtoleraWyjatek {
        ArrayList<StrategicznyWidokGracza> paskudy = paskudyWZasiegu();
        while (ja().ileAkcji(Akcja.STRZEL) > 0 && paskudy.size() > 0 && !ja().czyKoniecGry()) {
            StrategicznyWidokGracza paskuda = paskudy.get(rng.nextInt(paskudy.size()));
            akcjaStrzel(paskuda);
            if (paskuda.pz() == 0)
                paskudy.remove(paskuda);
        }
    }

    protected void zwalczRandomy() throws BladKonrtoleraWyjatek {
        while (ja().ileAkcji(Akcja.STRZEL) > 0 && !ja().czyKoniecGry()) {
            int zasiegRandomowania = Math.min(ja().zasieg(), ja().liczbaGraczy() - 1);
            int polozenieWzgledneCelu = (rng.nextInt(2) * 2 - 1) * (rng.nextInt(zasiegRandomowania) + 1);
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
