package dzikizachod;

import java.util.ArrayList;

/**
 * Strategia dla szeryfa.
 * Utrzymuje listę graczy, którzy go zaatakowali.
 */
public abstract class StrategiaSzeryfa extends StrategiaOgolna {
    /** Lista żywych graczy, którzy zaatakowali tego gracza. */
    protected ArrayList<StrategicznyWidokGracza> paskudniBandyci = new ArrayList<>();

    @Override
    protected void ogarnijWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim) {
        switch (a) {
            case STRZEL:
                if (naKim.equals(ja()) && !paskudniBandyci.contains(ktoGra))
                    paskudniBandyci.add(ktoGra);
                break;
        }
    }

    @Override
    protected void ogarnijZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca) {
        paskudniBandyci.remove(ofiara);
    }
}

