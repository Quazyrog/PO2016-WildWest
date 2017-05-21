package dzikizachod;


public class StrategiaSzeryfaDomyslna extends StrategiaSzeryfa {
    @Override
    public void patrzKolejnaTura(int numerTury) {}

    @Override
    public void patrzKoniecGry(Zakonczenie zakonczenie) {}

    @Override
    public void patrzRuchGracza(StrategicznyWidokGracza ktoGra) {}

    @Override
    public void patrzDobralAkcje(StrategicznyWidokGracza ktoGra, Akcja a) {}

    @Override
    public void patrzNaDynamit(StrategicznyWidokGracza ktoGra, boolean wybuchl) {}

    @Override
    public void patrzSkonczylTure(StrategicznyWidokGracza ktoGra) {}

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
