package ekalaya.id.speedinvid.ui.timelapse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import ekalaya.id.speedinvid.ui.timelapse.fragments.FragmentTimelapseQuality;
import ekalaya.id.speedinvid.ui.timelapse.fragments.FragmentTimelapseSpeed;
import ekalaya.id.speedinvid.ui.timelapse.fragments.FragmentTimelapseTrim;
import ekalaya.id.speedinvid.util.Const;

/**
 * Created by Femmy on 8/29/2017.
 */

public class TimelapseTabAdapter  extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    String videoUri;

    public TimelapseTabAdapter(FragmentManager fm, int tabnumb, String videoUri) {
        super(fm);
        mNumOfTabs = tabnumb;
        this.videoUri = videoUri;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentTimelapseTrim tabTrim = new FragmentTimelapseTrim();
                Bundle args = new Bundle();
                args.putString("videouri", videoUri);
                tabTrim.setArguments(args);
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
