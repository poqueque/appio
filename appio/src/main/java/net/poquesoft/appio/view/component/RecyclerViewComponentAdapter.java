package net.poquesoft.appio.view.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.poquesoft.appio.R;

import java.util.List;

public class RecyclerViewComponentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecyclerViewCompAdapter";
    private List<Component> dataset;
    private int layout;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            view = v;
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick " + getPosition());
        }
    }


    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public HeaderViewHolder(View v) {
            super(v);
            view = v;
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewComponentAdapter(List<Component> myDataset, int layout) {
        dataset = myDataset;
        this.layout = layout;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                                      int viewType) {
        switch (viewType) {
            case Component.HEADER:
                View hv = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_header, parent, false);
                HeaderViewHolder hvh = new HeaderViewHolder(hv);
                return hvh;
            default:
                // create a new view
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(layout, parent, false);
                ViewHolder vh = new ViewHolder(v);
                return vh;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        switch (holder.getItemViewType()) {
            case Component.HEADER:
                RecyclerViewComponentAdapter.HeaderViewHolder viewHolderHeader = (RecyclerViewComponentAdapter.HeaderViewHolder) holder;
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolderHeader.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
                dataset.get(position).initView(viewHolderHeader.view);
                break;
            default:
                RecyclerViewComponentAdapter.ViewHolder viewHolderItem = (RecyclerViewComponentAdapter.ViewHolder) holder;
                dataset.get(position).initView(viewHolderItem.view);
                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d(TAG,"[ALBUM] getItemCount: "+dataset.size());
        return dataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return (dataset.get(position).getType());
    }
}