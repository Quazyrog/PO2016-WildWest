package dzikizachod;

/**
 * Gra, która nie tasuje graczy
 */
public class GraTestowa extends Gra {
    @Override
    protected void przygotujRozgrywke() {
        sprawdzTabliceGraczy();
        przygotujGraczy();
    }
}
