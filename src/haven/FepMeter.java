package haven;

/**
 * Created by Kerrigan on 09.10.2015.
 */
import org.apxeolog.shovel.Shovel;

import java.awt.*;
import java.util.List;

public class FepMeter extends Widget {
    private static final Tex bg = Resource.loadtex("apxeolog/ui/fep");

    private final CharWnd.FoodMeter food;

    private static final Text.Foundry tipF = new Text.Foundry(Text.sans, 10);
    private Tex valueTex;

    public FepMeter(CharWnd.FoodMeter food) {
        super(IMeter.fsz);
        this.food = food;
    }

    private void calcValueText() {
        List<CharWnd.FoodMeter.El> els = food.els;
        double sum = 0.0;
        for(CharWnd.FoodMeter.El el : els) {
            sum += el.a;
        }
        valueTex = Text.renderstroked(String.format("%s/%s", Utils.odformat2(sum, 2), Utils.odformat(food.cap, 2)), Color.WHITE, Color.BLACK, tipF).tex();
    }

    @Override
    public void draw(GOut g) {
        Coord isz = IMeter.msz;
        Coord off = IMeter.off;
        g.chcolor(0, 0, 0, 255);
        g.frect(off, isz);
        g.chcolor();
        double x = 0;
        int w = isz.x;
        for(CharWnd.FoodMeter.El el : food.els) {
            int l = (int)Math.floor((x / food.cap) * w);
            int r = (int)Math.floor(((x += el.a) / food.cap) * w);
            try {
                Color col = el.ev().col;
                g.chcolor(new Color(col.getRed(), col.getGreen(), col.getBlue(), 255));
                g.frect(off.add(l, 0), new Coord(r - l, isz.y));
            } catch(Loading e) {
            }
        }
        if (Shovel.getSettings().showMeterValues) {
            calcValueText();
            g.chcolor();
            g.image(valueTex, sz.div(2).sub(valueTex.sz().div(2)).add(10, -1));
        }
        g.chcolor();
        g.image(bg, Coord.z);
    }

    @Override
    public Object tooltip(Coord c, Widget prev) {
        return food.tooltip(c, prev);
    }
}
