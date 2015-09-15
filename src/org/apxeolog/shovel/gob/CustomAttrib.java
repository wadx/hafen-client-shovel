package org.apxeolog.shovel.gob;

import haven.GAttrib;
import haven.Gob;

/**
 * Created by APXEOLOG on 15.09.2015.
 */
public class CustomAttrib extends GAttrib {
    public CustomAttrib(Gob gob) {
        super(gob);
    }

    public static class ReadyHideOrTubeAttrib extends CustomAttrib {
        public int height = 0;

        public ReadyHideOrTubeAttrib(Gob gob) {
            this(gob, 100);
        }

        public ReadyHideOrTubeAttrib(Gob gob, int val) {
            super(gob);
            height = val;
        }
    }
}
