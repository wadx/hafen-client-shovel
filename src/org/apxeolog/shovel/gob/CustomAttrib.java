package org.apxeolog.shovel.gob;

import haven.GAttrib;
import haven.Gob;
import org.apxeolog.shovel.highlight.HighlightOption;

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

    public static class HighlightAttrib extends CustomAttrib {
        public HighlightOption highlightOption;

        public HighlightAttrib(Gob gob, HighlightOption highlightOption) {
            super(gob);
            this.highlightOption = highlightOption;
        }
    }
}
