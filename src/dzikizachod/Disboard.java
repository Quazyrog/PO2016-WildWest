package dzikizachod;

import java.util.*;

/**
 * Reprezentacja całej gry; i to jest dobre miejsce, żeby opisać jak generalnie gra jest zorganizowana.
 *
 * Disboard jest całym światem gry i klasa odpowiedzialną za jej przeprowadzenie. W imię separacji UI i logiki,
 * Disboard sam z siebie nic nie wypisuje. Potrafi natomiast przekazywać informacje o przebiegu gry do obiektów
 * implementujących <code>IObserwator</code>. Wypisywacz, który implementuje ten interfejs jest odpowiedzialny za
 * wypisywanie komunikatów o przebiegu gry. Strategie równiez go implementują i w ten sposób same śledzą przebieg gry.
 *
 * Disboard tylko przeprowadza grę, ale to gracze sami wykonują swoje działania. W związku z tym, na ogół akcje takie
 * jak strzelanie nie angażują Disboardu (poza faktem konieczności rozgłoszenia komunikatu).
 *
 * Jest jeszcze jedna ważna kwestia. W celu ułatwienia debugowania można większości z klas w pakiecie przy inicjalizacji
 * podać instancję zmiennej random, którą mają wykorzystywać. To pozwala na odtworzenie wielokrotnie takiego zamego
 * przebiegu gry.
 *
 * Disboard jest areną wielu wydarzeń, tak więc pojedynczą instancję tej klasy można wykorzystac do rozegrania wielu
 * gier, ale nie równocześnie.
 *
 * @see IObserwator
 * @see Wypisywacz
 * @see Gracz
 */
public class Disboard {
    /** Ile ckcji gracz dobiera w ciągu tury */
    static protected final int LICZBA_DOBIERANYCH_AKCJI = 5;

    /** Pula akcji do gry */
    private PulaAkcji pulaAkcji;

    /** RNG do użytku własnego */
    private Random rng;

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

    /** Numer obecnej tury */
    private int numerTury;

    /** Referencja do właśnie grającego gracza. */
    private Gracz obecnyGracz;

    /** Czy dynamit jest przerzucany */
    private boolean dynamitIdzie;

    /** Widok na właśnie grającego gracza. */
    private StrategicznyWidokGracza widokObecnegoGracza;

    /** Lista obserwatorów dodanych przez graczy, które musza być usunięte po rozgrywce */
    ArrayList<IObserwator> filtryObserwatorowStrategii = new ArrayList<>();


    /**
     * Stwarza Disboard, który korzysta z podanego generatora liczb losowych
     * @param rng generator liczb losowych do wykorzystania
     */
    public Disboard(Random rng) {
        this.rng = rng;
    }


    /**
     * Dodaje obserwatora rozgrywki.
     * Obserwator będzie otrzymywał komunikaty o jej przebiegu. Raz dodany, może będzie śledzić wiele rozgrywek tak
     * długo, aż zostanie usunięty. Strategie graczy są automatycznie dodawane na początku rozgrywki i usuwane
     * po jej zakończeniu i nie należy ich dodawac przy pomocy tej funkcji.
     * @param obserwator obserwator do dodania.
     * @see Disboard#usunObserwatora(IObserwator)
     */
    public void dodajObserwatora(IObserwator obserwator) {
        if (obserwator == null)
            throw new NullPointerException();
        obserwatorzy.add(obserwator);
    }


    /**
     * Usuwa obserwatora rozgrywki.
     * Nie będzie już utrzymywac komunikatów o jej przebiegu. Jeśli nie był wcześnie dodany, to nic się ni stanie.
     * @param obserwator obserwator do usunięcia.
     * @see Disboard#usunObserwatora(IObserwator)
     */
    public void usunObserwatora(IObserwator obserwator) {
        if (obserwator == null)
            throw new NullPointerException();
        obserwatorzy.remove(obserwator);
    }


    /**
     * Zwraca liczbę żywych graczy
     * Zwraca liczbę żywych graczy biorących udział w toczącej się rozgrywce.
     * @return liczbę żywych grajacych
     */
    public int liczbaGraczy() {
        if (gracze == null)
            throw new IllegalStateException("Obecnie w Świecie Disboardu nie dzieje się nic ciekawego (gra się nie toczy)");
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
    public void rozgrywka(List<Gracz> gracze, PulaAkcji pulaAkcji) {
        if (pulaAkcji == null)
            throw new NullPointerException();
        if (gracze == null)
            throw new NullPointerException();

        this.gracze = new ArrayList<Gracz>(gracze.size());
        this.gracze.addAll(gracze);
        this.pulaAkcji = pulaAkcji;

        przygotujRozgrywke();
        bawcieSieDobrze("");
        zakonczRozgrywke();
        for (Gracz g : gracze)
            g.skonczGrac();
    }


    /**
     * Wykonuje różnorakie operacje potrzebne przed przeprowadzeniem rozgrywki.
     * Przed wywołaniem, nalezy przypisac do pól obiektu kolekcje graczy.
     */
    protected void przygotujRozgrywke() {
        pulaAkcji.przywrocPoczatkowe();
        sprawdzTabliceGraczy();
        potasujGraczy();
        przygotujGraczy();
        przygotujWidokiGraczy();
    }


    /**
     * Tworzy widoki dla wszystkich graczy, które będą przekazywane obserwatorom gry.
     * Dba o to, aby na jednego gracza zawsze przypadał jeden widok. Każdy widok jest tworzony tak, aby udzielić pełnej
     * informacji o graczu (podglądający gracza jest graczem podglądanym).
     * Należy jednak pamiętać, że strategie korzystają z własnych widoków o ograniczonych uprawnieniach.
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
     * W związku z tym musi być wywołane po tasowaniu graczy.
     */
    protected void przygotujGraczy() {
        gracze.get(0).przygotujDoGry(this, gracze.get(gracze.size() - 1), gracze.get(1), 0);
        for (int i = 1; i < gracze.size() - 1; ++i)
            gracze.get(i).przygotujDoGry(this, gracze.get(i - 1), gracze.get(i + 1), i);
        gracze.get(gracze.size() - 1).przygotujDoGry(this, gracze.get(gracze.size() - 2), gracze.get(0), gracze.size() - 1);

        for (Gracz g : gracze) {
            IObserwator filtr = new FiltrObserwatora(g.startegia(), g, gracze);
            filtryObserwatorowStrategii.add(filtr);
            dodajObserwatora(filtr);
        }
    }


    /**
     * Zwraca odpowiedź na pytanie ,,Czy gra już się zakpńczyła''
     * Gra jest uznana za zakończoną, kiedy (zachodzi dowolny):
     *   - żadna gra sie nie toczy
     *   - wszyscy bandyci nie żyją
     *   - szeryf nie żyje
     *   - dobiegła końca 42 runda
     * @return <code>false</code> kiey gra jeszcze trwa
     */
    public boolean czyKoniecGry() {
        if (gracze == null)
            return true;
        return liczbaBandytów == 0 || szeryf.pz() == 0 || numerTury > 42;
    }


    /**
     * Rozgrywa grę w pętli.
     * Musi być naturalnie wywołane po całości funkcji inicjalizujących.
     * X) Bawcie się dobrze
     */
    protected void bawcieSieDobrze(String kto) {
        assert kto.equals("");

        obecnyGracz = szeryf;
        numerTury = 0;
        dynamitIdzie = false;

        //Oglos poczatek gry
        for (IObserwator o : obserwatorzy)
            o.patrzPoczatekGry(widokiGraczy, widokSzeryfa, liczbaBandytów, gracze.size() - liczbaBandytów - 1);

        while (!czyKoniecGry()) {
            if (obecnyGracz == szeryf) {
                ++numerTury;
                if (numerTury == 43)
                    break;
                for (IObserwator o : obserwatorzy)
                    o.patrzKolejnaTura(numerTury);
            }
            rozegrajTureGracza();
        }

        //Oglos koniec gry
        Zakonczenie zakonczenie;
        if (numerTury == 43)
            zakonczenie = Zakonczenie.REMIS;
        else if (szeryf.pz() > 0)
            zakonczenie = Zakonczenie.DOBRO_WYGRALO;
        else
            zakonczenie = Zakonczenie.ZLO_WYGRALO;
        for (IObserwator o : obserwatorzy)
            o.patrzKoniecGry(zakonczenie);
    }


    /**
     * Fragment kodu rozgrywki odpowiedzialny za przeprowadzenie tury gracza.
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


    /**
     * Sprawdza, czy dynamit przechodzi przez grę i jeśli tak, to rozstrzyga jego efekt.
     * Choć pozbycie się tego ograniczenia byłoby raczej proste, to może być tylko jeden dynamit w grze.
     */
    protected void rozstrzygnijDynamit() {
        if (!dynamitIdzie)
            return;

        boolean kabum = rng.nextInt(6) == 0;
        StrategicznyWidokGracza w = widokiGraczy[obecnyGracz.identyfikator()];
        for (IObserwator o : obserwatorzy)
            o.patrzNaDynamit(w, kabum);

        if (kabum) {
            try {
                obecnyGracz.dodajPZ(-3, null);
            } catch (BladKonrtoleraWyjatek e) {
                throw new Error("Obsługuję trupa!", e);
            }
            dynamitIdzie = false;
        }
    }


    /**
     * Wywołane po zakończeniu gry.
     * Informuje wszystkich graczy o zakończeniu gry. Resetuje stan Disboardu do tego sprzed rozpoczęcia gry
     * (oprócz RNG rzecz jasna).
     */
    protected void zakonczRozgrywke() {
        for (IObserwator o : filtryObserwatorowStrategii)
            usunObserwatora(o);

        dynamitIdzie = false;
        gracze = null;
        liczbaBandytów = 0;
        numerTury = 0;
        obecnyGracz = null;
        pulaAkcji = null;
        szeryf = null;
        widokiGraczy = null;
        widokObecnegoGracza = null;
        widokSzeryfa = null;
        filtryObserwatorowStrategii.clear();
    }


    /**
     * Rozpoczyna przerzucanie dynamitu.
     */
    void uruchomDynamit() {
        if (dynamitIdzie)
            throw new Error("Za dużo dynamitów");
        dynamitIdzie = true;
    }


    /**
     * Zwraca akcję z powrotem do puli.
     * @param akcja akcja do odłożenia do puli
     */
    void oddajAkcje(Akcja akcja) {
        pulaAkcji.odrzuc(akcja);
    }


    /**
     * Gracz informuje w ten sposób grę, że umarł.
     * Gra dalej informuje o tym wszystkie strategie i zmniejsza pulkę graczy grających. Zmniejsza także ich numery (ale
     * nie identyfikatory), aby po usunięciu martwego gracza dalej odpowiadały ich indeksom w tablicy graczy.
     * @param gracz nieboszczyk
     */
    void graczUmarl(Gracz gracz, Gracz zrodloAtaku) {
        StrategicznyWidokGracza strasznyWidok = widokiGraczy[gracz.identyfikator()];
        StrategicznyWidokGracza kira = null;
        if (zrodloAtaku != null)
            kira = widokiGraczy[zrodloAtaku.identyfikator()];

        for (IObserwator o : obserwatorzy)
            o.patrzZabojstwo(strasznyWidok, kira);

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
