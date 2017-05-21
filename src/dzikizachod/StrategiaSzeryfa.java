package dzikizachod;

import java.util.Random;

/**
 * Strategia dla szeryfa.
 * Utrzymuje listę graczy, którzy go zaatakowali. Utrzymuje listę paskud na, które go zaatakowały. Nic jednak nie stoi
 * na przeszkodzie, aby dodawać do niej własne psakudy w podklasach. Trzeba natomiast wywoływac metodę patrzZabojstwo,
 * aby usuwać paskudy.
 */
public abstract class StrategiaSzeryfa extends StrategiaOgolna {
    public StrategiaSzeryfa(Random rng) {
        super(rng);
    }


    public StrategiaSzeryfa() {}
}

