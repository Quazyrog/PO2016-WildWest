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
    /** Karty do przetasowania, z których nie dobieramy */
    private LicznikAkcji poczatkowe = new LicznikAkcji();

    /** Karty do przetasowania, z których nie dobieramy */
    private LicznikAkcji sciepy = new LicznikAkcji();

    /** Karty przetasowane, z których dobieramy */
    private LicznikAkcji talia = new LicznikAkcji();

    /** Generator liczb losowych do losowania kart */
    private Random rng;


    /**
     * Tworzy pulę akcji używającą podanego generatora liczb losowych
     * @param rng
     */
    public PulaAkcji(Random rng) {
        this.rng = rng;
    }


    /**
     * Tworzy pulę akcji korzystającą z nowo utworzonego generatora liczb losowych.
     */
    public PulaAkcji() {
        this(new Random());
    }


    /**
     * Ustawia stan talii na taki, jaki był przed rozpoczęciem gry.
     * W tali będą znajdowac się tylko te akcje i w dokładnie takiej ilości, jak zostały dodane.
     */
    public void przywrocPoczatkowe() {
        sciepy = new LicznikAkcji();
        talia = new LicznikAkcji();
        for (Akcja a : Akcja.values())
            talia.dodaj(a, poczatkowe.ileTypu(a));
    }


    /**
     * Dodaj do puli podaną liczbę akcji danego typu.
     * Liczba akcji danego typu w puli jest ograniczona przez <c>Akcja.obetnijDoLimitu()</c>. Akcje można dodawać po
     * rozpoczęciu gry, ale zostanie to wzięte pod uwagę dopiero w następnej rozgrywcie z wykorzystaniem tej talii.
     * @param akcja typ dodawanych akcji
     * @param ile liczba dodawanych akcji
     * @return liczbę akcji dodanych do puli (może być mniejsza od <c>ile</c>)
     */
    public int dodaj(Akcja akcja, int ile) {
        if (ile <= 0)
            throw new IllegalArgumentException("Nie można zabrać kart z puli");

        int dodane = akcja.obetnijDoLimitu(poczatkowe.ileTypu(akcja) + ile) - poczatkowe.ileTypu(akcja);
        poczatkowe.dodaj(akcja, dodane);
        return dodane;
    }


    /**
     * Dobiera losową kartę z talii.
     * Jeśli talia jest pusta, to wszystkie odrzucone karty zostaną przetasowane (z uwzględnieniem tego, że
     * część może się nie wtasowywac ponownie, jak dynamit).
     * @return wylosowaną z talii kartę
     */
    public Akcja dobierz() {
        if (talia.ileWszystkich() == 0)
            przetasuj();

        Akcja wylosowana = wylosujAkcje();
        assert talia.zabierz(wylosowana) == 1;
        return wylosowana;
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
        if (talia.ileWszystkich() == 0) //bo dynamit
            throw new RuntimeException("Talia jest pusta i nie ma czego przetasować");
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
            if (ktora < 0 && talia.ileTypu(a) > 0)
                return a;
        }
        throw new Error("talia.ileRoznych() nie zadziałało");
    }
}
