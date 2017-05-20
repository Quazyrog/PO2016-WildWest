package dzikizachod;

/**
 * Tożsamość gracza z punktu widzenia innego gracza.
 */
public enum TozsamoscGracza {
    /** Gracz jesy szeryfem */
    SZERYF,

    /** Gracz jest pomocnikiem szeryfa */
    POMOCNIK_SZERYFA,

    /** Gracz jest bandytą */
    BANDYTA,

    /** Tamten gracz nie wie, kim jest ten gracz */
    NIEZNANA //Chociaż właściwie, jak nie jestem szeryfrm, to jestem vice
    ;


    @Override
    public String toString() {
        switch (this) {
            case SZERYF:
                return "Szeryf";
            case POMOCNIK_SZERYFA:
                return "Pomocnik Szeryfa";
            case BANDYTA:
                return "Bandyta";
            case NIEZNANA:
                return "Szczerbatek";
        }
        return "Święty Mikołaj";
    }
}
