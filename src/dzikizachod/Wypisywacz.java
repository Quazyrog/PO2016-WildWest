package dzikizachod;

import java.io.PrintStream;

/**
 * Na podstawie otrzymywanych od Disboardu wiadopmości wypisuje komunikaty, takie jak w specyfkacji.
 * Bo wydaje się, że wciskanie tego do jednej klasy z grą było by jak nieoddzielanie UI od Logiki, a tego się
 * bodaj nie robi.
 */
public class Wypisywacz implements IObserwator {
    private PrintStream out;
    private StrategicznyWidokGracza gracze[];

    private int WielkoscWciecia = 0;
    boolean zrobWciecie = true;
    private StringBuilder wciecie = new StringBuilder();
    private int wypisaneAkcjeDobrane;
    private int wypisaneAkcjeWykonane;


    /**
     * Konstruuje nowego Wypisywacza, który wypluwa komunikaty na <code>output</code>.
     * @param output strumień dla komunikatów.
     */
    Wypisywacz(PrintStream output) {
        out = output;
    }

    protected void zwiekszWciecie() {
        ++WielkoscWciecia;
        wciecie.append("  ");
    }

    protected void zmniejszWciecie() {
        if (WielkoscWciecia == 0)
            throw new IllegalStateException("Ta klasa nie potrafi zrobić ujemnego wciecia. " +
                    "Zgłoś się de deweloperów swojego terminala");
        --WielkoscWciecia;
        wciecie.delete(wciecie.length() - 2, -1);
    }

    protected void wypluj(String s) {
        if (zrobWciecie)
            out.print(wciecie);
        out.print(s);
    }

    protected void wyplujln(String s) {
        wypluj(s);
        wyplujln();
    }

    protected void wyplujln() {
        out.println();
        zrobWciecie = true;
    }

    @Override
    public void patrzPoczatekGry(StrategicznyWidokGracza[] gracze, StrategicznyWidokGracza szeryf, int liczbaBandytów, int liczbaPomocników) {
        gracze = gracze;

        wypluj("** START");
        zwiekszWciecie();
        wyplujGraczy();
        zmniejszWciecie();
    }

    protected void wyplujGraczy() {
        wyplujln("Gracze:");
        for (StrategicznyWidokGracza g : gracze) {
            if (g.pz() > 0)
                wyplujln((g.identyfikator() + 1) + ": " + g.tozsamosc() + "(liczba żyć: " + g.pz() + ")");
            else
                wyplujln((g.identyfikator() + 1) + ": X (" + g.tozsamosc() + ")");
        }
    }

    @Override
    public void patrzKolejnaTura(int numerTury) {
        zmniejszWciecie();
        wyplujln("** TURA " + numerTury);
        zwiekszWciecie();
    }

    @Override
    public void patrzRuchGracza(StrategicznyWidokGracza ktoGra) {
        wyplujln("GRACZ " + ktoGra.identyfikator() + "(" + ktoGra.tozsamosc() + "):");
        zwiekszWciecie();
        wypisaneAkcjeDobrane = 0;
        wypisaneAkcjeWykonane = 0;
    }

    @Override
    public void patrzDobralAkcje(StrategicznyWidokGracza ktoGra, Akcja a) {
        if (wypisaneAkcjeDobrane == 0) {
            wypluj("Akcje: [" + a);
        } else if (wypisaneAkcjeDobrane == Disboard.LICZBA_DOBIERANYCH_AKCJI) {
            wyplujln(", " + a + "]");
        } else {
            wypluj(", " + a);
        }
        ++wypisaneAkcjeDobrane;
    }

    @Override
    public void patrzNaDynamit(StrategicznyWidokGracza ktoGra, boolean wybuchl) {
        if (wybuchl)
            wyplujln("Dynamit: WYBUCHL");
        else
            wyplujln("Dynamit: PRZECHODZI DALEJ");
    }

    @Override
    public void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {
        if (wypisaneAkcjeWykonane == 0) {
            wyplujln("Akcje:");
            zwiekszWciecie();
        }
        switch (a) {
            case ULECZ:
                if (naKim.equals(ktoGra))
                    wyplujln(a.toString());
                else
                    wyplujln(a.toString() + " " + naKim.identyfikator());
                break;
            case STRZEL:
                wyplujln(a.toString() + naKim.identyfikator());
                break;
            case ZASIEG_PLUS_JEDEN:
            case ZASIEG_PLUS_DWA:
            case DYNAMIT:
                wyplujln(a.toString());
                break;
        }
        ++wypisaneAkcjeWykonane;
    }

    @Override
    public void patrzSkonczylTure(StrategicznyWidokGracza ktoGra) {
        if (wypisaneAkcjeWykonane > 0)
            zmniejszWciecie();
        wyplujGraczy();
        zmniejszWciecie();
    }

    @Override
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {
        //TODO jak by to bł dynamit
    }

    @Override
    public void patrzKoniecGry(Zakonczenie zakonczenie) {
        zmniejszWciecie();
        wyplujln("** KONIEC");

        switch (zakonczenie) {
            case DOBRO_WYGRALO:
                wyplujln("WYGRANA STRONA: szeryf i pomocnicy");
                break;
            case ZLO_WYGRALO:
                wyplujln("WYGRANA STRONA: bandyci");
                break;
            case REMIS:
                wyplujln("REMIS - OSIĄGNIĘTO LIMIT TUR");
                break;
        }
    }
}
