package dzikizachod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Reprezentacja całej gry
 */
public class Gra { //TODO całość implementacji obsetwatorów
    private PulaAkcji pulaAkcji;
    private ArrayList<Gracz> gracze;
    private Random rng = new Random();
    int liczbaBandytów;
    Gracz szeryf;

    /**
     * Zwraca liczbę żywych grajacych
     * @return liczbę żywych grajacych
     */
    public int liczbaGraczy() {
        return gracze.size();
    }

    public void rozgrywka(Gracz gracze[], PulaAkcji pulaAkcji) {
        rozgrywka(Arrays.asList(gracze), pulaAkcji);
    }

    public void rozgrywka(List gracze, PulaAkcji pulaAkcji) {
        if (pulaAkcji == null)
            throw new NullPointerException();
        if (gracze == null)
            throw new NullPointerException();
        this.gracze = new ArrayList<Gracz>(gracze.size());
        this.gracze.addAll(gracze);
        this.pulaAkcji = pulaAkcji;

        przygotujRozgrywke();
    }

    /**
     * Zwraca odpowiedź na pytanie ,,Czy gra toczy się dalej?''
     * @return <code>true</code> kiey gra jeszcze trwa
     */
    public boolean czyKoniecGry() {
        return liczbaBandytów != 0 && szeryf.pz() > 0;
    }

    /**
     * Wykonuje różnorakie operacje potrzebne przed przeprowadzeniem rozgrywki.
     * Przed wywołaniem, nalezy przypisac do pól obiektu kolekcje graczy.
     */
    protected void przygotujRozgrywke() {
        //Znajdź szeryfa i policz bandytów
        szeryf = null;
        liczbaBandytów = 0;
        for (Gracz g : gracze) {
            if (g.tozsamosc() == TozsamoscGracza.SZERYF) {
                if (szeryf != null)
                    throw new IllegalArgumentException("Może być tylko jeden szeryf wśród graczy");
                szeryf = null;
            } else if (g.tozsamosc() == TozsamoscGracza.BANDYTA) {
                ++liczbaBandytów;
            }
        }

        //Potasuj graczy
        for (int i = 0; i < gracze.size(); ++i) {
            int j = rng.nextInt(gracze.size());
            Gracz tymczasowa = gracze.get(j);
            gracze.set(j, gracze.get(i));
            gracze.set(i, tymczasowa);
        }

        //Inicjuj graczy
        gracze.get(0).przygotujDoGry(this, gracze.get(gracze.size() - 1), gracze.get(1), 0);
        for (int i = 1; i < gracze.size() - 1; ++i)
            gracze.get(i).przygotujDoGry(this, gracze.get(i - 1), gracze.get(i + 1), i);
        gracze.get(gracze.size() - 1).przygotujDoGry(this, gracze.get(gracze.size() - 2), gracze.get(0), gracze.size() - 1);
    }

    void uruchomDynamit() {
        //TODO ...
    }

    void oddajAkcje(Akcja akcja) {
        pulaAkcji.odrzuc(akcja);
    }

    void graczUmarl(Gracz gracz) {
        if (gracz.tozsamosc() == TozsamoscGracza.BANDYTA) {
            --liczbaBandytów;
        }
        gracze.remove(gracz);
        //Nie ma potrzeby aktualisowania cylku formowanego przez pola poLewej i poPrawej u graczy: oni robią to sami
        //(w funkcji przeskocz)
    }

    void oglosWykonanieAkcji(Gracz kto, Akcja a, Gracz naKim) {
        //TODO ...
    }

    Gracz graczONumerze(int numer) {
        return gracze.get(numer);
    }
}
