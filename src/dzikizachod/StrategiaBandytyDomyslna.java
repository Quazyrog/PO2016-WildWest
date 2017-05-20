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
    public void patrzKoniecGry(boolean czyDobroWygralo) {}

    @Override
    protected void ogarnijPoczatekGry(ReprezentacjaGracza[] gracze, ReprezentacjaGracza szeryf, int liczbaBandytów, int liczbaPomocników) {}

    @Override
    protected void ogarnijRuchGracza(ReprezentacjaGracza ktoGra) {}

    @Override
    protected void ogarnijWykonalAkcje(ReprezentacjaGracza ktoGra, Akcja a, ReprezentacjaGracza naKim) {}

    @Override
    protected void ogarnijZabojstwo(ReprezentacjaGracza ofiara, ReprezentacjaGracza zabojca) {}

    protected void najazdNaSzeryfa() throws BladKonrtoleraWyjatek {
        int doSzeryfa = odlegloscIKierunekOd(szeryf);
        if (Math.abs(doSzeryfa) <= zasieg()) {
            while (ileAkcji(Akcja.STRZEL) > 0)
                akcjaStrzel(szeryf);
        } else {
            ReprezentacjaGracza cel = losowyPomocnikWZasieguNaLuku(doSzeryfa);
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
            while (ileAkcji(Akcja.STRZEL) > 0)
                najazdNaSzeryfa();
        } catch (NieDaSieNicZrobicWyjatek ignored) {
        } catch (BladKonrtoleraWyjatek e) {
            e.printStackTrace();
        }

        if (ileAkcji(Akcja.DYNAMIT) > 0 && odlegloscSkierowanOd(1, szeryf) < 3)
            akcjaDynamit();
    }
}
