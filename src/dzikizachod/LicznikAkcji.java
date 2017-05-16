package dzikizachod;

/**
 * Zarządza zapasami akcji.
 * Jest to najzwuczajniej licznik na akcje. Określany także mianem ,,pudełka''.
 */
class LicznikAkcji {
    private int liczniki[] = new int [Akcja.liczbaIndeksow()];

    /**
     * Zwraca liczbę akcji danego typu dostępnych w pudełku.
     * @param co typ akcji do policzenia
     * @return liczbę akcji typu <c>co</c>
     */
    int ileDostepnych(Akcja co) {
        return liczniki[co.indeks()];
    }

    /**
     * Zabierz n akcji.
     * Jeśli w pudełku (liczniku) nie ma dosyć akcji, zabierz wszystkie.
     * @param co jakie akcje zabirać
     * @param ile ile ich zabrać
     * @return liczbę zabranych akcji
     */
    int dobierz(Akcja co, int ile) {
        if (ile < 0)
            throw new IllegalArgumentException("ile < 0");
        int dobrane = Math.min(ileDostepnych(co), ile);
        liczniki[co.indeks()] -= dobrane;
        return dobrane;
    }

    /**
     * Zabierz jedną akcję, jeżeli jest w pudełku.
     * @param co jakie akcje zabirać
     * @return liczbę zabranych akcji
     */
    int dobierz(Akcja co) {
        if (ileDostepnych(co) > 0) {
            liczniki[co.indeks()] -= 1;
            return 1;
        }
        return 0;
    }

    /**
     * Dodaj n akcji do pudełka.
     * @param co jakie akcje dodać
     * @param ile ile ich dodać
     */
    void dodaj(Akcja co, int ile) {
        if (ile < 0)
            throw new IllegalArgumentException("ile < 0");
        liczniki[co.indeks()] += ile;
    }
}
