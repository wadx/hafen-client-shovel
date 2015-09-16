package org.apxeolog.shovel.gob;

import haven.Gob;
import haven.MessageBuf;
import haven.Resource;
import org.apxeolog.shovel.highlight.HighlightManager;
import org.apxeolog.shovel.highlight.HighlightOption;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by APXEOLOG on 15.09.2015.
 * Collection of methods for extending Gob with custom attributes
 */
public class GobExtendManager {
    public static void initResDrawable(Gob gob, Resource resource, MessageBuf sdt) {
        if (resource.name == null) return;
        // Drying frame notification
        if ("gfx/terobjs/dframe".equals(resource.name)) {
            gob.addOverlayListener(new OverlayListener() {
                private WeakReference<Gob.Overlay> overlayWeakReference;

                @Override
                public void init(Gob gob, Gob.Overlay overlay) {
                    try {
                        String resName = overlay.res.get().name;
                        if (resName != null && resName.contains("hide") && !resName.endsWith("-blood")) {
                            // Add Attrib
                            overlayWeakReference = new WeakReference<Gob.Overlay>(overlay);
                            gob.setattr(new CustomAttrib.ReadyHideOrTubeAttrib(gob));
                        }
                    } catch (Exception ex) {

                    }
                }

                @Override
                public void remove(Gob gob, Gob.Overlay overlay) {
                    if (overlayWeakReference.get() == overlay) {
                        gob.delattr(CustomAttrib.ReadyHideOrTubeAttrib.class);
                    }
                }
            });
        }
        // Tanning tube
        if ("gfx/terobjs/ttub".equals(resource.name)) {
            if (sdt.rbuf.length > 0 && (sdt.rbuf[0] == 8 || sdt.rbuf[0] == 10)) {
                gob.setattr(new CustomAttrib.ReadyHideOrTubeAttrib(gob, 60));
            } else {
                gob.delattr(CustomAttrib.ReadyHideOrTubeAttrib.class);
            }
        }
        // Highlight manager
        ArrayList<HighlightOption> options = HighlightManager.getFinalHighlightData();
        for (HighlightOption option : options) {
            if (option.match(resource.name)) {
                gob.setattr(new CustomAttrib.HighlightAttrib(gob, option));
                break;
            }
        }
        // Crops stages
        if (resource.name.startsWith("gfx/terobjs/plants")
                && !resource.name.equals("gfx/terobjs/plants/trellis")) {
            if (sdt.rbuf.length > 0) {
                gob.setattr(new CustomAttrib.CropStageAttrib(gob, sdt.rbuf[0]));
            }
        }
        // Tree stages
        if (resource.name.startsWith("gfx/terobjs/trees")) {
            if (sdt.rbuf.length > 0 && sdt.rbuf[0] < 100) {
                gob.setattr(new CustomAttrib.CropStageAttrib(gob, sdt.rbuf[0]));
            }
        }
    }
}
