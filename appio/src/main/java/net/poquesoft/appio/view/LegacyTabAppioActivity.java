package net.poquesoft.appio.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import net.poquesoft.appio.R;
import net.poquesoft.appio.view.component.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LegacyTabAppioActivity extends AppioActivity {

    private static final String TAG = "TabAppioActivity";
    private static final String SELECTED_ITEM = "arg_selected_item";

    private List<TabFrame> tabFrameList = new ArrayList<>();
    private ViewGroup mContainer;
    private HashMap<Integer,ViewGroup> tabContainers = new HashMap<>();
    private ViewGroup visibleContainer;

    private BottomNavigationViewEx navigation;

    private int mSelectedItem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectTab(item);
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContainer = findViewById(R.id.contentPanel);
        navigation = findViewById(R.id.navigation);
        Log.d(TAG,"Items: "+navigation.getMenu().size());
        //Add MenuItem with icon to Menu
        navigation.getMenu().clear();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = navigation.getMenu().findItem(mSelectedItem);
            selectTab(selectedItem);
        } else if (navigation.getMenu().size()>0){
            selectedItem = navigation.getMenu().getItem(0);
            selectTab(selectedItem);
        }
    }

    protected synchronized void addTab(TabFrame tabFrame){
        tabFrameList.add(tabFrame);
        Log.d(TAG,"Items: "+navigation.getMenu().size());
        Log.d(TAG,"Max Items: "+navigation.getMaxItemCount());
        MenuItem menuItem = navigation.getMenu().add(Menu.NONE, tabFrame.getId(), Menu.NONE, tabFrame.getTitle()).setIcon(tabFrame.getIconResource());
        if (navigation.getMenu().size() == 1) selectTab(menuItem);
    }

    private void selectTab (MenuItem item) {
//            selectFragment(item);
        for (TabFrame tabFrame: tabFrameList) {
            if (item.getItemId() == tabFrame.getId()){
                selectTabFrame(tabFrame);
                updateToolbarText(item.getTitle());
            }
        }
    }

    private void updateToolbarText(final CharSequence text) {
        if (toolbar != null) {
            toolbar.setTitle(text);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = navigation.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectTab(homeItem);
        } else {
            super.onBackPressed();
        }
    }

/*
    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        // init corresponding fragment
        for (TabFrame tabFrame: tabFrameList) {
            if (item.getItemId() == tabFrame.id){
                frag = MenuFragment.newInstance(tabFrame.getLayout(), tabFrame.text, tabFrame.getBackgroundColor());
            }
        }

        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i< navigation.getMenu().size(); i++) {
            MenuItem menuItem = navigation.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }

        updateToolbarText(item.getTitle());

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }
*/

    private synchronized void selectTabFrame(TabFrame tabFrame) {
        //Check if the TabFrame is painted
        ViewGroup frameContainer = tabContainers.get(tabFrame.getId());
        if (frameContainer == null){
            //Create if it's not
            frameContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.frame_container, null);
            //Build components
            for (Component c: tabFrame.componentList){
                View child = c.getView(this, frameContainer);
                frameContainer.addView(child);
            }
            mContainer.addView(frameContainer);
            tabContainers.put(tabFrame.getId(),frameContainer);
        }

        //updateVisibility
        if (visibleContainer != null) visibleContainer.setVisibility(View.GONE);
        visibleContainer = frameContainer;
        visibleContainer.setVisibility(View.VISIBLE);
    }
}
