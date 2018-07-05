package net.poquesoft.appio.view;

import net.poquesoft.appio.view.component.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa el contenido de un tab
 */

public class TabFrame implements Serializable{

    private int id;
    private int layoutId;
    private int iconResource;
    private String title;
    private int backgroundColor;

    List<Component> componentList = new ArrayList<>();
/*
    @Deprecated
    public TabFrame(int id, int layoutId, int iconResource, String title){
        this.id = id;
        this.layoutId = layoutId;
        this.iconResource = iconResource;
        this.title = title;
        this.backgroundColor = R.color.colorAccent;
    }
*/
    private TabFrame(Builder builder) {
        this.id = builder.id;
        this.iconResource = builder.iconResource;
        this.title = builder.title;
        this.backgroundColor = builder.backgroundColor;
        this.componentList = builder.componentList;

    }

    public int getLayout() {
        return layoutId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResource() {
        return iconResource;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }


    public static class Builder {
        int id;
        int iconResource;
        String title;
        List<Component> componentList = new ArrayList<>();
        int backgroundColor;

        public Builder setId(final int id) {
            this.id = id;
            return this;
        }

        public Builder setIconResource (final int iconResource) {
            this.iconResource = iconResource;
            return this;
        }

        public Builder setBackgroundColor (final int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setTitle(final String title) {
            this.title = title;
            return this;
        }

        public Builder addComponent(final Component component) {
            componentList.add(component);
            return this;
        }

        public TabFrame create() {
            return new TabFrame(this);
        }
    }
}
