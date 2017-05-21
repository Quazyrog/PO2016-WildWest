package dzikizachod;


import java.util.Random;

public class StrategiaSzeryfaDomyslna extends StrategiaSzeryfa {
    private StrategicznyZliczacz zliczacz = new StrategicznyZliczacz(ja(), rng, this::akcjaStrzel);


    public StrategiaSzeryfaDomyslna(Random rng) {
        super(rng);
    }


    public StrategiaSzeryfaDomyslna() {}


    @Override
    public void patrzKolejnaTura(int numerTury) {
        if (numerTury == 1)
            zliczacz = new StrategicznyZliczacz(ja(), rng, this::akcjaStrzel);
    }


    @Override
    public void patrzKoniecGry(Zakonczenie zakonczenie) {}


    @Override
    public void patrzRuchGracza(StrategicznyWidokGracza ktoGra) {}


    @Override
    public void patrzDobralAkcje(StrategicznyWidokGracza ktoGra, Akcja a) {}


    @Override
    public void patrzNaDynamit(StrategicznyWidokGracza ktoGra, boolean wybuchl) {}


    @Override
    public void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {
        zliczacz.patrzWykonalAkcje(ktoGra, a, naKim);
    }


    @Override
    public void patrzSkonczylTure(StrategicznyWidokGracza ktoGra) {}


    @Override
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {
        zliczacz.patrzZabojstwo(ofiara, zabojca);
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
        zliczacz.zwalczPaskudy();
        zwalczRandomy();
    }
}
