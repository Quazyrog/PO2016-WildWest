package dzikizachod;

import java.util.Random;

/**
 * Prawie oryginalny Disboard, ale kompatybilny z interfejsem z zadania.
 * Od ogyginału różni się tylko tym, że w konstruktorze rejestruje wypisywacza :\ oraz pozwala się tworzyć bez RNG.
 */
public class Gra extends Disboard {
    /**
     * Tworzy grę z nową instancją RNG
     */
    public Gra() {
        this(new Random());
    }

    /**
     * Tworzy grę z podana instancją RNG
     * @param rng RNG do zainicjowania Disboardu
     */
    public Gra(Random rng) {
        super(rng);
        dodajObserwatora(new Wypisywacz(System.out));
    }
}
