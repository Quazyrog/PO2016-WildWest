package dzikizachod;


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

    @Override
    public void patrzKolejnaTura(int numerTury) {}

    @Override
    public void patrzKoniecGry(Zakonczenie zakonczenie) {}

    @Override
    public void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {}

    @Override
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {}

    protected void najazdNaSzeryfa() throws BladKonrtoleraWyjatek {
        int doSzeryfa = ja().odlegloscIKierunekOd(szeryf());
        if (Math.abs(doSzeryfa) <= ja().zasieg()) {
            while (ja().ileAkcji(Akcja.STRZEL) > 0)
                akcjaStrzel(szeryf());
        } else {
            StrategicznyWidokGracza cel = losowyPomocnikWZasieguNaLuku(doSzeryfa);
            if (cel == null)
                cel = losowyPomocnikWZasieguNaLuku(-doSzeryfa);
            if (cel == null)
                throw new NieDaSieNicZrobicWyjatek();
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
