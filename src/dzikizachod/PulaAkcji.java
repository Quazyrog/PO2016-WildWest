package dzikizachod;

import java.util.Random;

/**
 * Klasa reprezentująca pulę akcji.
 *
 * Umożliwia dodanie przed rozpoczęciem rozgrywki pewnych liczb, a następnie dobieranie ich w trakcie rozgrywki.
 * Akcje są automatycznie przetasowywane, kiedy pula zostanie wyczerpana.
 *
 * @implNote Pula akcji nie przechowuje fkatycznej talii w postaci np. tablicy kart. Jej stan jest trzymany
 * jako liczniki kart dostępnych i odrzuconych każdego typu, a losowanie polega na dobraniu losowej karty
 * dostępnego typu. Oczywiście karty których jest więcej, są bardziej prawdopodobne.
 */
public class PulaAkcji {
    /** Czy można dodawać akcje, czyli czy nic nie zostało jeszcze dobrane */
    private boolean moznaDodawac = true;

    /** Karty do przetasowania, z których nie dobieramy */
    private LicznikAkcji sciepy = new LicznikAkcji();

    /** Karty przetasowane, z których dobieramy */
    private LicznikAkcji talia = new LicznikAkcji();

    /** Generator liczb losowych do losowania kart */
    private Random rng = new Random();

    /**
     * Dodaj do puli podaną liczbę akcji danego typu.
     * Liczba akcji danego typu w puli jest ograniczona przez <c>Akcja.obetnijDoLimitu()</c>. Ponadto nie
     * można dodawać akcji, kiedy jakaś została już dobrana.
     * @param akcja typ dodawanych akcji
     * @param ile liczba dodawanych akcji
     * @return liczbę akcji dodanych do puli (może być mniejsza od <c>ile</c>)
     */
    public int dodaj(Akcja akcja, int ile) {
        if (!moznaDodawac)
            throw new IllegalStateException("Nie można dodawać kart po rozpoczęciu rozgrywki");
        if (ile <= 0)
            throw new IllegalArgumentException("Nie można zabrać kart z puli");

        int dodane = akcja.obetnijDoLimitu(sciepy.ileTypu(akcja) + ile) - sciepy.ileTypu(akcja);
        sciepy.dodaj(akcja, dodane);
        return dodane;
    }

    /**
     * Dobiera losową kartę z talii.
     * Jeśli talia jest pusta, to wszystkie odrzucone karty zostaną przetasowane (z uwzględnieniem tego, że
     * część może się nie wtasowywac ponownie, jak dynamit).
     * @return wylosowaną z talii kartę
     */
    public KartaAkcji dobierz() {
        moznaDodawac = false;
        if (talia.ileWszystkich() == 0)
            przetasuj();

        Akcja wylosowana = wylosujAkcje();
        assert talia.zabierz(wylosowana) == 1;

        return new KartaAkcji(this, wylosowana);
    }

    /**
     * Odrzuca akcję do ściepów (kart do przetasowania)
     * @param sciep akcja przyporządkowana do odrzucanej karty
     */
    void odrzuc(Akcja sciep) {
        sciepy.dodaj(sciep, 1);
    }

    /**
     * Wtasowuje ściepy z powrotem do talii kart.
     */
    private void przetasuj() {
        if (sciepy.ileWszystkich() == 0)
            throw new RuntimeException("Talia jest pusta i nie ma czego przetasować");
        for (Akcja a : Akcja.values()) {
            int liczbaSciepow = sciepy.ileTypu(a);
            talia.dodaj(a, a.liczbaPonownieWtasowanych(liczbaSciepow));
            sciepy.zabierz(a, liczbaSciepow);
        }
    }

    /**
     * Losuje akcję z dostępnych w przetasowanej talii.
     * Nie przetasuje ściepów, jeśli przetasowana część talii jest pusta. Licznik wylosowanej
     * akcji jest aktualizowany (zmniejszany o 1). Prawdopodobieństwo dobrania danej karty jest
     * proprcjonalne do jej liczebności wśród dostępnych kart.
     * @return losową akcję z dostępnych w talii
     */
    private Akcja wylosujAkcje() {
        int ktora = rng.nextInt(talia.ileWszystkich());
        for (Akcja a : Akcja.values()) {
            ktora -= talia.ileTypu(a);
            if (ktora <= 0)
                return a;
        }
        throw new Error("talia.ileRoznych() nie zadziałało");
    }
}
