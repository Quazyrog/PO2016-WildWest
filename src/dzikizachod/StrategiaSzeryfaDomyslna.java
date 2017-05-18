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

    @Override
    void graj() {
        super.graj();
        if (ileAkcji(Akcja.STRZEL) == 0)
            return; //Smutny szeryf nie ma naboi

        ArrayList<ReprezentacjaGracza> paskudy = paskudyWZasiegu();
        while (ileAkcji(Akcja.STRZEL) > 0) {
            try {
                akcjaStrzel(paskudy.get(rng.nextInt(paskudy.size())));
            } catch (BladKonrtoleraWyjatek e) {
                e.printStackTrace();
                throw new Error("To się nie powinno zdażyc!", e);
            }
        }
    }
}
