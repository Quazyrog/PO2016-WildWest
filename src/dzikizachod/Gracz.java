package dzikizachod;

import java.util.Random;

/**
 * Abstrakcyjna klasa reprezętująca gracza.
 */
public abstract class Gracz {
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
    private Gra gra;

    /** Arsenał gracza */
    private LicznikAkcji akcje = new LicznikAkcji();

    /** Numer gracza w toczonej rozgrywce */
    private int numer;

    /** Ustawiane na <code>true</code>, gdy gracz wykonuje ruch i na <code>false</code>, gdy już skończy */
    private boolean wykonujeRuch;

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
     * W zależności od znaku kierunku zwraca gracza po lewej lub prawej od podanego.
     * Dziki Zachód jest miejscem, w którym panuje orientacja dodatnia, więc naturalnym kierunkiem gry jest kierunek
     * przeciwny do ruchu wskazówek zegara. Wobec tego kierunek ujemny oznacza gracza po lewej, a dodatni po prawej.
     * @param wKierunku kierunek, oznaczający lewego sąsiada (kierunek ujemny) lub prawego (dodatni); nie może być 0
     * @return <c>odGracza.poLewej</c> gdy <c>kierunek < 0</c>; <c>odGracza.poPrawej</c> gdy <c>kierunek > 0</c>
     */
    public Gracz przeskocz(int wKierunku) {
        assert wKierunku != 0;
        if (wKierunku < 0)
            return poLewej;
        if (wKierunku > 0)
            return poPrawej;
        throw new Error("To się nie powinno zdażyć");
    }

    /**
     * Inicjuje gracza opodana strategią oraz ustawia mu losowo 3 lub 4 PZ
     * @param kontroler strategia kontrolujaca
     */
    public Gracz(Strategia kontroler) {
        this(kontroler, 3 + rng.nextInt(1));
    }

    /**
     * —Kim jesteś? —Jestem wajsem.
     * @return zwraca prawdziwą tożsamość gracza
     */
    public abstract TozsamoscGracza tozsamosc();

    /**
     * Zmienia punkty życia gracza o podana liczbę.
     * Punkty życia nie spadną do wartości mniejszej niż 0.
     * @param delta liczba dodana do punktów życia
     */
    public void dodajPZ(int delta) {
        pz = Math.max(0, Math.min(limitPZ, pz + delta));
        if (pz == 0) {
            gra.graczUmarl(this);
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

    /**
     * Zwraca obecną liczbę punktów życia.
     * @return liczbę punktów zycia
     */
    public int pz() {
        return pz;
    }

    /**
     * Zwraca liczbę maksymalnych punktów życia.
     * @return maksymalną liczbę punktów życia
     */
    public int maksymalnePZ() {
        return limitPZ;
    }

    /**
     * Zwraca zasięg gracza
     * @return zasięg gracza
     */
    public int zasieg() {
        return zasieg;
    }

    /**
     * Zwraca numer gracza w toczącej się rozgrywce lub -1.
     * @return -1 gdy gracz nie gra, a jego numer w grze w przeciwnym wypadku
     */
    public int numer() {
        return numer;
    }

    /**
     * Zwraca odpowiedź na pytanie ,,Czy gracz wykonuje teraz ruch?''
     * @return <code>true</code> podczas ruchu tego gracza; <code>false</code> poza ruchem
     */
    public boolean czyWykonujeRuch() {
        return wykonujeRuch;
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
    public Gracz dalekiSasiad(int odlegloscSkierowana) {
        if (Math.abs(odlegloscSkierowana) >= gra.liczbaGrczy())
            throw new ArrayIndexOutOfBoundsException();
        return gra.graczONumerze((gra.liczbaGrczy() + odlegloscSkierowana + numer) % gra.liczbaGrczy());
    }

    /**
     * Mierzy odległość pomiędzy tym graczem, a wskazanym w parametrze.
     * @param gracz gracz, do którego mierzymy odległość. Musi być żywy i brać udział w tej samej rozgrywce.
     * @return odległość tego gracza od podanego
     */
    public int odlegloscOd(Gracz gracz) {
        return Math.abs(odlegloscIKierunekOd(gracz));
    }

    /**
     * Mierzy odległość pomiędzy tym graczem, a wskazanym w parametrze; zwraca dodatkowo informację o kierunku
     * @param gracz gracz, do którego mierzymy odległość. Musi być żywy i brać udział w tej samej rozgrywce.
     * @return liczbę całkowita o module równym odległości do wskazanego gracza, której znak zależy od kierunku jej
     * mierzenia (dodatni -- zgodnie z kierunkiem gry, ujemny -- przeciwnie do niefgo)
     */
    public int odlegloscIKierunekOd(Gracz gracz) {
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
    public int odlegloscSkierowanOd(int kierunek, Gracz gracz) {
        if (kierunek == 0)
            throw new IllegalArgumentException();
        if (gracz == this)
            return 0;
        if (numer < gracz.numer) {
            int zgodnaOdleglosc = gracz.numer - numer;
            if (kierunek > 0)
                return zgodnaOdleglosc;
            return gra.liczbaGrczy() - zgodnaOdleglosc;
        }
        return gracz.odlegloscSkierowanOd(-kierunek, this);
    }

    /**
     * Wykonaj akcję leczenia na celu o ile to możliwe.
     * Przyjmuje się, że jest możliwe, nawet jeśli cel ma maksymalną liczbę punktóœ życia.
     * @param cel gracz do uleczania (teg lub sąsiad)
     * @throws PozaZasiegiemWyjatek kiedy cel jest poza zasięgiem leczenia
     * @throws BrakAkcjiWyjatek kiedy gracz nie ma tej akcji na ręce
     */
    public void akcjaUlecz(Gracz cel) throws PozaZasiegiemWyjatek, BrakAkcjiWyjatek, NieTwojRochWyjatek {
        if (cel != this && cel != poLewej && cel != poPrawej)
            throw new PozaZasiegiemWyjatek();
        if (!czyWykonujeRuch())
            throw new NieTwojRochWyjatek();
        odrzucAkcje(Akcja.ULECZ);
        cel.dodajPZ(1);
    }

    /**
     * Strzel do gracza, o ile to możliwe.
     * @param cel gracz, do którego mierzymy
     * @throws PozaZasiegiemWyjatek kiedy cel znajduje się poza zasięgiem
     * @throws BrakAkcjiWyjatek kiedy gracz nie ma tej akcji na ręce
     */
    public void akcjaStrzel(Gracz cel) throws PozaZasiegiemWyjatek, BrakAkcjiWyjatek, NieTwojRochWyjatek {
        if (odlegloscOd(cel) > zasieg)
            throw new PozaZasiegiemWyjatek();
        if (!czyWykonujeRuch())
            throw new NieTwojRochWyjatek();
        odrzucAkcje(Akcja.STRZEL);
        cel.dodajPZ(-1);
    }

    /**
     * Wykorzystuje akcję zwiększenia zasięgu.
     * @throws BrakAkcjiWyjatek kied gracz nie ma tej akcji
     */
    public void akcjaZasiegPlusJeden() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        if (!czyWykonujeRuch())
            throw new NieTwojRochWyjatek();
        odrzucAkcje(Akcja.ZASIEG_PLUS_JEDEN);
        zasieg += 1;
    }

    /**
     * Wykorzystuje akcję zwiększenia zasięgu o dwa.
     * @throws BrakAkcjiWyjatek gdy nie ma akcji
     */
    public void akcjaZasiegPlusDwa() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        if (!czyWykonujeRuch())
            throw new NieTwojRochWyjatek();
        odrzucAkcje(Akcja.ZASIEG_PLUS_DWA);
        zasieg += 2;
    }

    /**
     * Wykorzystuje akcję rzucenia dynamitu
     * @throws BrakAkcjiWyjatek ...
     */
    public void akcjaDynamit() throws BrakAkcjiWyjatek, NieTwojRochWyjatek {
        if (!czyWykonujeRuch())
            throw new NieTwojRochWyjatek();
        odrzucAkcje(Akcja.DYNAMIT);
        gra.uruchomDynamit();
    }

    /**
     * Zwraca liczbę akcji danego typu, jaki gracz ma do wykorzystania
     * @param akcja typ akcji, o którą pytamy
     * @return liczbę akcji typu <code>akcja</code>
     */
    public int ileAkcji(Akcja akcja) {
        return akcje.ileTypu(akcja);
    }

    /**
     * Wykonuje ruch gracza.
     */
    void graj() {
        try {
            wykonujeRuch = true;
            kontroler.graj();
        } catch (BladKonrtoleraWyjatek e) {
            e.printStackTrace();
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
    void przygotujDoGry(Gra gra, Gracz obokZLewej, Gracz obokZPrawej, int numer) {
        assert obokZLewej != null;
        assert obokZPrawej != null;
        this.poLewej = obokZLewej;
        this.poPrawej = obokZPrawej;
        this.gra = gra;
        this.numer = numer;
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
     * Odrzuca akcję z ręki (i ile tam jest) z powrotem na stos ściepów.
     * @param akcja akcja do odzrucenia
     * @throws BrakAkcjiWyjatek kiedy nie ma akcji na ręce
     */
    private void odrzucAkcje(Akcja akcja) throws BrakAkcjiWyjatek {
        if (akcje.zabierz(akcja) < 1)
            throw new BrakAkcjiWyjatek();
        gra.oddajAkcje(akcja);
    }
}
