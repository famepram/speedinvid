package ekalaya.id.speedinvid.ui.timelapse;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ekalaya.id.speedinvid.ui.timelapse.fragments.FragmentTimelapseQuality;
import ekalaya.id.speedinvid.ui.timelapse.fragments.FragmentTimelapseSpeed;
import ekalaya.id.speedinvid.ui.timelapse.fragments.FragmentTimelapseTrim;

/**
 * Created by Femmy on 8/29/2017.
 */

public class TimelapseTabAdapter  extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public TimelapseTabAdapter(FragmentManager fm, int tabnumb) {
        super(fm);
        mNumOfTabs = tabnumb;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentTimelapseTrim tabTrim = new FragmentTimelapseTrim();
                return tabTrim;
            case 1:
                FragmentTimelapseSpeed tabSpeed = new FragmentTimelapseSpeed();
                return tabSpeed;
            case 2:
                FragmentTimelapseQuality tabQty = new FragmentTimelapseQuality();
                return tabQty;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
