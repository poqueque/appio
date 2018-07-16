package net.poquesoft.appio.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

public class TabAppioActivity extends AppioActivity {

    private static final String TAG = "TabAppioActivity";
    private static final String SELECTED_ITEM = "arg_selected_item";
    private static final String TAB_FRAMES = "tabFrames";

    private List<TabFrame> tabFrameList = new ArrayList<>();
    private ViewGroup mContainer;
    private HashMap<Integer,ViewGroup> tabContainers = new HashMap<>();
    private ViewGroup visibleContainer;

    private BottomNavigationViewEx navigation;
    private ViewPager viewPager;

    private VpAdapter adapter;
    private List<BaseFragment> fragments;// used for ViewPager adapter

    private int mSelectedItem;
/*
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectTab(item);
            return true;
        }

    };
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContainer = findViewById(R.id.contentPanel);
        navigation = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.vp);
//        Log.d(TAG,"Items: "+navigation.getMenu().size());

        initView();
        initData();
        initEvent();
        //Add MenuItem with icon to Menu
/*
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
*/
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * change BottomNavigationViewEx style
     */
    private void initView() {
//        bind.bnve.enableItemShiftingMode(true);
//        bind.bnve.enableAnimation(false);
    }


    /**
     * create fragments
     */
    private synchronized void initData() {
        fragments = new ArrayList<>();

        navigation.getMenu().clear();
        for (TabFrame tabFrame: tabFrameList){
            addFragment(tabFrame);
        }

        // set adapter
        adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        // binding with ViewPager
        navigation.setupWithViewPager(viewPager);
    }


    protected void addTab(TabFrame tabFrame){
        tabFrameList.add(tabFrame);
    }

    protected void clearTab(int tabId){
        for (BaseFragment baseFragment: fragments){
            if (baseFragment.getTabFrame().getId() == tabId){
                baseFragment.clearFrame();
            }
        }
    }

    protected void addToTab(int tabId, Component component){
        for (BaseFragment baseFragment: fragments){
            if (baseFragment.getTabFrame().getId() == tabId){
                baseFragment.addComponent(component);
            }
        }
    }

    private synchronized BaseFragment addFragment(TabFrame tabFrame){
        // create music fragment and add it
        BaseFragment fragment = new BaseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", tabFrame.getTitle());
        fragment.setArguments(bundle);
        fragments.add(fragment);
        fragment.setFrame(tabFrame);

        navigation.getMenu().add(Menu.NONE, tabFrame.getId(), Menu.NONE, tabFrame.getTitle()).setIcon(tabFrame.getIconResource());
//        if (navigation.getMenu().size() == 1) selectTab(menuItem);
        return fragment;
    }


    /**
     * set listeners
     */
    private void initEvent() {
        // set listener to do something then item selected
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, item.getItemId() + " item was selected-------------------");
                updateToolbarText(item.getTitle());
                return true;
            }
        });


    }

    private void paintFragments(){
        for (Fragment fragment:fragments){
            if (fragment instanceof BaseFragment){
                BaseFragment baseFragment = (BaseFragment) fragment;
                baseFragment.paintFrame();
            }
        }
    }
    /*
    private void selectTab (MenuItem item) {
//            selectFragment(item);
        for (TabFrame tabFrame: tabFrameList) {
            if (item.getItemId() == tabFrame.getId()){
                selectTabFrame(tabFrame);
                updateToolbarText(item.getTitle());
            }
        }
    }
*/
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
        super.onBackPressed();
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



    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> data;

        public VpAdapter(FragmentManager fm, List<BaseFragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }
}
