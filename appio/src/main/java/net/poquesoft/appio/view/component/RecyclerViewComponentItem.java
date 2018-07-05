package net.poquesoft.appio.view.component;

public class RecyclerViewComponentItem extends RecyclerViewListItem {

    private Component component;

    // here getters and setters 
    // for title and so on, built 
    // using event

    @Override
    public int getType() {
        return TYPE_COMPONENT;
    }

}