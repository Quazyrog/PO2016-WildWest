package dzikizachod;

import java.util.Random;

/**
 * Abstrakcyjna klasa reprezętująca gracza.
 * Wchodzi w interakcje z innymi graczami, bez pomocy disboardu i generalnie jest od niego zależny tylko podczas gry.
 * Ma dużo metod ostatecznych, aby nie dało się zrobić oszukiwanych graczy.
 */
public abstract class Gracz {
    /** RNG używany do rozdawania losowych ograniczeń na HP */
    private static Random rng = new Random();

    /** Limit punktów życia gracza */
    private int limitPZ;

    /** Obecna liczba punktów życia gracza */
    private int pz;

    /** Zasięg gracza */
    private int zasieg = 1;

    /** Strategia kontrolująca gracza */
    private Strategia kontroler;

    /** Gracze po lewej i prawej stronie w rozgrywce (odpowiednio poprzedni i kolejny gracz) */
    private Gracz poLewej, poPrawej;

    /** Gra, w której aktualnie bierze udział gracz */
    private Disboard gra;

    /** Arsenał gracza */
    private LicznikAkcji akcje = new LicznikAkcji();

    /** Pierwszy numer nadany graczowi, który nie zmienia się podczas rozgrywki */
    private int identyfikator;

    /** Numer gracza w toczonej rozgrywce. Zmienia się wraz z zabijaniem kolejnych graczy. */
    private int numer;

    /** Ustawiane na <code>true</code>, gdy gracz wykonuje ruch i na <code>false</code>, gdy już skończy */
    private boolean wykonujeRuch;


    public static void nasiono(long n) {
        rng = new Random(n);
    }


    /**
     * Inicjuje gracza opodana strategią oraz ustawia mu liczbę punktów życia
     * @param kontroler strategia kontrolujaca
     * @param pz maksymalne punkty życia (tyle samo będzie początkowych)
     */
    public Gracz(Strategia kontroler, int pz) {
        this.limitPZ = pz;
        this.pz = pz;
        this.kontroler = kontroler;
        kontroler.przypiszGracza(this);
    }


    /**
     * Inicjuje gracza opodana strategią oraz ustawia mu losowo 3 lub 4 PZ
     * @param kontroler strategia kontrolujaca
     */
    public Gracz(Strategia kontroler) {
        this(kontroler, 3 + rng.nextInt(2));
    }


    /**
     * W zależności od znaku kierunku zwraca gracza po lewej lub prawej od podanego.
     * Dziki Zachód jest miejscem, w którym panuje orientacja dodatnia, więc naturalnym kierunkiem gry jest kierunek
     * przeciwny do ruchu wskazówek zegara. Wobec tego kierunek ujemny oznacza gracza po lewej, a dodatni po prawej.
     * ta funkcja także zaktualizuje danego sąsiada (lewego lub prawego w zależnosci od kierunku), jeżeli poprzedni
     * sąsiad już nie żyje.
     * @param wKierunku kierunek, oznaczający lewego sąsiada (kierunek ujemny) lub prawego (dodatni); nie może być 0
     * @return <c>odGracza.poLewej</c> gdy <c>kierunek < 0</c>; <c>odGracza.poPrawej</c> gdy <c>kierunek > 0</c>
     */
    final public Gracz przeskocz(int wKierunku) {
        if (wKierunku == 0) {
            throw new IllegalArgumentException();
        }
        if (wKierunku < 0) {
            if (poLewej.pz() == 0)
                poLewej = poLewej.przeskocz(-1);
            return poLewej;
        }
        if (wKierunku > 0) {
            if (poPrawej.pz() == 0)
                poPrawej = poPrawej.przeskocz(1);
            return poPrawej;
        }
        throw new Error("To się nie powinno zdażyć");
    }


    /**
     * —Kim jesteś? —Jestem wajsem.
     * @return zwraca prawdziwą tożsamość gracza
     */
    public abstract TozsamoscGracza tozsamosc();


    /**
     * Zmienia punkty życia gracza o podana liczbę.
     * Punkty życia nie spadną do wartości mniejszej niż 0. W wypadku śmierci gracza, zostanie o tym powiadomiony
     * Disboard, a akcje gracza zwrócone do puli. Nie można tego wywołać, gdy gra się nie toczy.
     * @param delta liczba dodana do punktów życia
     * @param zrodloAtaku gracz, który uleczyl lub zaatakował; <code>null</code> w przypadku dynamitu
     * @throws NieInteresujSieTrupemWyjatek kiedy ten gracz jest już martwy
     */
    final public void dodajPZ(int delta, Gracz zrodloAtaku) throws BladKonrtoleraWyjatek {
        if (czyKoniecGry())
            throw new IllegalStateException("Gra musi się toczyć, aby modyfikowac PZ");
        if (pz == 0)
            throw new NieInteresujSieTrupemWyjatek();
        pz = Math.max(0, Math.min(limitPZ, pz + delta));
        if (pz == 0)
            umieram(zrodloAtaku);
    }


    /**
     * Zwraca obecną liczbę punktów życia.
     * @return liczbę punktów zycia
     */
    final public int pz() {
        return pz;
    }


    /**
     * Zwraca liczbę maksymalnych punktów życia.
     * @return maksymalną liczbę punktów życia
     */
    final public int maksymalnePZ() {
        return limitPZ;
    }


    /**
     * Zwraca zasięg gracza
     * @return zasięg gracza
     */
    final public int zasieg() {
        return zasieg;
    }


    /**
     * Zwraca identyfikator gracza.
     * Jest to stały numer, nadany na początku rozgrywki.
     * @return identyfikator gracza
     */
    final public int identyfikator() {
        return identyfikator;
    }


    /**
     * Zwraca numer gracza w toczącej się rozgrywce lub -1.
     * @return -1 gdy gracz nie gra, a jego numer w grze w przeciwnym wypadku
     */
    final public int numer() {
        return numer;
    }


    /**
     * Delegacja <code>czyKoniecGry</code> z klasy gry
     * @return <code>true</code>, kiedy któraś ze stron już wygrała; <code>false</code> w.p.p.
     */
    final public boolean czyKoniecGry() {
        return gra.czyKoniecGry();
    }


    /** Zwraca liczbę żywych graczy biorących udział w rozgrywce */
    final public int liczbaGraczy() {
        return gra.liczbaGraczy();
    }


    /**
     * Zwraca odpowiedź na pytanie ,,Czy gracz wykonuje teraz ruch?''
     * Kiedy gra zakończy się w trakcie tury gracza, to gracz nie wykonuje już ruchów po jej zakończeniu.
     * @return <code>true</code> podczas ruchu tego gracza; <code>false</code> poza ruchem
     */
    final public boolean czyWykonujeRuch() {
        return wykonujeRuch && !czyKoniecGry();
    }


    /**
     * Zwraca gracza znajdującego się we wskazanej odległości na prawo lub lewo od tego gracza.
     * Dodatnia wartość parametru oznacza gracza znajdującego się <code>odlegloscSkierowana</code> pozycji na prawo.
     * Ujemna wartość tego parametru oznacza gracza <code>-odlegloscSkierowana</code> na lewo. Wartosć bezwzględna
     * parametru musi być mniejsza niż liczba żywych graczy.
     * @param odlegloscSkierowana liczba, której moduł określa odległość poszukiwanego gracza, a znak kierunek jej
     *                            mierzenia (ujemny -> na lewo; dodatni -> na prawo). Zerowy oznacza tego gracza.
     * @return gracza znajdującego się we wskazanej odległości na prawo lub lewo od tego gracza
     */
    final public Gracz dalekiSasiad(int odlegloscSkierowana) throws NieInteresujSieTrupemWyjatek {
        if (pz == 0)
            throw new NieInteresujSieTrupemWyjatek();
        if (Math.abs(odlegloscSkierowana) >= gra.liczbaGraczy())
            throw new ArrayIndexOutOfBoundsException();
        return gra.graczONumerze((gra.liczbaGraczy() + odlegloscSkierowana + numer) % gra.liczbaGraczy());
    }


    /**
     * Mierzy odległość pomiędzy tym graczem, a wskazanym w parametrze.
     * @param gracz gracz, do którego mierzymy odległość. Musi być żywy i brać udział w tej samej rozgrywce.
     * @return odległość tego gracza od podanego
     */
    final public int odlegloscOd(Gracz gracz) throws NieInteresujSieTrupemWyjatek {
        return Math.abs(odlegloscIKierunekOd(gracz));
    }


    /**
     * Mierzy odległość pomiędzy tym graczem, a wskazanym w parametrze; zwraca dodatkowo informację o kierunku
     * @param gracz gracz, do którego mierzymy odległość. Musi być żywy i brać udział w tej samej rozgrywce.
     * @return liczbę całkowita o module równym odległości do wskazanego gracza, której znak zależy od kierunku jej
     * mierzenia (dodatni -- zgodnie z kierunkiem gry, ujemny -- przeciwnie do niefgo)
     */
    final public int odlegloscIKierunekOd(Gracz gracz) throws NieInteresujSieTrupemWyjatek {
        int przeciwna = odlegloscSkierowanOd(-1, gracz);
        int zgodna = odlegloscSkierowanOd(1, gracz);
        if (zgodna <= przeciwna)
            return zgodna;
        return -przeciwna;
    }


    /**
     * Mierzy odległosć w danym kierunku do podanego gracza.
     * @param kierunek dodatni oznacza odległośc na peawo, ujemny w lewo (nie może być 0)
     * @param gracz gracz, do którego mierzymy odległość
     * @return odległość od gracza mierząoną we wskazanym kierunku
     */
    final public int odlegloscSkierowanOd(int kierunek, Gracz gracz) throws NieInteresujSieTrupemWyjatek {
        if (pz == 0 || gracz.pz() == 0)
            throw new NieInteresujSieTrupemWyjatek();
        if (kierunek == 0)
            throw new IllegalArgumentException();
        if (gracz == this)
            return 0;
        else
            assert gracz.numer != this.numer;
        if (numer < gracz.numer) {
            int zgodnaOdleglosc = gracz.numer - numer;
            if (kierunek > 0)
                return zgodnaOdleglosc;
            return gra.liczbaGraczy() - zgodnaOdleglosc;
        }
        return gracz.odlegloscSkierowanOd(-kierunek, this);
    }


    /**
     * Wykonaj akcję leczenia na celu o ile to możliwe.
     * Przyjmuje się, że jest możliwe, nawet jeśli cel ma maksymalną liczbę punktóœ życia.
     * @param cel gracz do uleczania (teg lub sąsiad)
     * @throws PozaZasiegiemWyjatek kiedy cel jest poza zasięgiem leczenia
     * @throws BrakAkcjiWyjatek kiedy gracz nie ma tej akcji na ręce
     * @throws NieTwojRochWyjatek kiedy ten gracz nie może się teraz ruszyć
     * @throws NieInteresujSieTrupemWyjatek kiedy akcja miałaby wykonac się na martwum graczu
     */
    final public void akcjaUlecz(Gracz cel) throws BladKonrtoleraWyjatek {
        if (cel != this && cel != przeskocz(-1) && cel != przeskocz(1))
            throw new PozaZasiegiemWyjatek();
        if (!czyWykonujeRuch())
            throw new NieTwojRochWyjatek();
        odrzucAkcje(Akcja.ULECZ);
        gra.oglosWykonanieAkcji(this, Akcja.ULECZ, cel);
        cel.dodajPZ(1, this);
    }


    /**
     * Strzel do gracza, o ile to możliwe.
     * @param cel gracz, do którego mierzymy
     * @throws PozaZasiegiemWyjatek kiedy cel znajduje się poza zasięgiem
     * @throws BrakAkcjiWyjatek kiedy gracz nie ma tej akcji na ręce
     * @throws NieTwojRochWyjatek kiedy ten gracz nie może się teraz ruszyć
     * @throws NieInteresujSieTrupemWyjatek kiedy strzelisz do trupa
     */
    final public void akcjaStrzel(Gracz cel) throws BladKonrtoleraWyjatek {
        if (odlegloscOd(cel) > zasieg)
            throw new PozaZasiegiemWyjatek();
        if (!czyWykonujeRuch())
            throw new NieTwojRochWyjatek();
        odrzucAkcje(Akcja.STRZEL);
        gra.oglosWykonanieAkcji(this, Akcja.STRZEL, cel);
        cel.dodajPZ(-1, this);
    }


    /**
     * Wykorzystuje akcję zwiększenia zasięgu.
     * @throws BrakAkcjiWyjatek kied gracz nie ma tej akcji
     * @throws NieTwojRochWyjatek kiedy ten gracz nie może się teraz ruszyć
     */
    final public void akcjaZasiegPlusJeden() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        if (!czyWykonujeRuch())
            throw new NieTwojRochWyjatek();
        odrzucAkcje(Akcja.ZASIEG_PLUS_JEDEN);
        gra.oglosWykonanieAkcji(this, Akcja.ZASIEG_PLUS_JEDEN, this);
        zasieg += 1;
    }


    /**
     * Wykorzystuje akcję zwiększenia zasięgu o dwa.
     * @throws BrakAkcjiWyjatek gdy nie ma akcji
     * @throws NieTwojRochWyjatek kiedy ten gracz nie może się teraz ruszyć
     */
    final public void akcjaZasiegPlusDwa() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        if (!czyWykonujeRuch())
            throw new NieTwojRochWyjatek();
        odrzucAkcje(Akcja.ZASIEG_PLUS_DWA);
        gra.oglosWykonanieAkcji(this, Akcja.ZASIEG_PLUS_DWA, this);
        zasieg += 2;
    }


    /**
     * Wykorzystuje akcję rzucenia dynamitu
     * @throws BrakAkcjiWyjatek ..
     * @throws NieTwojRochWyjatek kiedy ten gracz nie może się teraz ruszyć.
     */
    final public void akcjaDynamit() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        if (!czyWykonujeRuch())
            throw new NieTwojRochWyjatek();
        odrzucAkcje(Akcja.DYNAMIT);
        gra.oglosWykonanieAkcji(this, Akcja.DYNAMIT, null);
        gra.uruchomDynamit();
    }


    /**
     * Zwraca liczbę akcji danego typu, jaki gracz ma do wykorzystania
     * @param akcja typ akcji, o którą pytamy
     * @return liczbę akcji typu <code>akcja</code>
     */
    final public int ileAkcji(Akcja akcja) {
        return akcje.ileTypu(akcja);
    }


    /**
     * Wywoływane, gdy gracz umiera.
     * Oddaje akcje do puli i ogłasza tę tragedię w Disboardzie.
     * @param zrodloAtaku
     */
    protected void umieram(Gracz zrodloAtaku) {
        gra.graczUmarl(this, zrodloAtaku);
        oddajWszystkieAkcje();
    }


    /**
     * Zwraca strategię kontrolującą gracza.
     * @return strategię kontrolującą gracza
     */
    Strategia startegia() {
        return kontroler;
    }


    /**
     * Wykonuje ruch gracza.
     * Na ten czas kontrolę przejmuje sterująca strategia. Jeżeli popełni błąd, który spowoduje rzucenie wyjątku
     * <code>{@link BladKonrtoleraWyjatek}</code>, to zostanie on tu złapany, a ruch gracza uzanny za wykonany.
     */
    void graj() {
        try {
            wykonujeRuch = true;
            kontroler.graj();
        } catch (BladKonrtoleraWyjatek e) {
            //Jeśli koniec gry, to można odpuścić, bo strategie tego nie sprawdzają
            if (!czyKoniecGry()) {
                System.err.println("gugu");
                e.printStackTrace();
            }
        } finally {
            wykonujeRuch = false;
        }
    }


    /**
     * Przypisuje gracza do rozgrywki.
     * @param gra obiekt przeprowadzajacyy rozgrywkę
     * @param obokZLewej gracz po lewej (poprzedni gracz)
     * @param obokZPrawej gracz po prawej (następny gracz)
     */
    void przygotujDoGry(Disboard gra, Gracz obokZLewej, Gracz obokZPrawej, int numer) {
        assert obokZLewej != null;
        assert obokZPrawej != null;
        this.poLewej = obokZLewej;
        this.poPrawej = obokZPrawej;
        this.gra = gra;
        this.numer = numer;
        this.identyfikator = numer;
        pz = limitPZ;
    }


    /**
     * Gracz opuszcza rozgrywkę.
     */
    void skonczGrac() {
        poLewej = null;
        poPrawej = null;
        gra = null;
        numer = -1;
    }


    /**
     * Daje graczowi akcje na reke.
     * @param coDobral akacja do dobrania
     */
    void dobierz(Akcja coDobral) {
        akcje.dodaj(coDobral, 1);
    }


    /**
     * Zmienia numer gracza
     * Numer gracza odpowiada jego indeksowy w tablicy w Disboardzie i jest używany do obliczania odległości
     * i dalekich sąsiadów
     * @param nowyNumer nowy numer gracza
     */
    void przenumeruj(int nowyNumer) {
        this.numer = nowyNumer;
    }


    /**
     * Odrzuca akcję z ręki (i ile tam jest) z powrotem na stos ściepów.
     * @param akcja akcja do odzrucenia
     * @throws BrakAkcjiWyjatek kiedy nie ma akcji na ręce
     */
    private void odrzucAkcje(Akcja akcja) throws BrakAkcjiWyjatek {
        if (akcje.zabierz(akcja) < 1)
            throw new BrakAkcjiWyjatek();
        gra.oddajAkcje(akcja);
    }


    /**
     * Odrzuca wszystkie akcjie, jakie gracz miał na ręce.
     */
    protected void oddajWszystkieAkcje() {
        for (Akcja a : Akcja.values()) {
            try {
                while (ileAkcji(a) > 0)
                    odrzucAkcje(a);
            } catch (BrakAkcjiWyjatek e) {
                e.printStackTrace();
            }
        }
    }

}
