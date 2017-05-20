package dzikizachod;

import java.util.*;

/**
 * Reprezentacja całej gry
 */
public class Disboard {
    /** Ile ckcji gracz dobiera w ciągu tury */
    protected final int LICZBA_DOBIERANYCH_AKCJI = 5;

    /** Pula akcji do gry */
    private PulaAkcji pulaAkcji;

    /** RNG do użytku własnego */
    private Random rng = new Random();

    /** Lista graczy w rozgrywce */
    private ArrayList<Gracz> gracze;

    /** Liczba żywych bandytów w rozgrywce */
    private int liczbaBandytów;

    /** Referencja do szeryfa */
    private Gracz szeryf;

    /** Obserwatorzy rozgrywki */
    private Set<IObserwator> obserwatorzy = new HashSet<>();

    /** Widoki wszystkich gracza, także martwych. Nie zmienia się w trakcie gry */
    private StrategicznyWidokGracza widokiGraczy[];

    /** Widok gracza-szeryfa */
    private StrategicznyWidokGracza widokSzeryfa;

    /** Referencja do właśnie grającego gracza. */
    private Gracz obecnyGracz;

    /** Czy dynamit jest przerzucany */
    private boolean dynamitIdzie;

    /** Widok na właśnie grającego gracza. */
    private StrategicznyWidokGracza widokObecnegoGracza;


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
        bawcieSieDobrze("");
    }

    /**
     * Wykonuje różnorakie operacje potrzebne przed przeprowadzeniem rozgrywki.
     * Przed wywołaniem, nalezy przypisac do pól obiektu kolekcje graczy.
     */
    protected void przygotujRozgrywke() {
        sprawdzTabliceGraczy();
        potasujGraczy();
        przygotujGraczy();
        przygotujWidokiGraczy();
    }

    /**
     * Tworzy widoki dla wszystkich graczy, które będą przekazywane obserwatorom gry.
     * Dba o to, aby na jednego gracza zawsze przypadał jeden widok. Każdy widok jest tworzony tak, aby udzielić pełnej
     * informacji o graczu (podglądający gracza jest graczem podglądanym).
     */
    private void przygotujWidokiGraczy() {
        widokiGraczy = new StrategicznyWidokGracza[gracze.size()];
        widokSzeryfa = null;
        for (int i = 0; i < gracze.size(); ++i) {
            Gracz g = gracze.get(i);
            widokiGraczy[i] = new StrategicznyWidokGracza(g, g);
            if (g.tozsamosc() == TozsamoscGracza.SZERYF)
                widokSzeryfa = widokiGraczy[i];
        }
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

    /**
     * Zwraca odpowiedź na pytanie ,,Czy gra toczy się dalej?''
     * @return <code>true</code> kiey gra jeszcze trwa
     */
    public boolean czyKoniecGry() {
        return liczbaBandytów != 0 && szeryf.pz() > 0;
    }

    /**
     * Rozgrywa grę w pętli.
     * X) Bawcie się dobrze
     */
    protected void bawcieSieDobrze(String kto) {
        assert kto.equals("");

        obecnyGracz = szeryf;
        int numerTury = 0;
        dynamitIdzie = false;

        //Oglos poczatek gry
        for (IObserwator o : obserwatorzy)
            o.patrzPoczatekGry(widokiGraczy, widokSzeryfa, liczbaBandytów, gracze.size() - liczbaBandytów - 1);

        while (!czyKoniecGry()) {
            if (obecnyGracz == szeryf) {
                ++numerTury;
                for (IObserwator o : obserwatorzy)
                    o.patrzKolejnaTura(numerTury);
            }
            rozegrajTureGracza();
        }

        //Oglos koniec gry
        for (IObserwator o : obserwatorzy)
            o.patrzKoniecGry(szeryf.pz() > 0);
    }

    /**
     * Fragment kodu rozgrywki odpowiedzialny za przeprowadzenie tury gracza
     */
    protected void rozegrajTureGracza() {
        widokObecnegoGracza = widokiGraczy[obecnyGracz.identyfikator()];
        for (IObserwator o : obserwatorzy)
            o.patrzRuchGracza(widokObecnegoGracza);

        dajAkcje();
        rozstrzygnijDynamit();
        obecnyGracz.graj();
        obecnyGracz = obecnyGracz.przeskocz(1);

        for (IObserwator o : obserwatorzy)
            o.patrzSkonczylTure(widokObecnegoGracza);
    }

    protected void dajAkcje() {
        for (int i = 0; i < LICZBA_DOBIERANYCH_AKCJI; ++i) {
            Akcja a = pulaAkcji.dobierz();
            for (IObserwator o : obserwatorzy)
                o.patrzDobralAkcje(widokObecnegoGracza, a);
            obecnyGracz.dobierz(a);
        }
    }

    protected void rozstrzygnijDynamit() {
        boolean kabum = rng.nextInt(6) == 0;
        StrategicznyWidokGracza w = widokiGraczy[obecnyGracz.identyfikator()];
        for (IObserwator o : obserwatorzy)
            o.patrzNaDynamit(w, kabum);

        if (kabum) {
            obecnyGracz.dodajPZ(-3);
            dynamitIdzie = false;
        }
    }

    void uruchomDynamit() {
        if (dynamitIdzie)
            throw new Error("Za dużo dynamitów");
        dynamitIdzie = true;
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
     * @param gracz nieboszczyk
     */
    void graczUmarl(Gracz gracz) {
        StrategicznyWidokGracza strasznyWidok = widokiGraczy[gracz.identyfikator()];
        for (IObserwator o : obserwatorzy)
            o.patrzZabojstwo(strasznyWidok, widokObecnegoGracza);

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

    /**
     * Ogłasza wszystkim obserwatorom wykonanie akcji przez gracza.
     * Ta metoda jest wywoływana przez gracza wykonującego akcje.
     * @param kto gracz wykonujący akcję
     * @param a wykonana akcja
     * @param naKim gracz, na którym wykonano akcję (null w przypadku dynamitu)
     */
    void oglosWykonanieAkcji(Gracz kto, Akcja a, Gracz naKim) {
        assert kto == obecnyGracz;
        StrategicznyWidokGracza wKto = widokiGraczy[kto.identyfikator()];
        StrategicznyWidokGracza wNaKim = naKim != null ? widokiGraczy[naKim.identyfikator()] : null;
        for (IObserwator o : obserwatorzy)
            o.patrzWykonalAkcje(wKto, a, wNaKim);
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
