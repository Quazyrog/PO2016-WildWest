package dzikizachod;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.util.*;

/**
 * Reprezentacja całej gry
 */
public class Gra { //TODO całość implementacji obsetwatorów
    private PulaAkcji pulaAkcji;
    private ArrayList<Gracz> gracze;
    private Random rng = new Random();
    private Set<IObserwator> obserwatorzy = new HashSet<>();
    private int liczbaBandytów;
    private Gracz szeryf;


    /**
     * Dodaje obserwatora rozgrywki.
     * @param obserwator obserwator do dodania.
     */
    public void dodajObserwatora(IObserwator obserwator) {
        if (obserwator == null)
            throw new NullPointerException();
        obserwatorzy.add(obserwator);
    }

    /**
     * Usuwa obserwatora rozgrywki.
     * @param obserwator obserwator do usunięcia.
     */
    public void usunObserwatora(IObserwator obserwator) {
        if (obserwator == null)
            throw new NullPointerException();
        obserwatorzy.remove(obserwator);
    }

    /**
     * Zwraca liczbę żywych grajacych
     * @return liczbę żywych grajacych
     */
    public int liczbaGraczy() {
        return gracze.size();
    }

    /**
     * Rozgrywa grę z podanymi graczami i pulą akcji.
     * @param gracze lista graczy
     * @param pulaAkcji pula z akcjami
     */
    public void rozgrywka(Gracz gracze[], PulaAkcji pulaAkcji) {
        rozgrywka(Arrays.asList(gracze), pulaAkcji);
    }

    /**
     * Rozgrywa grę z podanymi graczami i pulą akcji.
     * @param gracze lista graczy
     * @param pulaAkcji pula z akcjami
     */
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
        sprawdzTabliceGraczy();
        potasujGraczy();
        przygotujGraczy();
    }

    /**
     * Sprawdza tablice graczy: liczy bandytów i wyszukuje szeryfa.
     */
    protected void sprawdzTabliceGraczy() {
        szeryf = null;
        liczbaBandytów = 0;
        for (Gracz g : gracze) {
            if (g.tozsamosc() == TozsamoscGracza.SZERYF) {
                if (szeryf != null)
                    throw new IllegalArgumentException("To miasto jest za małe dla nas dwóch!");
                szeryf = g;
            } else if (g.tozsamosc() == TozsamoscGracza.BANDYTA) {
                ++liczbaBandytów;
            }
        }
        if (szeryf == null)
            throw new IllegalArgumentException("Chudy! gdzie jesteś?!");
    }

    /**
     * Miesza kolejnośc graczy w tablicy.
     */
    protected void potasujGraczy() {
        for (int i = 0; i < gracze.size(); ++i) {
            int j = rng.nextInt(gracze.size());
            Gracz tymczasowa = gracze.get(j);
            gracze.set(j, gracze.get(i));
            gracze.set(i, tymczasowa);
        }
    }

    /**
     * Dla każdego gracz wywołuje na nim <code>przygotujDoGry()</code> z odpowiednimi parametrami.
     */
    protected void przygotujGraczy() {
        gracze.get(0).przygotujDoGry(this, gracze.get(gracze.size() - 1), gracze.get(1), 0);
        for (int i = 1; i < gracze.size() - 1; ++i)
            gracze.get(i).przygotujDoGry(this, gracze.get(i - 1), gracze.get(i + 1), i);
        gracze.get(gracze.size() - 1).przygotujDoGry(this, gracze.get(gracze.size() - 2), gracze.get(0), gracze.size() - 1);
    }

    void uruchomDynamit() {
        //TODO ...
    }

    /**
     * Zwraca akcję z powrotem do puli.
     * @param akcja
     */
    void oddajAkcje(Akcja akcja) {
        pulaAkcji.odrzuc(akcja);
    }

    /**
     * Gracz informuje w ten sposób grę, że umarł.
     * @param gracz
     */
    void graczUmarl(Gracz gracz) {
        if (gracz.tozsamosc() == TozsamoscGracza.BANDYTA) {
            --liczbaBandytów;
        }
        for (int i = gracze.indexOf(gracz); i < gracze.size(); ++i) {
            Gracz g = gracze.get(i);
            g.przenumeruj(g.numer() - 1);
        }
        gracze.remove(gracz);
        //Nie ma potrzeby aktualisowania cylku formowanego przez pola poLewej i poPrawej u graczy: oni robią to sami
        //(w funkcji przeskocz)
    }

    void oglosWykonanieAkcji(Gracz kto, Akcja a, Gracz naKim) {
        //TODO ...
    }

    /**
     * Zwraca gracza o podanym numerze, spośród żyjących graczy.
     * Numery są ustalane po przetasowaniu graczy, a potem dodatkowo zmieniają się w trakcie gry.
     * @param numer numer gracza
     * @return gracza o numerze <code>numer</code>
     */
    Gracz graczONumerze(int numer) {
        return gracze.get(numer);
    }
}
