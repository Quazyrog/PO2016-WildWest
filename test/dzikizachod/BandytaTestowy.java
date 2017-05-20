package dzikizachod;


class BandytaTestowy extends Bandyta {
    public BandytaTestowy() {
        super(new StrategiaBandytyDomyslna());
    }

    @Override
    public boolean czyWykonujeRuch() {
        return true;
    }

    @Override
    void graj() {}
}
