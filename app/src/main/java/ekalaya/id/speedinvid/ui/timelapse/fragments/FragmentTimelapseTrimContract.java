package ekalaya.id.speedinvid.ui.timelapse.fragments;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by WebDev on 30/08/2017.
 */

public class FragmentTimelapseTrimContract {
    interface View {
        void timelineDrawn(List<Bitmap> e);

    }

    interface Presenter {
        void drawTimeline(String abspath);
    }
}
