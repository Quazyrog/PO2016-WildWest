package dzikizachod;

/**
 * Disboard, który od ogyginału różni się tylko tym, że w konstruktorze rejestruje wypisywacza :\
 */
public class Gra extends Disboard {
    public Gra() {
        dodajObserwatora(new Wypisywacz(System.out));
    }
}
