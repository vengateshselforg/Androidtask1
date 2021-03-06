package nfnlabs.test.task1.listing.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import nfnlabs.test.task1.R;
import nfnlabs.test.task1.base.BaseActivity;
import nfnlabs.test.task1.model.FavouriteEvent;

public class ListActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "ListActivity";

    private static final String HOME_TAG_FRAGMENT = "home_tab_fragment";
    private static final String FAVOURITES_TAG_FRAGMENT = "favourites_tab_fragment";

    private BottomNavigationView bottom_nav_tab_view;

    private ImageListFragment homeFragment;
    private ImageListFragment favouritesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register event bus
        EventBus.getDefault().register(this);

        setUpUi();
        loadFavouritesFragment();
        loadHomeFragment();
    }

    private void loadHomeFragment() {
        homeFragment = ImageListFragment.newInstance("home");
        if (getSupportFragmentManager() != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ma_fragment_holder, homeFragment, HOME_TAG_FRAGMENT)
                    .commit();
        }
    }

    private void loadFavouritesFragment() {
        favouritesFragment = ImageListFragment.newInstance("favourites");
        if (getSupportFragmentManager() != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ma_fragment_holder, favouritesFragment, FAVOURITES_TAG_FRAGMENT)
                    .hide(favouritesFragment)
                    .commit();
        }
    }

    /**
     * Initialize all UI components here
     */
    private void setUpUi() {
        bottom_nav_tab_view = findViewById(R.id.ma_bottom_nav_tab);

        bottom_nav_tab_view.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_home:
                getSupportFragmentManager().beginTransaction()
                        .hide(favouritesFragment)
                        .show(homeFragment)
                        .commit();
                return true;

            case R.id.menu_favourites:
                getSupportFragmentManager().beginTransaction()
                        .hide(homeFragment)
                        .show(favouritesFragment).commit();
                return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFavouriteEvent(FavouriteEvent event) {
        if (favouritesFragment != null) {
            favouritesFragment.getImagesForFavouritesTab();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Register event bus
        EventBus.getDefault().unregister(this);
    }
}
