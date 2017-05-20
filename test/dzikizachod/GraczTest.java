package dzikizachod;

import org.junit.Test;

import static org.junit.Assert.*;

class TestowyGracz extends Gracz {
    TestowyGracz() {
        super(new StrategiaWygrywajaca(), 5);
    }

    @Override
    public TozsamoscGracza tozsamosc() {
        return TozsamoscGracza.NIEZNANA;
    }

    @Override
    public boolean czyWykonujeRuch() {
        return true; //Bo musi się dać debugować
    }
}


public class GraczTest {
    private Gracz gracze[];
    private Gra gra;

    public GraczTest() {
        gracze = new Gracz[]{
                new TestowyGracz(),
                new TestowyGracz(),
                new TestowyGracz(),
                new TestowyGracz(),
                new TestowyGracz(),
                new TestowyGracz()
        };
        gra = new Gra(){
            @Override
            public int liczbaGraczy() {
                return gracze.length;
            }

            @Override
            Gracz graczONumerze(int numer) {
                return gracze[numer];
            }
        };

        for (int i = 1; i < gracze.length - 1; ++i)
            gracze[i].przygotujDoGry(gra, gracze[i - 1], gracze[i + 1], i);
        gracze[0].przygotujDoGry(gra, gracze[gracze.length - 1], gracze[1], 0);
        gracze[gracze.length - 1].przygotujDoGry(gra, gracze[gracze.length - 2], gracze[0], gracze.length - 1);
    }

    @Test
    public void przeskokiPoKole() {
        assertSame(gracze[0].dalekiSasiad(0), gracze[0]);
        assertSame(gracze[0].dalekiSasiad(3), gracze[3]);
        assertSame(gracze[0].dalekiSasiad(-3), gracze[gracze.length - 3]);
        assertSame(gracze[3].dalekiSasiad(-3), gracze[0]);
        assertSame(gracze[gracze.length - 3].dalekiSasiad(3), gracze[0]);

        assertSame(gracze[2].dalekiSasiad(3), gracze[5]);
        assertSame(gracze[2].dalekiSasiad(-3), gracze[gracze.length - 1]);
        assertSame(gracze[5].dalekiSasiad(-3), gracze[2]);
        assertSame(gracze[gracze.length - 1].dalekiSasiad(3), gracze[2]);
    }

    @Test
    public void odleglosci() {
        assertEquals(1, gracze[0].odlegloscOd(gracze[1]));
        assertEquals(1, gracze[0].odlegloscOd(gracze[gracze.length - 1]));
        assertEquals(3, gracze[3].odlegloscOd(gracze[0]));
        assertEquals(2, gracze[3].odlegloscOd(gracze[1]));
        assertEquals(2, gracze[3].odlegloscOd(gracze[gracze.length - 1]));
    }

    @Test
    public void akcje() {
        Gracz dobryRew = gracze[0];
        Gracz bliskiCel = gracze[1];
        Gracz dalekiCel = gracze[3];
        dobryRew.dobierz(Akcja.STRZEL);
        dobryRew.dobierz(Akcja.STRZEL);
        dobryRew.dobierz(Akcja.STRZEL);
        dobryRew.dobierz(Akcja.ULECZ);
        dobryRew.dobierz(Akcja.ULECZ);
        dobryRew.dobierz(Akcja.ULECZ);
        dobryRew.dobierz(Akcja.ULECZ); //niewykorzystana
        dobryRew.dobierz(Akcja.ZASIEG_PLUS_DWA);
        dobryRew.dobierz(Akcja.ZASIEG_PLUS_JEDEN); //niewykorzystana
        dobryRew.dobierz(Akcja.ZASIEG_PLUS_JEDEN); //niewykorzystana
        dobryRew.dobierz(Akcja.DYNAMIT); //niewykorzystana

        try {
            //Ulecz się i samobój
            dobryRew.akcjaUlecz(dobryRew);
            assertEquals(dobryRew.maksymalnePZ(), dobryRew.pz());
            dobryRew.akcjaStrzel(dobryRew);
            assertEquals(dobryRew.maksymalnePZ() - 1, dobryRew.pz());

            //Strzel i ulecz sąsiada
            dobryRew.akcjaStrzel(bliskiCel);
            assertEquals(bliskiCel.maksymalnePZ() - 1, bliskiCel.pz());
            dobryRew.akcjaUlecz(bliskiCel);
            dobryRew.akcjaUlecz(bliskiCel);
            assertEquals(bliskiCel.maksymalnePZ(), bliskiCel.pz());

            //Strzel poza zasięg
            try {
                dobryRew.akcjaStrzel(dalekiCel);
                fail();
            } catch (PozaZasiegiemWyjatek ignored) {}

            dobryRew.akcjaZasiegPlusDwa();
            assertEquals(dobryRew.zasieg(), 3);

            //Teraz w zasieg
            dobryRew.akcjaStrzel(dalekiCel);
            assertEquals(dalekiCel.maksymalnePZ() - 1, dalekiCel.pz());
        } catch (BladKonrtoleraWyjatek e) {
            e.printStackTrace();
            fail();
        }

        //Sprawdź pozostałe akcje
        assertEquals(0, dobryRew.ileAkcji(Akcja.STRZEL));
        assertEquals(1, dobryRew.ileAkcji(Akcja.ULECZ));
        assertEquals(2, dobryRew.ileAkcji(Akcja.ZASIEG_PLUS_JEDEN));
        assertEquals(0, dobryRew.ileAkcji(Akcja.ZASIEG_PLUS_DWA));
        assertEquals(1, dobryRew.ileAkcji(Akcja.DYNAMIT));
    }
}
