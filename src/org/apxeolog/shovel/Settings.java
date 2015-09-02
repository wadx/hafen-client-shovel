package org.apxeolog.shovel;

/**
 * Created by APXEOLOG on 31/08/2015.
 * Settings class
 */
public class Settings {
    public boolean dumpMinimaps = true;
    public boolean alwaysShowNickname = true;
    public boolean showFlavor = false;
    public boolean nightvision = true;

    // Runtime options, don't save them
    transient public boolean debugMode = false;
    transient public boolean checkedForNewReleases = false;
}
