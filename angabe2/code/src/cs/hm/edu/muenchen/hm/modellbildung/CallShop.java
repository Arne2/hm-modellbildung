package cs.hm.edu.muenchen.hm.modellbildung;

import cs.hm.edu.muenchen.hm.modellbildung.time.Simulator;

/**
 * Created by Arne on 27.04.2017.
 */
public class CallShop {

    public static void main(String[] args) {
        Simulator sim = new Simulator();
        sim.doUntil(10000000);

    }
}
