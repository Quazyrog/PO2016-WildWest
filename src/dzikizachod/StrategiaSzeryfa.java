package dzikizachod;

import java.util.ArrayList;

/**
 * Strategia dla szeryfa.
 * Utrzymuje listę graczy, którzy go zaatakowali.
 */
public abstract class StrategiaSzeryfa extends StrategiaOgolna {
    /** Lista żywych graczy, którzy zaatakowali tego gracza. */
    protected ArrayList<ReprezentacjaGracza> paskudniBandyci = new ArrayList<>();

    @Override
    protected void ogarnijWykonalAkcje(ReprezentacjaGracza ktoGra, Akcja a, ReprezentacjaGracza naKim) {
        switch (a) {
            case STRZEL:
                if (naKim.equals(ja()) && !paskudniBandyci.contains(ktoGra))
                    paskudniBandyci.add(ktoGra);
                break;
        }
    }

    @Override
    protected void ogarnijZabojstwo(ReprezentacjaGracza ofiara, ReprezentacjaGracza zabojca) {
        paskudniBandyci.remove(ofiara);
    }
}

