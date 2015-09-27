/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven;

import org.apxeolog.shovel.Settings;
import org.apxeolog.shovel.Shovel;
import org.apxeolog.shovel.info.ItemQualityInfo;

import java.util.*;

public class Inventory extends Widget implements DTarget {
    public static final Tex invsq = Resource.loadtex("gfx/hud/invsq");
    public static final Coord sqsz = new Coord(33, 33);
    Coord isz;
    Map<GItem, WItem> wmap = new HashMap<GItem, WItem>();

	private boolean locked = false;

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public void toggleLocked() {
		this.locked = !this.locked;
	}

	@RName("inv")
    public static class $_ implements Factory {
	public Widget create(Widget parent, Object[] args) {
	    return(new Inventory((Coord)args[0]));
	}
    }

    public void draw(GOut g) {
	Coord c = new Coord();
	for(c.y = 0; c.y < isz.y; c.y++) {
	    for(c.x = 0; c.x < isz.x; c.x++) {
		g.image(invsq, c.mul(sqsz));
	    }
	}
	super.draw(g);
    }
	
    public Inventory(Coord sz) {
	super(invsq.sz().add(new Coord(-1, -1)).mul(sz).add(new Coord(1, 1)));
	isz = sz;
    }
    
    public boolean mousewheel(Coord c, int amount) {
	if(ui.modshift) {
	    Inventory minv = getparent(GameUI.class).maininv;
	    if(minv != this) {
		if(amount < 0)
		    wdgmsg("invxf", minv.wdgid(), 1);
		else if(amount > 0)
		    minv.wdgmsg("invxf", this.wdgid(), 1);
	    }
	}
	return(true);
    }
    
    public void addchild(Widget child, Object... args) {
	add(child);
	Coord c = (Coord)args[0];
	if(child instanceof GItem) {
	    GItem i = (GItem)child;
	    wmap.put(i, add(new WItem(i), c.mul(sqsz).add(1, 1)));
	}
    }
    
    public void cdestroy(Widget w) {
	super.cdestroy(w);
	if(w instanceof GItem) {
	    GItem i = (GItem)w;
	    ui.destroy(wmap.remove(i));
	}
    }
    
    public boolean drop(Coord cc, Coord ul) {
	wdgmsg("drop", ul.add(sqsz.div(2)).div(invsq.sz()));
	return(true);
    }

	@Override
	public void wdgmsg(Widget sender, String msg, Object... args) {
		if (isLocked()) return;
		String itemMsg = null; Comparator<GItem> comparator = null;
		switch (msg) {
			case "transfer_all":
				itemMsg = "transfer";
				comparator = Shovel.getSettings().qualityDisplayType == Settings.QualityDisplayType.MAX
						? ItemQualityInfo.MaxQualityComparatorDesc : ItemQualityInfo.AverageQualityComparatorDesc;
				break;
			case "transfer_all_asc":
				itemMsg = "transfer";
				comparator = Shovel.getSettings().qualityDisplayType == Settings.QualityDisplayType.MAX
						? ItemQualityInfo.MaxQualityComparatorAsc : ItemQualityInfo.AverageQualityComparatorAsc;
				break;
			case "drop_all":
				itemMsg = "drop";
				comparator = Shovel.getSettings().qualityDisplayType == Settings.QualityDisplayType.MAX
						? ItemQualityInfo.MaxQualityComparatorDesc : ItemQualityInfo.AverageQualityComparatorDesc;
				break;
			case "drop_all_asc":
				itemMsg = "drop";
				comparator = Shovel.getSettings().qualityDisplayType == Settings.QualityDisplayType.MAX
						? ItemQualityInfo.MaxQualityComparatorAsc : ItemQualityInfo.AverageQualityComparatorAsc;
				break;
		}
		if (itemMsg != null && comparator != null) {
			try {
				if (!(args[0] instanceof String)) return;
				String resName = (String) args[0];
				ArrayList<GItem> items = new ArrayList<>();
				items.addAll(wmap.keySet());
				items.sort(comparator);
				Iterator<GItem> iterator = items.iterator();
				while (iterator.hasNext()) {
					GItem item = iterator.next();
					if (resName.equals(item.getItemName())) {
						item.wdgmsg(itemMsg, Coord.z);
					}
				}
			} catch (Exception ex) {

			}
		} else super.wdgmsg(sender, msg, args);
	}

	public boolean iteminteract(Coord cc, Coord ul) {
	return(false);
    }
	
    public void uimsg(String msg, Object... args) {
	if(msg == "sz") {
	    isz = (Coord)args[0];
	    resize(invsq.sz().add(new Coord(-1, -1)).mul(isz).add(new Coord(1, 1)));
	}
    }
}
