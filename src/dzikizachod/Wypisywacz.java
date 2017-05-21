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

    private int wielkoscWciecia = 0;
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
        ++wielkoscWciecia;
        wciecie.append("  ");
    }


    protected void zmniejszWciecie() {
        if (wielkoscWciecia == 0)
            return;
        --wielkoscWciecia;
        wciecie.delete(wielkoscWciecia * 2, wielkoscWciecia * 2 + 2);
    }


    protected void wypluj(String s) {
        if (zrobWciecie) {
            out.print(wciecie);
            zrobWciecie = false;
        }
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
        this.gracze = gracze;

        wyplujln("** START");
        zwiekszWciecie();
        wyplujGraczy();
        zmniejszWciecie();
    }


    protected void wyplujGraczy() {
        wyplujln("Gracze:");
        zwiekszWciecie();
        for (StrategicznyWidokGracza g : gracze) {
            if (g.pz() > 0)
                wyplujln(g.identyfikator() + ": " + g.tozsamosc() + "(liczba żyć: " + g.pz() + ")");
            else
                wyplujln(g.identyfikator() + ": X (" + g.tozsamosc() + ")");
        }
        zmniejszWciecie();
    }


    @Override
    public void patrzKolejnaTura(int numerTury) {
        zmniejszWciecie();
        wyplujln("\n** TURA " + numerTury);
        zwiekszWciecie();
    }


    @Override
    public void patrzRuchGracza(StrategicznyWidokGracza ktoGra) {
        if (ktoGra.tozsamosc() != TozsamoscGracza.SZERYF)
            wyplujln();
        wyplujln("GRACZ " + ktoGra.identyfikator() + "(" + ktoGra.tozsamosc() + "):");
        zwiekszWciecie();
        wypisaneAkcjeDobrane = 0;
        wypisaneAkcjeWykonane = 0;
    }


    @Override
    public void patrzDobralAkcje(StrategicznyWidokGracza ktoGra, Akcja a) {
        //FIXME to nie ma być lista DOBRANYCH akcji
        if (wypisaneAkcjeDobrane == 0) {
            wypluj("Akcje: [" + a);
        } else if (wypisaneAkcjeDobrane + 1 == Disboard.LICZBA_DOBIERANYCH_AKCJI) {
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
            wyplujln("Ruchy:");
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
                wyplujln(a.toString() + " " + naKim.identyfikator());
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
        else if (ktoGra.pz() > 0)
            wyplujln("Ruchy:");
        wyplujln();
        wyplujGraczy();
        zmniejszWciecie();
    }


    @Override
    public void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {
        if (zabojca == null) {
            wyplujln("Ruchy:");
            zwiekszWciecie();
            wyplujln("MARTWY");
            zmniejszWciecie();
        }
    }


    @Override
    public void patrzKoniecGry(Zakonczenie zakonczenie) {
        if (wielkoscWciecia > 0)
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
