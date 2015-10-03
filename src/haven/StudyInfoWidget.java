package haven;

/**
 * Created by Kerrigan on 03.10.2015.
 */
public class StudyInfoWidget extends Widget {
    private static final Tex backgrund = Resource.loadtex("gfx/hud/wnd/lg/bg");

    public int exp, attr, lp;
    private final Text.UText<?> lpTex = new Text.UText<Integer>(Text.std) {
        public Integer value() {return(lp);}
        public String text(Integer v) {return(Utils.thformat(v));}
    };
    private final Text.UText<?> attrTex = new Text.UText<String>(Text.std) {
        public String value() {return(attr + "/" + ui.sess.glob.cattr.get("int").comp);}
    };
    private final Text.UText<?> expTex = new Text.UText<Integer>(Text.std) {
        public Integer value() {return(exp);}
        public String text(Integer v) {return(Integer.toString(exp) + "/" + GameUI.instance.chrwdg.enc);}
    };


    public StudyInfoWidget (Coord sz) {
        super(sz);
        add(new Label("Att:"), 2, 2);
        add(new Label("Exp:"), 2, 17);
        add(new Label("LP:"), 2, 32);
    }

    private void recalc(Widget s) {
        int texp = 0, tattr = 0, tlp = 0;
        for(GItem item : s.children(GItem.class)) {
            try {
                Curiosity ci = ItemInfo.find(Curiosity.class, item.info());
                if(ci != null) {
                    tlp += ci.exp;
                    tattr += ci.mw;
                    texp += ci.enc;
                }
            } catch(Loading l) {
                //
            }
        }

        this.exp = texp; this.attr = tattr; this.lp = tlp;
    }

    @Override
    public void draw(GOut g) {
        Widget s = GameUI.instance.studyWidget;
        if (s != null) {
            g.image(backgrund, Coord.z);
            recalc(s);
            super.draw(g);
            g.chcolor(255, 192, 255, 255);
            g.aimage(attrTex.get().tex(), new Coord(sz.x - 4, 2), 1.0, 0.0);
            g.chcolor(255, 255, 192, 255);
            g.aimage(expTex.get().tex(), new Coord(sz.x - 4, 17), 1.0, 0.0);
            g.chcolor(192, 192, 255, 255);
            g.aimage(lpTex.get().tex(), new Coord(sz.x - 4, 32), 1.0, 0.0);
        }
    }
}
