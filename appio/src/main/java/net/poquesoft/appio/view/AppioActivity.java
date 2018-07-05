package net.poquesoft.appio.view;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import net.poquesoft.appio.R;
import net.poquesoft.appio.authentication.Authentication;
import net.poquesoft.appio.presenter.AppioPresenter;
import net.poquesoft.appio.view.dialogs.AppioDialogs;
import net.poquesoft.appio.view.drawer.DrawerConfig;
import net.poquesoft.appio.view.drawer.DrawerItem;

public class AppioActivity extends AppCompatActivity implements BaseView<AppioPresenter>, Drawer.OnDrawerListener {

    private static final String TAG = "AppioActivity";
    AppioPresenter presenter;
    Drawer drawer;

    Toolbar toolbar;
    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appio);
    }

    @Override
    public void setPresenter(AppioPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onReceiveData(String key, String data) {
        Log.d(TAG,"[MVP] AppioData received from Presenter");
    }

    protected synchronized void addDrawer(Bundle savedInstanceState, final DrawerConfig drawerConfig){
        Typeface typeface = ResourcesCompat.getFont(this, R.font.raleway);
        DrawerBuilder drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withSelectedItem(drawerConfig.getDefaultSelection())
                .withDrawerLayout(R.layout.material_drawer_fits_not)
                .withOnDrawerListener(this);

        for (DrawerItem drawerItem: drawerConfig.getDrawerItems())
            drawerBuilder.addDrawerItems(drawerItem.getIDrawerItem());

        drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem iDrawerItem) {
                for (DrawerItem drawerItem: drawerConfig.getDrawerItems()){
                    if (drawerItem.is(iDrawerItem)){
                        drawerItem.click(AppioActivity.this);
                    }
                }
                return false;
            }
        });

        if (drawerConfig.getHeaderLayout() != DrawerConfig.NONE) {
            drawerBuilder.withHeader(drawerConfig.getHeaderLayout());
        }

        if (toolbar != null) drawerBuilder.withToolbar(toolbar);

        drawer = drawerBuilder.build();

        if (drawer.getActionBarDrawerToggle() != null)
            drawer.getActionBarDrawerToggle()
                    .setHomeAsUpIndicator(new IconicsDrawable(this, FontAwesome.Icon.faw_bars).color(Color.WHITE).sizeDp(20).paddingDp(20));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            if (drawer.getActionBarDrawerToggle() != null)
                drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        }
        updateDrawer();
    }


    protected synchronized void updateDrawer(){
        for (IDrawerItem item: drawer.getDrawerItems()){
            if (item.getTag() instanceof DrawerItem) {
                DrawerItem drawerItem = (DrawerItem) item.getTag();
                if (drawerItem.is(DrawerItem.USER_ITEM)){
                    //modify an item of the drawer
                    String userNameToShow = Authentication.getName();
                    Log.d(TAG,"[DRAWER] UserName: "+userNameToShow);
                    if (userNameToShow == null) userNameToShow = "Iniciar Sesión / Registro";
                    if (item instanceof PrimaryDrawerItem)
                        ((PrimaryDrawerItem)item).withName(userNameToShow);
                    if (item instanceof SecondaryDrawerItem)
                        ((SecondaryDrawerItem)item).withName(userNameToShow);
                    //notify the drawer about the updated element. it will take care about everything else
                    drawer.updateItem(item);
                }
            }
        }
        Authentication.getName();
    }


    protected synchronized void setBackArrow() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {
        progressDialog = AppioDialogs.showProgress(this, getString(R.string.loading));
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void hideProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }


    @Override
    public void onDrawerOpened(View drawerView) {
        updateDrawer();
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }
}
