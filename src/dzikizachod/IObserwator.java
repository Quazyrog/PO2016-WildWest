package dzikizachod;

/**
 * Interfejs obserwatorów gry (taki listener tego co się dzieje).
 * Warto wiedzieć, że powiadomienia są wysyłane, zanim efekt zostanie nałożony.
 */
public interface IObserwator {
    /**
     * Wysyłane przez grę na początku gry.
     * @param gracze wszyscy gracze biorący udział w rozgrywce w kolejności rozgrywania
     * @param szeryf gracz, który jest szeryfem (spośród tablicy <code>gracze</code>)
     * @param liczbaBandytów ile jest bandytów
     * @param liczbaPomocników ile jest pomocników
     */
    void patrzPoczatekGry(StrategicznyWidokGracza[] gracze, StrategicznyWidokGracza szeryf, int liczbaBandytów, int liczbaPomocników);


    /**
     * Wysyłane na początku nowej tury
     * @param numerTury numer nowej tury (zaczęty od 0)
     */
    void patrzKolejnaTura(int numerTury);


    /**
     * Wysyłane na początku ruchu gracza
     * @param ktoGra gracz, który zaczyna ruch
     */
    void patrzRuchGracza(StrategicznyWidokGracza ktoGra);


    /**
     * Wysyłane, kiedy grajacy gracz dobiera akcje
     * @param ktoGra grający
     * @param a akcja, którą dobrał
     */
    void patrzDobralAkcje(StrategicznyWidokGracza ktoGra, Akcja a);


    /**
     * Wysyłane po rozstrzygnięciu czy dynamit wybuchł.
     * @param ktoGra grający
     * @param wybuchl czy dynamit wybuchł
     */
    void patrzNaDynamit(StrategicznyWidokGracza ktoGra, boolean wybuchl);


    /**
     * Wysyłane, kiedy grający gracz wykona akcję.
     * @param ktoGra grający
     * @param a wykonana akcja
     * @param naKim kto był celem akcji:
     *              - w przypadku leczenia, leczony gracz
     *              – w przypadku strzelania, cel
     *              - w przypadku zwiększenia zasięgu, grający (czyli <code>ktoGra</code>)
     *              - w przypadku dynamitu, <code>null</code>
     */
    void patrzWykonalAkcje(StrategicznyWidokGracza ktoGra, Akcja a, StrategicznyWidokGracza naKim);


    /**
     * Wysyłane, kiedy grający gracz kończy swoją turę.
     * @param ktoGra grający
     */
    void patrzSkonczylTure(StrategicznyWidokGracza ktoGra);


    /**
     * Wysyłane, kiedy ktoś zginie
     * @param ofiara gracz, który został zabity
     * @param zabojca gracz który zabił; jeśli śmierć spowodowana została przez dynamit, to rzucajacy jest zabujcą
     */
    void patrzZabojstwo(StrategicznyWidokGracza ofiara, StrategicznyWidokGracza zabojca);


    /**
     * Wysyłane na końcu gry.
     * @param zakonczenie jak to się wszystko skończyło
     */
    void patrzKoniecGry(Zakonczenie zakonczenie);
}
