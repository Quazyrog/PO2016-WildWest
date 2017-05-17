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

    /**
     * W zależności od znaku kierunku zwraca gracza po lewej lub prawej od podanego.
     * Dziki Zachód jest miejscem, w którym panuje orientacja dodatnia, więc naturalnym kierunkiem gry jest kierunek
     * przeciwny do ruchu wskazówek zegara. Wobec tego kierunek ujemny oznacza gracza po lewej, a dodatni po prawej.
     * @param odGracza gracz, którego sąsiada potrzebujemy
     * @param wKierunku kierunek, oznaczający lewego sąsiada (kierunek ujemny) lub prawego (dodatni); nie może być 0
     * @return <c>odGracza.poLewej</c> gdy <c>kierunek < 0</c>; <c>odGracza.poPrawej</c> gdy <c>kierunek > 0</c>
     */
    static private Gracz przeskocz(Gracz odGracza, int wKierunku) {
        assert wKierunku != 0;
        if (wKierunku < 0)
            return odGracza.poLewej;
        if (wKierunku > 0)
            return odGracza.poPrawej;
        throw new Error("To się nie powinno zdażyć");
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
        if (pz == 0)
            gra.graczUmarl(this);
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
     * Zwraca gracza znajdującego się we wskazanej odległości na prawo lub lewo od tego gracza.
     * Dodatnia wartość parametru oznacza gracza znajdującego się <code>odlegloscSkierowana</code> pozycji na prawo.
     * Ujemna wartość tego parametru oznacza gracza <code>-odlegloscSkierowana</code> na lewo. Wartosć bezwzględna
     * parametru musi być mniejsza niż liczba żywych graczy.
     * @param odlegloscSkierowana liczba, której moduł określa odległość poszukiwanego gracza, a znak kierunek jej
     *                            mierzenia (ujemny -> na lewo; dodatni -> na prawo). Zerowy oznacza tego gracza.
     * @return gracza znajdującego się we wskazanej odległości na prawo lub lewo od tego gracza
     */
    public Gracz dalekiSasiad(int odlegloscSkierowana) {
        if (odlegloscSkierowana == 0)
            return this;
        Gracz iter = this;
        while (odlegloscSkierowana != 0) {
            iter = przeskocz(iter, odlegloscSkierowana);
            odlegloscSkierowana -= Integer.signum(odlegloscSkierowana);
            if (iter == this)
                throw new ArrayIndexOutOfBoundsException();
        }
        return iter;
    }

    /**
     * Mierzy odległość pomiędzy tym graczem, a wskazanym w parametrze.
     * @param gracz gracz, do którego mierzymy odległość. Musi być żywy i brać udział w tej samej rozgrywce.
     * @return odległość tego gracza od podanego
     */
    public int odlegloscOd(Gracz gracz) {
        return Math.min(odlegloscSkierowanOd(-1, gracz), odlegloscSkierowanOd(1, gracz));
    }

    /**
     * Wykonaj akcję leczenia na celu o ile to możliwe.
     * Przyjmuje się, że jest możliwe, nawet jeśli cel ma maksymalną liczbę punktóœ życia.
     * @param cel gracz do uleczania (teg lub sąsiad)
     * @throws PozaZasiegiemWyjatek kiedy cel jest poza zasięgiem leczenia
     * @throws BrakAkcjiWyjatek kiedy gracz nie ma tej akcji na ręce
     */
    public void akcjaUlecz(Gracz cel) throws PozaZasiegiemWyjatek, BrakAkcjiWyjatek {
        if (cel != this && cel != poLewej && cel != poPrawej)
            throw new PozaZasiegiemWyjatek();
        odrzucAkcje(Akcja.ULECZ);
        cel.dodajPZ(1);
    }

    /**
     * Strzel do gracza, o ile to możliwe.
     * @param cel gracz, do którego mierzymy
     * @throws PozaZasiegiemWyjatek kiedy cel znajduje się poza zasięgiem
     * @throws BrakAkcjiWyjatek kiedy gracz nie ma tej akcji na ręce
     */
    public void akcjaStrzel(Gracz cel) throws PozaZasiegiemWyjatek, BrakAkcjiWyjatek {
        if (odlegloscOd(cel) > zasieg)
            throw new PozaZasiegiemWyjatek();
        odrzucAkcje(Akcja.STRZEL);
        cel.dodajPZ(-1);
    }

    /**
     * Wykorzystuje akcję zwiększenia zasięgu.
     * @throws BrakAkcjiWyjatek kied gracz nie ma tej akcji
     */
    public void akcjaZasiegPlusJeden() throws BrakAkcjiWyjatek {
        odrzucAkcje(Akcja.ZASIEG_PLUS_JEDEN);
        zasieg += 1;
    }

    /**
     * Wykorzystuje akcję zwiększenia zasięgu o dwa.
     * @throws BrakAkcjiWyjatek gdy nie ma akcji
     */
    public void akcjaZasiegPlusDwa() throws BrakAkcjiWyjatek {
        odrzucAkcje(Akcja.ZASIEG_PLUS_DWA);
        zasieg += 2;
    }

    /**
     * Wykorzystuje akcję rzucenia dynamitu
     * @throws BrakAkcjiWyjatek ...
     */
    public void akcjaDynamit() throws BrakAkcjiWyjatek {
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
        kontroler.graj();
    }

    /**
     * Przypisuje gracza do rozgrywki.
     * @param gra obiekt przeprowadzajacyy rozgrywkę
     * @param obokZLewej gracz po lewej (poprzedni gracz)
     * @param obokZPrawej gracz po prawej (następny gracz)
     */
    void przygotujDoGry(Gra gra, Gracz obokZLewej, Gracz obokZPrawej) {
        assert obokZLewej != null;
        assert obokZPrawej != null;
        this.poLewej = obokZLewej;
        this.poPrawej = obokZPrawej;
        this.gra = gra;
        pz = limitPZ;
    }

    /**
     * Gracz opuszcza rozgrywkę.
     */
    void skonczGrac() {
        this.poLewej = null;
        this.poPrawej = null;
        this.gra = null;
    }

    /**
     * Daje graczowi akcje na reke.
     * @param coDobral akacja do dobrania
     */
    void dobierz(Akcja coDobral) {
        akcje.dodaj(coDobral, 1);
    }

    /**
     * Mierzy odległosć w danym kierunku do podanego gracza.
     * @param kierunek dodatni oznacza odległośc na peawo, ujemny w lewo
     * @param gracz gracz, do którego mierzymy odległość
     * @return odległość od gracza mierząoną we wskazanym kierunku
     */
    private int odlegloscSkierowanOd(int kierunek, Gracz gracz) {
        assert kierunek != 0;
        if (gracz.gra != gra)
            throw new IllegalArgumentException("gracze muszą byc w tej samej rozgrywce i obaj żywi");
        if (gracz == this)
            return 0;
        kierunek = Integer.signum(kierunek);

        int odleglosc = 0;
        Gracz iter = this;
        do {
            ++odleglosc;
            iter = przeskocz(iter, kierunek);
        } while (iter != this && iter != gracz);

        if (iter == gracz)
            return odleglosc;
        throw new Error("To się nie powinno zdażyć");
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
