package org.apxeolog.shovel.highlight;

import java.awt.*;

/**
 * Created by APXEOLOG on 08/09/2015.
 */
public class HighlightOption {
    public String name = "";
    public String resource = null;
    public String icon = null;
    public Color color = null;
    public boolean equalMatch = false;
    public boolean enabled;

    public boolean match(String resName) {
        if (resName == null || resource == null) return false;
        if (equalMatch)
            return resName.equals(resource);
        else
            return resName.contains(resource);
    }
}
