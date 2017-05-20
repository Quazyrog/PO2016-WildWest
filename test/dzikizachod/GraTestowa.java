package dzikizachod;

/**
 * Gra, która nie tasuje graczy.
 * Poza tym, rzuca wyjątek RuntimeException, jak stanie sie coś dziwnego (np Vice strzeli do Szeryfa).
 */
public class GraTestowa extends Disboard {
    @Override
    protected void przygotujRozgrywke() {
        sprawdzTabliceGraczy();
        przygotujGraczy();
    }
}
