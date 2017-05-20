package dzikizachod;

/**
 * Rodzaje akcji dostępnych w grze.
 */
public enum Akcja {
    /** Heal + 1 */
    ULECZ,
    /** Strzelenie w innego gracza */
    STRZEL,
    /** Zwiększenie zasiegu o 1 */
    ZASIEG_PLUS_JEDEN,
    /** Zwiększenie zasięgu o 2 */
    ZASIEG_PLUS_DWA,
    /** Rzucenie dynamitu */
    DYNAMIT;

    /**
     * Zwróć liczbę elementów w wyliczeniu
     * @return <c>Akcja.values.length</c>
     */
    public static int liczbaIndeksow() {
        return Akcja.values().length;
    }

    /**
     * Obetnij liczbę akcji zgodnie z limitem na maksymalną liczbę akcji w talii
     * Pozwala ograniczyć liczbę akcji, jaka może się znajdować jednocześnie w tali danej rozgrywki. Oczywiście
     * przestrzeganie tego limitu to i tak dobra wola implementacji pulii kart.
     * @param liczbaAkcji żądana liczba kart w puli
     * @return min(liczbaAkcji, limitNaDanąAkcję)
     */
    public int obetnijDoLimitu(int liczbaAkcji) {
        if (liczbaAkcji < 0)
            return 0;
        switch (this) {
            case DYNAMIT:
                return Math.min(1, liczbaAkcji);
            default:
                return liczbaAkcji;
        }
    }

    /**
     * Zwróć liczbę akcji, które mają zostać wtasowane do puli, po jej wyczerpaniu.
     * Pozwala to doprowadzić wprowadzić mechanikę, w której pewne karty są tylko częściowo (lub wcale nie są)
     * wtasowywane. Przestrzeganie tego to również wola implementacji pulii kart.
     * @param liczbaSciepow liczba akcji danego typu oczekująca na przetasowanie
     * @return liczbę kart danego typu, która powinna zostać ponownie wtasowana
     */
    public int liczbaPonownieWtasowanych(int liczbaSciepow) {
        switch (this) {
            case DYNAMIT:
                return 0;
            default:
                return liczbaSciepow;
        }
    }

    /**
     * Zwróć indeks przyporządkowany danemu typowi akcji.
     * Wszystkie dostępne typy akcji mają swój indeks (są to poprawne indeksy tablicy).
     * @return indeks danej akcji
     */
    public int indeks() {
        switch (this) {
            case ULECZ:
                return 0;
            case STRZEL:
                return 1;
            case ZASIEG_PLUS_JEDEN:
                return 2;
            case ZASIEG_PLUS_DWA:
                return 3;
            case DYNAMIT:
                return 4;
        }
        throw new Error("This should never happen.");
    }


    @Override
    public String toString() {
        switch (this) {
            case ULECZ:
                return "ULECZ";
            case STRZEL:
                return "STRZEL";
            case ZASIEG_PLUS_JEDEN:
                return "ZASIEG_PLUS_JEDEN";
            case ZASIEG_PLUS_DWA:
                return "ZASIEG_PLUS_DWA";
            case DYNAMIT:
                return "DYNAMIT";
        }
        throw new Error("This should never happen.");
    }
}
