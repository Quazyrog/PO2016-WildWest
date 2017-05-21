package dzikizachod;

import java.util.Random;

/**
 * Disboard, który od ogyginału różni się tylko tym, że w konstruktorze rejestruje wypisywacza :\
 */
public class Gra extends Disboard {
    public Gra() {
        this(new Random());
    }

    public Gra(Random rng) {
        super(rng);
        dodajObserwatora(new Wypisywacz(System.out));
    }
}
