package dzikizachod;

import java.util.List;
import java.util.Random;

/**
 * Sprytna strategia bandyty
 */
public class StrategiaBandytySprytna extends StrategiaBandytyDomyslna {
    private boolean krewTowarzyszaNaMychRekach = false;


    public StrategiaBandytySprytna(Random rng) {
        super(rng);
    }


    public StrategiaBandytySprytna() {}

    @Override
    public void patrzKolejnaTura(int numerTury) {
        if (numerTury == 1)
            krewTowarzyszaNaMychRekach = false;
    }

    @Override
    void graj() throws BladKonrtoleraWyjatek {
        super.graj();
        if (bijSzeryfaJakMozna())
            return;
        if (krewTowarzyszaNaMychRekach) {
            super.graj();
        } else {
            List<StrategicznyWidokGracza> towazysze = graczeWZasieguKtorzySa(TozsamoscGracza.BANDYTA, 0);
            StrategicznyWidokGracza najslabszy = null;
            for (StrategicznyWidokGracza t : towazysze) {
                if (najslabszy == null || najslabszy.pz() > t.pz())
                    najslabszy = t;
            }
            if (najslabszy != null && najslabszy.pz() <= ja().ileAkcji(Akcja.STRZEL)) {
                while (najslabszy.pz() > 0)
                    akcjaStrzel(najslabszy);
            } else {
                //Z forum, że niby tak ma być
                super.graj();
            }
        }
    }
}
