package dzikizachod;


import java.util.Random;

/**
 * Domyślna strategia bandyty.
 * Dąży do ubicia szeryfa.
 */
public class StrategiaBandytyDomyslna extends StrategiaBandyty {
    protected class NieDaSieNicZrobicWyjatek extends BladKonrtoleraWyjatek {
        NieDaSieNicZrobicWyjatek() {
            super("Nie da się nic zrobić");
        }
    }


    public StrategiaBandytyDomyslna(Random rng) {
        super(rng);
    }


    public StrategiaBandytyDomyslna() {
    }


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


    /**
     * Strzela w szeryfa, a jak nie to w jakiegokolwiek pomocnika, preferując tych na krótszym łuku do szeryfa.
     */
    protected void najazdNaSzeryfa() throws BladKonrtoleraWyjatek {
        if (!bijSzeryfaJakMozna()) {
            int doSzeryfa = ja().odlegloscIKierunekOd(szeryf());
            StrategicznyWidokGracza cel = losowyPomocnikWZasieguNaLuku(doSzeryfa);
            if (cel == null)
                cel = losowyPomocnikWZasieguNaLuku(-doSzeryfa);
            if (cel == null)
                throw new NieDaSieNicZrobicWyjatek();
            while (ja().ileAkcji(Akcja.STRZEL) > 0 && cel.pz() > 0)
                akcjaStrzel(cel);
        }
    }


    @Override
    void graj() throws BladKonrtoleraWyjatek {
        super.graj();
        try {
            while (ja().ileAkcji(Akcja.STRZEL) > 0 && !ja().czyKoniecGry())
                najazdNaSzeryfa();
        } catch (NieDaSieNicZrobicWyjatek ignored) {}
    }
}
