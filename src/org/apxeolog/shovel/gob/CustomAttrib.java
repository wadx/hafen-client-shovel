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

    public static class DriedHideAttrib extends CustomAttrib {

        public DriedHideAttrib(Gob gob) {
            super(gob);
        }
    }
}
