package dzikizachod;

import java.util.Random;

/**
 * Reprezentacja całej gry
 */
public class Gra {
    private PulaAkcji pulaAkcji;
    private Gracz gracze[];
    private Random rng = new Random();

    public int liczbaGrczy() {
        return gracze.length;
    }

    public void rozgrywka(Gracz gracze[], PulaAkcji pulaAkcji) {
        if (pulaAkcji == null)
            throw new NullPointerException();
        if (gracze == null)
            throw new NullPointerException();
        this.gracze = gracze;
        this.pulaAkcji = pulaAkcji;
    }

    public boolean czyKoniecGry() {
        return false; //todo ...
    }

    protected void przygotujRozgrywke() {
        //Potasuj graczy
        for (int i = 0; i < gracze.length; ++i) {
            int j = rng.nextInt(gracze.length);
            Gracz tymczasowa = gracze[j];
            gracze[j] = gracze[i];
            gracze[i] = tymczasowa;
        }

        //Inicjuj graczy
        gracze[0].przygotujDoGry(this, gracze[gracze.length - 1], gracze[1], 0);
        for (int i = 1; i < gracze.length - 1; ++i)
            gracze[i].przygotujDoGry(this, gracze[i - 1], gracze[i + 1], i);
        gracze[gracze.length - 1].przygotujDoGry(this, gracze[gracze.length - 2], gracze[0], gracze.length - 1);
    }

    void uruchomDynamit() {
        //TODO ...
    }

    void oddajAkcje(Akcja akcja) {
        //TODO ...
    }

    void graczUmarl(Gracz gracz) {
        //TODO
    }

    Gracz graczONumerze(int numer) {
        return gracze[numer];
    }
}
