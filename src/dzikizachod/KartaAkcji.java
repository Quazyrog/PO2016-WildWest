package dzikizachod;

/**
 * Reprezentuje akcję poza pulą z której dobieramy.
 */
class KartaAkcji {
    /** Akcja na tej karcie */
    private Akcja akcja;

    /** Pula, do której karta mza zostać odłożona po wykorzystaniu. <c>null</c>, gdy została już odłożona. */
    private PulaAkcji rodzic;

    /**
     * Tworzy nową kartę akcji przywiązaną do podanej puli.
     * @param rodzic pula, z której pochodzi karta
     * @param akcja akcja na karcie
     */
    KartaAkcji(PulaAkcji rodzic, Akcja akcja) {
        if (rodzic == null)
            throw new NullPointerException("rodzic == null");
        this.akcja = akcja;
        this.rodzic = rodzic;
    }

    /**
     * Zwraca akcje przypisaną do karty
     * @return akcje na karcie
     */
    public Akcja akcja() {
        return akcja;
    }

    /**
     * Odkłada kartę z powrotem do puli, o ile nie została wcześniej odłożona.
     */
    public void odrzuc() {
        if (rodzic == null)
            return;
        rodzic.odrzuc(akcja);
        rodzic = null;
    }
}
