package org.apxeolog.shovel.info;

import haven.Buff;
import haven.TexI;
import haven.Text;
import haven.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by APXEOLOG on 02.09.2015.
 * Structure to describe quality info of the Item
 */
public class ItemQualityInfo {
    public static final Color COLOR_ESSENCE = new Color(255, 0, 255);
    public static final Color COLOR_SUBSTANCE = new Color(204, 204, 0);
    public static final Color COLOR_VITALITY = new Color(0, 204, 0);

    private static class ItemQuality {
        public String name;
        public int quality;

        public ItemQuality(String name, int quality) {
            this.name = name;
            this.quality = quality;
        }
    }

    public int substance;
    public int essence;
    public int vitality;

    private int maxValue;
    private Color maxColor;

    public TexI textCache;

    public void setByType(String name, int quality) {
        switch (name) {
            case "Substance": substance = quality; break;
            case "Essence": essence = quality; break;
            case "Vitality": vitality = quality; break;
        }
    }

    public void build() {
        ArrayList<ItemQuality> itemQualities = new ArrayList<>(3);
        itemQualities.add(new ItemQuality("Substance", substance));
        itemQualities.add(new ItemQuality("Essence", essence));
        itemQualities.add(new ItemQuality("Vitality", vitality));
        Collections.sort(itemQualities, (o1, o2) -> o2.quality - o1.quality);
        ItemQuality maxQuality = itemQualities.get(0);
        maxValue = maxQuality.quality;
        switch (maxQuality.name) {
            case "Substance": maxColor = COLOR_SUBSTANCE; break;
            case "Essence": maxColor = COLOR_ESSENCE; break;
            case "Vitality": maxColor = COLOR_VITALITY; break;
        }
        textCache = Utils.renderOutlinedFont(Text.std, Integer.toString(getMaxValue()), getMaxColor(), Color.BLACK, 1);
    }

    public Color getMaxColor() {
        return maxColor;
    }

    public int getMaxValue() {
        return maxValue;
    }
}
