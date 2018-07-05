package net.poquesoft.appio.view.component;

public abstract class RecyclerViewListItem {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_COMPONENT = 1;

    abstract public int getType();
} 