package org.wmii.endorfit.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.wmii.endorfit.Tabs.tabABSBack;
import org.wmii.endorfit.Tabs.tabArms;
import org.wmii.endorfit.Tabs.tabChest;
import org.wmii.endorfit.Tabs.tabLegs;
import org.wmii.endorfit.Tabs.tabMoving;
import org.wmii.endorfit.Tabs.tabShoulders;

public class PageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public PageAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new tabChest();
            case 1:
                return new tabArms();
            case 2:
                return new tabABSBack();
            case 3:
                return new tabShoulders();
            case 4:
                return new tabLegs();
            case 5:
                return new tabMoving();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
