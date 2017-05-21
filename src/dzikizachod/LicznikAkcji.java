package dzikizachod;

/**
 * Zarządza zapasami akcji.
 * Jest to najzwuczajniej licznik na akcje. Określany także mianem ,,pudełka''.
 */
class LicznikAkcji {
    /** Licznik dla każdego rodzaju akcji w grze */
    private int liczniki[] = new int [Akcja.liczbaIndeksow()];

    /** Utrzymuje liczbę wszystkich przechowywanych akcji w sumie. */
    private int licznikAkcji = 0;


     /**
     * Zwraca liczbę akcji danego typu dostępnych w pudełku.
     * @param co typ akcji do policzenia
     * @return liczbę akcji typu <c>co</c>
     */
    public int ileTypu(Akcja co) {
        return liczniki[co.indeks()];
    }


    /**
     * Zabierz n akcji.
     * Jeśli w pudełku (liczniku) nie ma dosyć akcji, zabierz wszystkie.
     * @param co jakie akcje zabirać
     * @param ile ile ich zabrać
     * @return liczbę zabranych akcji
     */
    public int zabierz(Akcja co, int ile) {
        if (ile < 0)
            throw new IllegalArgumentException("ile < 0");
        int dobrane = Math.min(ileTypu(co), ile);
        zmienLicznik(co, ile);
        return dobrane;
    }


    /**
     * Zabierz jedną akcję, jeżeli jest w pudełku.
     * @param co jakie akcje zabirać
     * @return liczbę zabranych akcji
     */
    public int zabierz(Akcja co) {
        if (ileTypu(co) > 0) {
            zmienLicznik(co, -1);
            return 1;
        }
        return 0;
    }


    /**
     * Dodaj n akcji do pudełka.
     * @param co jakie akcje dodać
     * @param ile ile ich dodać
     */
    public void dodaj(Akcja co, int ile) {
        if (ile < 0)
            throw new IllegalArgumentException("ile < 0");
        zmienLicznik(co, ile);
    }


    /**
     * Zwraca liczbę wszystkich przechowywanych akcji w sumie
     * @return liczbę wszystkich akcji
     */
    public int ileWszystkich() {
        return licznikAkcji;
    }


    /**
     * Zmienia licznik akcji, o delta.
     * Dba o utrzymywanie oddatkowych liczników oraz by nie przechowywać ujemnej liczby akcji.
     * Jeśli liczba akcji powinna spaść poniżej zera, to spadnie tylko do zera.
     * @param co typ akcji do zmodyfikowania
     * @param delta liczba, o którą przestawiamy licznik
     */
    protected void zmienLicznik(Akcja co, int delta) {
        liczniki[co.indeks()] = Math.max(liczniki[co.indeks()] + delta, 0);
        licznikAkcji = Math.max(licznikAkcji + delta, 0);
    }
}
