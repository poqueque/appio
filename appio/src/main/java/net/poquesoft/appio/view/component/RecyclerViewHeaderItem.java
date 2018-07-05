package net.poquesoft.appio.view.component;

public class RecyclerViewHeaderItem extends RecyclerViewListItem {

    private String headerText;

    // here getters and setters 
    // for title and so on, built
    // using date

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

}