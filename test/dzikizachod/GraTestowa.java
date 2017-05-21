package dzikizachod;

import java.util.Random;

/**
 * Gra, która nie tasuje graczy.
 * Poza tym, rzuca wyjątek RuntimeException, jak stanie sie coś dziwnego (np Vice strzeli do Szeryfa).
 */
public class GraTestowa extends Disboard {
    public GraTestowa(Random rng) {
        super(rng);
    }

    public GraTestowa() {
        super(new Random());
    }

    @Override
    protected void przygotujRozgrywke() {
        sprawdzTabliceGraczy();
        przygotujGraczy();
    }
}
