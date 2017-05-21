package dzikizachod.main;
import dzikizachod.*;

import java.util.Random;

/**
 * Testowy main
 */
public class TestMain {
    public static void demo(long seed) {
        Random rng = new Random(seed);
        Gracz.nasiono(seed);

        Gracz gracze[] = {
                new Szeryf(new StrategiaSzeryfaZliczajaca(rng)),
                new PomocnikSzeryfa(new StrategiaPomocnikaSzeryfaZliczajaca(rng)),
                new PomocnikSzeryfa(new StrategiaPomocnikaSzeryfaZliczajaca(rng)),
                new Bandyta(new StrategiaBandytyDomyslna(rng)),
                new Bandyta(new StrategiaBandytyDomyslna(rng)),
                new Bandyta(new StrategiaBandytyDomyslna(rng))
        };

        PulaAkcji pulaAkcji = new PulaAkcji(rng);
        pulaAkcji.dodaj(Akcja.ULECZ, 20);
        pulaAkcji.dodaj(Akcja.STRZEL, 60);
        pulaAkcji.dodaj(Akcja.ZASIEG_PLUS_JEDEN, 3);
        pulaAkcji.dodaj(Akcja.ZASIEG_PLUS_DWA, 1);
        pulaAkcji.dodaj(Akcja.DYNAMIT, 1);

        Gra gra = new Gra(rng);
        gra.rozgrywka(gracze, pulaAkcji);
    }


    public static void debugDemo() {
        long seed = System.currentTimeMillis();
        System.err.println("Seed: " + seed);
        demo(seed);
    }


    public static void trippleDemo(long seed) {
        Random rng = new Random(seed);
        Gracz.nasiono(seed);

        Gracz gracze[] = {
                new Szeryf(new StrategiaSzeryfaZliczajaca(rng)),
                new PomocnikSzeryfa(new StrategiaPomocnikaSzeryfaDomyslna(rng)),
                new PomocnikSzeryfa(new StrategiaPomocnikaSzeryfaDomyslna(rng)),
                new Bandyta(new StrategiaBandytyDomyslna(rng)),
                new Bandyta(new StrategiaBandytyDomyslna(rng)),
                new Bandyta(new StrategiaBandytyDomyslna(rng))
        };

        PulaAkcji pulaAkcji = new PulaAkcji(rng);
        pulaAkcji.dodaj(Akcja.ULECZ, 20);
        pulaAkcji.dodaj(Akcja.STRZEL, 60);
        pulaAkcji.dodaj(Akcja.ZASIEG_PLUS_JEDEN, 3);
        pulaAkcji.dodaj(Akcja.ZASIEG_PLUS_DWA, 1);
        pulaAkcji.dodaj(Akcja.DYNAMIT, 1);

        Gra gra = new Gra(rng);
        gra.rozgrywka(gracze, pulaAkcji);
        gra.rozgrywka(gracze, pulaAkcji);
        gra.rozgrywka(gracze, pulaAkcji);
    }

    public static void main(String args[]) {
        demo(1495362163578L);
    }
}
