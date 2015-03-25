package co.johnnyli.gamer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;


public class Feed extends ActionBarActivity {
    private static long back_pressed;
    private ViewPager viewPager;
    private String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        color = MainActivity.color;
        //Changes ActionBar color
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        //Code for Tabs
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FeedFragmentPageAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabsStrip.setIndicatorColor(Color.parseColor(color));
        tabsStrip.setShouldExpand(true);
        tabsStrip.setIndicatorHeight(15);
        tabsStrip.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent search = new Intent(this, Search.class);
                startActivity(search);
                return true;
            case R.id.action_refresh:
                viewPager.setAdapter(new FeedFragmentPageAdapter(getSupportFragmentManager()));
                PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
                tabsStrip.setIndicatorColor(Color.parseColor(color));
                tabsStrip.setShouldExpand(true);
                tabsStrip.setIndicatorHeight(15);
                tabsStrip.setViewPager(viewPager);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Press back again to log out", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}
