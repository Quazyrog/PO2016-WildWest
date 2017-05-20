package dzikizachod;

class SzeryfTestowy extends Szeryf {
    public SzeryfTestowy() {
        super(new StrategiaSzeryfaDomyslna());
    }

    @Override
    public boolean czyWykonujeRuch() {
        return true;
    }

    @Override
    void graj() {}
}