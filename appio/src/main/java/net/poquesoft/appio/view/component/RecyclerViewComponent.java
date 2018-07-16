package net.poquesoft.appio.view.component;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import net.poquesoft.appio.R;

import java.util.ArrayList;

/**
 * Standard Button component
 */

public class RecyclerViewComponent extends Component {

    public static final String TAG ="RecyclerViewComponent";
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private int layout;

    ArrayList<Component> items = new ArrayList<>();

    public RecyclerViewComponent(int layout) {
        super();
        this.layout = layout;

    }

    @Override
    public int getLayout() {
        return R.layout.component_recyclerview;
    }

    @Override
    public void initView(final View v) {

        Log.d(TAG,"[ALBUM] initView");
        recyclerView = v.findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new RecyclerViewComponentAdapter(items, layout);
        recyclerView.setAdapter(adapter);
    }

    public void addItem(Component item){
        Log.d(TAG,"[ALBUM] addItem");
        items.add(item);
        adapter.notifyDataSetChanged();
    };

    @Override
    public void update(){
        Log.d(TAG,"[ALBUM] updateItem");
        adapter.notifyDataSetChanged();
    };

    public void removeItems(){
        items.clear();
    };
}
