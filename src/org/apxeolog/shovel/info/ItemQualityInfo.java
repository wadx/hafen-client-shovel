package org.apxeolog.shovel.info;

import haven.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by APXEOLOG on 02.09.2015.
 * Structure to describe quality info of the Item
 */
public class ItemQualityInfo {
    public static final Color COLOR_ESSENCE = new Color(255, 0, 255);
    public static final Color COLOR_SUBSTANCE = new Color(204, 204, 0);
    public static final Color COLOR_VITALITY = new Color(0, 204, 0);
    public static final Color COLOR_AVERAGE = new Color(116, 136, 204);
    public static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("###.##");

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

    public double average;

    private int maxValue;
    private Color maxColor;

    public TexI textCache;
    public TexI averageTextCache;

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
        average = Math.sqrt((substance * substance + essence * essence + vitality * vitality) / 3.0d);
        textCache = Utils.renderOutlinedFont(Text.std, Integer.toString(getMaxValue()), getMaxColor(), Color.BLACK, 1);
        averageTextCache = Utils.renderOutlinedFont(Text.std, DOUBLE_FORMAT.format(average), COLOR_AVERAGE, Color.BLACK, 1);
    }

    public Color getMaxColor() {
        return maxColor;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public static final Comparator<GItem> MaxQualityComparatorAsc = (o1, o2) -> o1.getItemQualityInfo().getMaxValue() - o2.getItemQualityInfo().getMaxValue();
    public static final Comparator<GItem> MaxQualityComparatorDesc = (o1, o2) -> o2.getItemQualityInfo().getMaxValue() - o1.getItemQualityInfo().getMaxValue();
    public static final Comparator<GItem> AverageQualityComparatorAsc = (o1, o2) -> (int) Math.signum(o1.getItemQualityInfo().average - o2.getItemQualityInfo().average);
    public static final Comparator<GItem> AverageQualityComparatorDesc = (o1, o2) -> (int) Math.signum(o2.getItemQualityInfo().average - o1.getItemQualityInfo().average);
}
