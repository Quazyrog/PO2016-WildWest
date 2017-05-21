package dzikizachod;

/**
 * Pozwala zliczaczowi strzelać.
 * Dzzięki temu, strategiczny zliczacz może strzeclić do innego gracza, chociaż to normalnie wymagałoby to, aby
 * implementacja tej metody znajdowała się w strategii (a wtedy musiała by być napisana dwa razy
 * <code>zwalczPaskudy</code>).
 */
public interface IPistoletStrategicznegoZliczacza {
    /**
     * Strzela do podanego gracza.
     * Wywołanie tej metody sprawia, że strategia odpowiedzialna za utworzenie tego obiektu strzela do podanego grazca.
     * @param wKogo cel
     * @throws BladKonrtoleraWyjatek kiedy nie uda się strzelić do tego gracza (np bo koniec gry)
     */
    void strzel(StrategicznyWidokGracza wKogo) throws BladKonrtoleraWyjatek;
}
