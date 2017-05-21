package dzikizachod.main;
import dzikizachod.*;

import java.util.Random;

/**
 * Główna klasa odpowiedzilna za uruchomienie programu.
 */
public class Main {
    public static void demo() {
        Gracz gracze[] = {
                new Szeryf(new StrategiaSzeryfaZliczajaca()),
                new PomocnikSzeryfa(),
                new PomocnikSzeryfa(),
                new Bandyta(),
                new Bandyta(),
                new Bandyta()
        };

        PulaAkcji pulaAkcji = new PulaAkcji();
        pulaAkcji.dodaj(Akcja.ULECZ, 20);
        pulaAkcji.dodaj(Akcja.STRZEL, 60);
        pulaAkcji.dodaj(Akcja.ZASIEG_PLUS_JEDEN, 3);
        pulaAkcji.dodaj(Akcja.ZASIEG_PLUS_DWA, 1);
        pulaAkcji.dodaj(Akcja.DYNAMIT, 1);

        Gra gra = new Gra();
        gra.rozgrywka(gracze, pulaAkcji);

    }

    public static void main(String args[]) {
        demo();
    }
}
