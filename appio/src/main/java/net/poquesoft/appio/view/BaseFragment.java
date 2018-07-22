package net.poquesoft.appio.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.poquesoft.appio.R;
import net.poquesoft.appio.view.component.Component;
import net.poquesoft.appio.view.listeners.SimpleListener;

public class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    private String title;
    private TabFrame tabFrame;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SimpleListener swipeListener;
    private ViewGroup mContainer;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get title
        title = getArguments().getString("title");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_swipe_base, null);
        // bind view
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutId);
        setSwipe();

        mContainer = view.findViewById(R.id.contentPanel);

        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        paintFrame();
        return view;
    }

    private void setSwipe() {
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        if (swipeListener != null)
                            swipeListener.onAction();
                    }
                }
        );
    }

    protected synchronized void setFrame(TabFrame tabFrame){
        this.tabFrame = tabFrame;
    }

    protected synchronized void paintFrame() {

        mContainer.removeAllViews();

        for (Component c: tabFrame.componentList){
            View child = c.getView(mContext, mContainer);
            mContainer.addView(child);
        }

    }

    protected synchronized void addComponent(Component component) {
        View child = component.getView(mContext, mContainer);
        mContainer.addView(child);
    }

    protected synchronized void clearFrame() {
        mContainer.removeAllViews();
    }

    protected void setSwipeListener(SimpleListener swipeListener) {
        this.swipeListener = swipeListener;
    }

    protected synchronized void setEnabledSwipe(boolean enabledSwipe) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setEnabled(enabledSwipe);
    }

    protected synchronized void showSwipe() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);
    }

    protected synchronized void hideSwipe() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }



    public TabFrame getTabFrame() {
        return tabFrame;
    }
}