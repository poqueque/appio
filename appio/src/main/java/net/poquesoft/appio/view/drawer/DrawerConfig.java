package net.poquesoft.appio.view.drawer;

import android.graphics.Typeface;

import net.poquesoft.appio.view.listeners.ContextActionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration of the drawer
 */

public class DrawerConfig {

    public static final int NONE = -1;
    private List<DrawerItem> drawerItems = new ArrayList<>();
    private ContextActionListener drawerActionListener;
    private int defaultSelection = NONE;
    private Typeface typeface;

    private int headerLayoutResource;


    public void setHeaderLayout(int headerLayoutResource) {
        this.headerLayoutResource = headerLayoutResource;
    }

    public void addDrawerItem(DrawerItem drawerItem) {
        drawerItem.setConfig(this);
        drawerItems.add(drawerItem);
    }

    public void setActionListener(ContextActionListener drawerActionListener) {
        this.drawerActionListener = drawerActionListener;
    }

    public void setDefaultSelection(int defaultSelection) {
        this.defaultSelection = defaultSelection;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public ContextActionListener getActionListener() {
        return drawerActionListener;
    }

    public int getDefaultSelection() {
        return defaultSelection;
    }

    public int getHeaderLayout() {
        return headerLayoutResource;
    }

    public List<DrawerItem> getDrawerItems() {
        return drawerItems;
    }

    public Typeface getTypeface() {
        if (typeface == null) return Typeface.defaultFromStyle(Typeface.NORMAL);
        return typeface;
    }
}
