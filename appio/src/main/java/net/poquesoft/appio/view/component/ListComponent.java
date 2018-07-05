package net.poquesoft.appio.view.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.poquesoft.appio.R;

import java.util.ArrayList;

/**
 * Standard Button component
 */

public class ListComponent<T extends Component> extends Component {

    ViewGroup container;
    ArrayList<T> items = new ArrayList<>();

    public ListComponent(Class<T> itemClass) {
        super();
    }

    @Override
    public int getLayout() {
        return R.layout.component_list;
    }

    @Override
    public void initView(final View v) {
        container = v.findViewById(R.id.container);
        boolean isFirst = true;
        for (T item: items){
            View child = item.getView(container.getContext(), container);
            container.addView(child);
            if (!isFirst) {
                LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View separator = inflater.inflate(R.layout.item_separator, container, false);
                container.addView(separator);
            }

            isFirst = false;
        }
    }

    public void addItem(T item){
        items.add(item);
    };

    public void removeItems(){
        items.clear();
    };
}
