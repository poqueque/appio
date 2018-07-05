package net.poquesoft.appio.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.poquesoft.appio.R;
import net.poquesoft.appio.view.component.Component;

public class BaseFragment extends Fragment {
    private String title;
    private TabFrame tabFrame;
    private ViewGroup mContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get title
        title = getArguments().getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_base, null);
        // bind view

        mContainer = view.findViewById(R.id.contentPanel);

        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        paintFrame();
        return view;
    }

    protected synchronized void setFrame(TabFrame tabFrame){
        this.tabFrame = tabFrame;
    }

    protected synchronized void paintFrame() {

        mContainer.removeAllViews();
        ViewGroup frameContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.frame_container, null);
        //Build components
        for (Component c: tabFrame.componentList){
            View child = c.getView(this.getContext(), frameContainer);
            frameContainer.addView(child);
        }
        mContainer.addView(frameContainer);
    }
}