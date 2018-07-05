package net.poquesoft.appio.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import net.poquesoft.appio.R;
import net.poquesoft.appio.view.component.Component;

public class SimpleAppioActivity extends AppioActivity {

    private static final String TAG = "SimpleAppioActivity";
    private static final String SELECTED_ITEM = "arg_selected_item";

    private TabFrame tabFrame;
    private ViewGroup mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContainer = findViewById(R.id.contentPanel);
    }

    protected synchronized void setFrame(TabFrame tabFrame){
        this.tabFrame = tabFrame;
        paintFrame();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private synchronized void paintFrame() {

        ViewGroup frameContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.frame_container, null);
        //Build components
        for (Component c: tabFrame.componentList){
            View child = c.getView(this, frameContainer);
            frameContainer.addView(child);
        }
        mContainer.addView(frameContainer);
        updateToolbarText(tabFrame.getTitle());
    }

    private void updateToolbarText(final CharSequence text) {
        if (toolbar != null) {
            toolbar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toolbar.setTitle(text);
                }
            }, 10);
        }
    }
}
