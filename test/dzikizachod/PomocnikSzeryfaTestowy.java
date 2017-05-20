package dzikizachod;

class PomocnikSzeryfaTestowy extends PomocnikSzeryfa {
    public PomocnikSzeryfaTestowy() {
        super(new StrategiaPomocnikaSzeryfaDomyslna());
    }

    @Override
    public boolean czyWykonujeRuch() {
        return true;
    }


    @Override
    void graj() {}
}
