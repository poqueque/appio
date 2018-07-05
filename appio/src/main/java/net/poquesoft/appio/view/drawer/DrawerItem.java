package net.poquesoft.appio.view.drawer;

import android.content.Context;
import android.graphics.Typeface;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import net.poquesoft.appio.authentication.Authentication;

/**
 * Item for drawer
 */

public class DrawerItem {

    public static final int DIVIDER = 100;
    public static final int SECTION_TITLE = 200;
    public static final int PRIMARY_DRAWER_ITEM = 1;
    public static final int SECONDARY_DRAWER_ITEM = 2;
    public static final int USER_ITEM = 3;
    private int id;
    private int type;
    private FontAwesome.Icon icon;
    private String text;
    private String tag;
    private DrawerConfig config;
    private static final FontAwesome.Icon userIcon = FontAwesome.Icon.faw_user;

    //Clicable item
    public DrawerItem(int id, int type, FontAwesome.Icon  icon, String text) {
        this.id = id;
        this.type = type;
        this.icon = icon;
        this.text = text;
    }

    //Profile
    public DrawerItem(int id, int type) {
        this.id = id;
        this.type = type;
    }

    //Separator
    public DrawerItem(int type) {
        this.type = type;
    }

    //Title
    public DrawerItem(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public IDrawerItem getIDrawerItem() {
        if (type == PRIMARY_DRAWER_ITEM)
            return new PrimaryDrawerItem().withName(text).withIcon(icon).withTypeface(config.getTypeface()).withTag(this);
        if (type == SECONDARY_DRAWER_ITEM)
            return new SecondaryDrawerItem().withName(text).withIcon(icon).withTag(this);
        if (type == USER_ITEM) {
            String userNameToShow = Authentication.getName();
            if (userNameToShow == null) userNameToShow = "Iniciar Sesión / Registro";
            return new PrimaryDrawerItem().withName(userNameToShow).withIcon(userIcon).withTypeface(config.getTypeface()).withTag(this);
        }
        if (type == DIVIDER)
            return new DividerDrawerItem().withSelectable(false).withTag(this);
        if (type == SECTION_TITLE)
            return new SecondaryDrawerItem().withName(text).withTypeface(Typeface.defaultFromStyle(Typeface.BOLD)).withSelectable(false).withTag(this);
        return null;
    }

    public boolean is(IDrawerItem iDrawerItem) {
        return (iDrawerItem.getTag() == this);
    }

    public void click(Context c) {
        if (config != null && config.getActionListener() != null)
            config.getActionListener().onAction(c, id);
    }

    public void setConfig(DrawerConfig config) {
        this.config = config;
    }

    public boolean is(int type) {
        return (this.type == type);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
