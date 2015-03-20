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

import com.astuetz.PagerSlidingTabStrip;


public class Group extends ActionBarActivity {

    public static String nameOfGroup;
    public static String pkOfGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        nameOfGroup = this.getIntent().getExtras().getString("name");
        pkOfGroup = this.getIntent().getExtras().getString("pk");
        setTitle(nameOfGroup);
        String color = MainActivity.color;
        //ActionBar Color
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        //Code for Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new GroupFragmentPageAdapter(getSupportFragmentManager()));
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
                finish();
                startActivity(getIntent());
        }

        return super.onOptionsItemSelected(item);
    }
}
