package com.roly.g3m.view;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;
import com.roly.GiveMeMyMoney2.R;
import com.roly.g3m.view.adapter.MainPagerAdapter;
import com.roly.g3m.view.dialog.AddDialog;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main_menu)
public class MainActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener, ActionBar.TabListener {

    private ActionBar actionBar;

    @ViewById(R.id.pager)
    ViewPager pager;

    @OptionsItem
    void menuAdd() {
        AddDialog dialog = new AddDialog();
        dialog.show(getSupportFragmentManager(),"AddDialog");
    }

    @AfterViews
    public void initializeView(){
        MainPagerAdapter adapter = new MainPagerAdapter(getApplicationContext(), getSupportFragmentManager());

        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab = actionBar.newTab().setText(getString(R.string.activity_main_tab_to_me)).setTabListener(this);
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText(getString(R.string.activity_main_tab_from_me)).setTabListener(this);
        actionBar.addTab(tab);

        actionBar.setLogo(R.drawable.icon);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }

    @Override
    public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageScrollStateChanged(int state) {}
}