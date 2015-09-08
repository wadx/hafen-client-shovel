package org.apxeolog.shovel.widget;

import haven.*;
import haven.Button;
import haven.Label;
import haven.Window;
import org.apxeolog.shovel.ALS;
import org.apxeolog.shovel.Shovel;
import org.apxeolog.shovel.highlight.HighlightGroup;
import org.apxeolog.shovel.highlight.HighlightManager;
import org.apxeolog.shovel.highlight.HighlightOption;

import java.awt.*;

/**
 * Created by APXEOLOG on 31/08/2015.
 */
public class ConfigurationWnd extends Window {
    public ConfigurationWnd(Coord sz) {
        super(new Coord(400, 450), "Shovel Settings");

        final Tabs tabs = new Tabs(new Coord(15, 50), Coord.z, this);
        Tabs.Tab generalSettings;
        {
            generalSettings = tabs.add(); int y = 10;
            generalSettings.add(new CheckBox("Always show nicknames", Shovel.getSettings().alwaysShowNickname) {
                @Override
                public void changed(boolean val) {
                    Shovel.getSettings().alwaysShowNickname = val;
                    Shovel.saveSettings();
                }
            }, new Coord(15, y));
            generalSettings.add(new CheckBox("Save minimaps", Shovel.getSettings().dumpMinimaps) {
                @Override
                public void changed(boolean val) {
                    Shovel.getSettings().dumpMinimaps = val;
                    Shovel.saveSettings();
                }
            }, new Coord(15, y += 30));
            generalSettings.add(new CheckBox("Disable flavor objects", Shovel.getSettings().showFlavor) {
                @Override
                public void changed(boolean val) {
                    Shovel.getSettings().showFlavor = val;
                    Shovel.saveSettings();
                }
            }, new Coord(15, y += 30));
            generalSettings.add(new CheckBox("Enable friends notification", Shovel.getSettings().showFriendNotifications) {
                @Override
                public void changed(boolean val) {
                    Shovel.getSettings().showFriendNotifications = val;
                    Shovel.saveSettings();
                }
            }, new Coord(15, y += 30));
        }
        Tabs.Tab highlightSettings;
        {
            highlightSettings = tabs.add();
            highlightSettings.add(new Label("Select group"), new Coord(10, 0));
            highlightSettings.add(new Label("Enable or disable icon (dblclick works too)"), new Coord(140, 0));
            TextEntry colorPicker = new TextEntry(130, "");
            HighlightGroup[] highlightGroups = HighlightManager.getHighlightGroups();
            HighlightOptionList highlightOptionList = new HighlightOptionList(210, 10) {
                @Override
                public void change(HighlightOption item) {
                    if (item != null) {
                        colorPicker.settext(HighlightManager.formatColor(
                                item.color != null ? item.color : Color.BLACK));
                    }
                    super.change(item);
                }
            };
            highlightSettings.add(highlightOptionList, new Coord(130, 20));
            HighlightGroupList highlightGroupList = new HighlightGroupList(120, 10, highlightGroups) {
                @Override
                public void change(HighlightGroup item) {
                    if (item != null) {
                        highlightOptionList.setHighlightOptions(item.options);
                        highlightOptionList.setActiveItem(0);
                        highlightOptionList.sb.val = 0;
                    }
                    super.change(item);
                }
            };
            highlightGroupList.setActiveItem(0);
            highlightSettings.add(highlightGroupList, new Coord(0, 20));
            highlightSettings.add(new Button(100, "Enable Group") {
                @Override
                public void click() {
                    HighlightGroup highlightGroup = highlightGroupList.getCurrentItem();
                    if (highlightGroup != null) {
                        for (int i = 0; i < highlightGroup.options.length; i++) {
                            highlightGroup.options[i].enabled = true;
                        }
                        HighlightManager.saveHighlightGroups();
                    }
                }
            }, new Coord(10, 330));
            highlightSettings.add(new Button(100, "Disable Group") {
                @Override
                public void click() {
                    HighlightGroup highlightGroup = highlightGroupList.getCurrentItem();
                    if (highlightGroup != null) {
                        for (int i = 0; i < highlightGroup.options.length; i++) {
                            highlightGroup.options[i].enabled = false;
                        }
                        HighlightManager.saveHighlightGroups();
                    }
                }
            }, new Coord(120, 330));
            highlightSettings.add(new Button(100, "Toggle Item") {
                @Override
                public void click() {
                    HighlightOption highlightOption = highlightOptionList.getCurrentItem();
                    if (highlightOption != null) {
                        highlightOption.enabled = !highlightOption.enabled;
                        HighlightManager.saveHighlightGroups();
                    }
                }
            }, new Coord(230, 330));
            highlightSettings.add(colorPicker, new Coord(0, 370));
            highlightSettings.add(new Button(100, "Set for Group") {
                @Override
                public void click() {
                    HighlightGroup highlightGroup = highlightGroupList.getCurrentItem();
                    if (highlightGroup != null) {
                        Color parsed = HighlightManager.parseColor(colorPicker.text);
                        for (int i = 0; i < highlightGroup.options.length; i++) {
                            highlightGroup.options[i].color = parsed;
                        }
                        HighlightManager.saveHighlightGroups();
                    }
                }
            }, new Coord(140, 360));
            highlightSettings.add(new Button(100, "Set for Current") {
                @Override
                public void click() {
                    HighlightOption highlightOption = highlightOptionList.getCurrentItem();
                    if (highlightOption != null) {
                        Color parsed = HighlightManager.parseColor(colorPicker.text);
                        highlightOption.color = parsed;
                        HighlightManager.saveHighlightGroups();
                    }
                }
            }, new Coord(250, 360));
        }
        tabs.pack();
        add(new Button(100, "General") {
            @Override
            public void click() {
                tabs.showtab(generalSettings);
            }
        }, new Coord(15, 10));
        add(new Button(100, "Highlight") {
            @Override
            public void click() {
                tabs.showtab(highlightSettings);
            }
        }, new Coord(125, 10));
    }

    @Override
    public void wdgmsg(Widget sender, String msg, Object... args) {

    }

    public static class HighlightGroupList extends Listbox<HighlightGroup> {
        private HighlightGroup[] highlightGroups;
        private HighlightGroup currentItem = null;

        public HighlightGroupList(int w, int h, HighlightGroup[] highlightGroups) {
            super(w, h, 30);
            this.highlightGroups = highlightGroups;
        }

        @Override
        protected HighlightGroup listitem(int i) {
            return highlightGroups[i];
        }

        @Override
        public void change(HighlightGroup item) {
            currentItem = item;
        }

        public void setActiveItem(int index) {
            if (index >= highlightGroups.length) return;
            currentItem = highlightGroups[index];
            change(currentItem);
        }

        public HighlightGroup getCurrentItem() {
            return currentItem;
        }

        @Override
        protected int listitems() {
            return highlightGroups.length;
        }

        @Override
        protected void drawitem(GOut g, HighlightGroup item, int i) {
            if (currentItem == item) {
                g.chcolor(64, 64, 64, 255);
                g.frect(Coord.z, new Coord(sz.x, 30));
                g.chcolor();
            }
            g.image(CharWnd.attrf.render(item.name).tex(), new Coord(10, 3));
        }
    }

    public static class HighlightOptionList extends Listbox<HighlightOption> {
        private HighlightOption[] highlightOptions;
        private HighlightOption currentItem = null;

        public HighlightOptionList(int w, int h) {
            super(w, h, 30);
            this.highlightOptions = new HighlightOption[0];
        }

        public void setHighlightOptions(HighlightOption[] highlightOptions) {
            this.highlightOptions = highlightOptions;
        }

        @Override
        protected HighlightOption listitem(int i) {
            return highlightOptions[i];
        }

        @Override
        public void change(HighlightOption item) {
            currentItem = item;
        }

        public void setActiveItem(int index) {
            if (index >= highlightOptions.length) return;
            currentItem = highlightOptions[index];
            change(currentItem);
        }

        public HighlightOption getCurrentItem() {
            return currentItem;
        }

        @Override
        protected int listitems() {
            return highlightOptions.length;
        }

        @Override
        protected void drawitem(GOut g, HighlightOption item, int i) {
            if (currentItem == item) {
                g.chcolor(64, 64, 64, 255);
                g.frect(Coord.z, new Coord(sz.x, 30));
                g.chcolor();
            }
            if (item.color != null) {
                g.chcolor(item.color);
                g.fellipse(new Coord(20, 15), new Coord(6, 6));
                g.chcolor();
            } else if (item.icon != null) {
                Tex icon = null;
                try {
                    icon = Resource.loadtex(item.icon);
                } catch (Exception ex) {
                    icon = WItem.missing.layer(Resource.imgc).tex();
                }
                g.image(icon, new Coord(6, 1), new Coord(28, 28));
            }
            if (item.enabled)
                g.chcolor(0, 102, 0, 255);
            g.image(CharWnd.attrf.render(item.name).tex(), new Coord(40, 3));
            if (item.enabled)
                g.chcolor();
        }

        private long lastClickTime = 0;

        private void dblclick(HighlightOption item) {
            if (currentItem != null) {
                currentItem.enabled = !currentItem.enabled;
            }
        }

        @Override
        protected void itemclick(HighlightOption item, int button) {
            if (button == 1) {
                if (item != null && currentItem == item && (System.currentTimeMillis() - lastClickTime) < 1000) {
                    dblclick(item);
                    lastClickTime = 0;
                } else {
                    change(item);
                    lastClickTime = System.currentTimeMillis();
                }
            }
        }
    }
}
