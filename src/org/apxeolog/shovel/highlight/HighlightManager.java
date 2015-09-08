package org.apxeolog.shovel.highlight;

import com.google.gson.Gson;
import org.apxeolog.shovel.ALS;
import org.apxeolog.shovel.Shovel;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by APXEOLOG on 08/09/2015.
 */
public class HighlightManager {

    public static HighlightGroup[] loadHighlightData() {
        HighlightGroup[] highlightGroups = new HighlightGroup[0];
        File highlightFile = new File(Shovel.getWorkingDirectory(), "highlight.json");
        try {
            if (highlightFile.exists()) {
                Gson gson = new Gson();
                highlightGroups = gson.fromJson(new FileReader(highlightFile), highlightGroups.getClass());
            }
        } catch (Exception ex) {
            ALS.alDebugPrint("Cannot load highlight data:", ex.getMessage());
        }
        return highlightGroups;
    }
}
