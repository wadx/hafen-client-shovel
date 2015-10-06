package haven;

/**
 * Created by Kerrigan on 06.10.2015.
 */
public class EquipSlot extends Widget implements DTarget {
    private int equipSlotIndex = -1;

    public EquipSlot(int slot) {
        super(new Coord(40, 40));
        equipSlotIndex = slot;
    }

    private Equipory getEquip() {
        Window eq = GameUI.instance.equwnd;
        if (eq == null)
            return null;

        for (Widget w = eq.lchild; w != null; w = w.prev)
            if (w instanceof Equipory)
                return (Equipory)w;

        return null;
    }

    @Override
    public boolean drop(Coord cc, Coord ul) {
        Equipory eq = getEquip();
        if (eq != null) {
            eq.wdgmsg("drop", equipSlotIndex);
            return true;
        }

        return false;
    }

    @Override
    public void draw(GOut g) {
        Equipory eq = getEquip();
        if (eq == null)
            return;

        g.chcolor(0, 0, 0, 170);
        g.frect(Coord.z, sz);
        g.chcolor(240, 230, 175, 255);
        g.rect(Coord.z, sz);

        if (eq.equipItems[equipSlotIndex] != null) {
            WItem witem = eq.equipItems[equipSlotIndex];
            GItem item = witem.item;
            GSprite fanta = item.spr();
            GOut drawer = g.reclip(new Coord(4, 4), g.sz);
            if (fanta != null) {
                Coord fantaSize = fanta.sz();
                drawer.defstate();
                witem.drawmain(drawer, fanta);
                drawer.defstate();
                if (item.num > 0)
                    drawer.atext(Integer.toString(item.num), fantaSize, 1, 1);
                else if (witem.itemnum.get() != null)
                    drawer.aimage(witem.itemnum.get(), fantaSize, 1, 1);
            } else {
                drawer.image(WItem.missing.layer(Resource.imgc).tex(), Coord.z, sz);
            }
        }
    }

    @Override
    public boolean iteminteract(Coord cc, Coord ul) {
        Equipory eq = getEquip();
        if (eq != null && eq.equipItems[equipSlotIndex] != null) {
            return eq.equipItems[equipSlotIndex].iteminteract(cc, ul);
        }

        return false;
    }

    @Override
    public boolean mousedown(Coord c, int button) {
        Equipory eq = getEquip();
        if (eq != null && eq.equipItems[equipSlotIndex] != null) {
            eq.equipItems[equipSlotIndex].mousedown(c, button);
            return true;
        }

        return false;
    }
}
