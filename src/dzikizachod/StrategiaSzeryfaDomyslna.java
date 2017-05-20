package dzikizachod;

import java.util.ArrayList;
import java.util.Random;

public class StrategiaSzeryfaDomyslna extends StrategiaSzeryfa {
    protected Random rng = new Random();

    @Override
    public void patrzKolejnaTura(int numerTury) {}

    @Override
    public void patrzKoniecGry(boolean czyDobroWygralo) {}

    @Override
    protected void
    ogarnijPoczatekGry(ReprezentacjaGracza[] gracze, ReprezentacjaGracza szeryf,
                       int liczbaBandytów, int liczbaPomocników)
    {}

    @Override
    protected void ogarnijRuchGracza(ReprezentacjaGracza ktoGra) {}

    protected ArrayList<ReprezentacjaGracza> paskudyWZasiegu() {
        ArrayList<ReprezentacjaGracza> wynik = new ArrayList<>();
        for (ReprezentacjaGracza paskuda : paskudniBandyci) {
            if (odlegloscOd(paskuda) <= zasieg())
                wynik.add(paskuda);
        }
        return wynik;
    }

    protected void zwalczPaskudy() throws BladKonrtoleraWyjatek {
        ArrayList<ReprezentacjaGracza> paskudy = paskudyWZasiegu();
        while (ileAkcji(Akcja.STRZEL) > 0 && paskudy.size() > 0) {
            ReprezentacjaGracza paskuda = paskudy.get(rng.nextInt(paskudy.size()));
            akcjaStrzel(paskuda);
            if (paskuda.pz() == 0)
                paskudy.remove(paskuda);
        }
    }

    protected void zwalczRandomy() throws BladKonrtoleraWyjatek {
        while (ileAkcji(Akcja.STRZEL) > 0) {
            int zasiegRandomowania = Math.min(zasieg(), liczbaGraczy() - 1);
            int polozenieWzgledneCelu = (rng.nextInt(1) * 2 - 1) * (rng.nextInt(zasiegRandomowania) + 1);
            akcjaStrzel(dalekiSasiad(polozenieWzgledneCelu));
        }
    }

    @Override
    void graj() {
        super.graj();
        if (ileAkcji(Akcja.STRZEL) == 0)
            return; //Smutny szeryf nie ma naboi

        try {
            zwalczPaskudy();
            zwalczRandomy();
        } catch (BladKonrtoleraWyjatek e) {
            if (!czyKoniecGry()) {
                e.printStackTrace();
                throw new Error("To się nie powinno zdażyc!", e);
            }
        }
    }
}
