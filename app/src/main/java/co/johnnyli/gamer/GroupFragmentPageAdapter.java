package co.johnnyli.gamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class GroupFragmentPageAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {"Discussion", "About"};

    public GroupFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle disBundle = new Bundle();
                disBundle.putString("pk", Group.pkOfGroup);
                DiscussionFragment discuss = new DiscussionFragment();
                discuss.setArguments(disBundle);
                return discuss;

            case 1:
                Bundle bundle = new Bundle();
                bundle.putString("name", Group.nameOfGroup);
                bundle.putString("pk", Group.pkOfGroup);
                AboutFragment about = new AboutFragment();
                about.setArguments(bundle);
                return about;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
