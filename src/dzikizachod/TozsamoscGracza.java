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
}
