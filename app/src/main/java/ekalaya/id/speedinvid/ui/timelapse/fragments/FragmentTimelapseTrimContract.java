package ekalaya.id.speedinvid.ui.timelapse.fragments;

import android.graphics.Bitmap;

import java.util.List;

import ekalaya.id.speedinvid.data.models.VideoSource;

/**
 * Created by WebDev on 30/08/2017.
 */

public class FragmentTimelapseTrimContract {
    interface View {
        void timelineDrawn(List<Bitmap> e);

        void overlaySeekbarchange(int ml, int mr, int cw);

        void parentVideoSrcModified(VideoSource e);

        void setTextTime(String start, String end);

    }

    interface Presenter {
        void drawTimeline(String abspath);

        void seekbarvaluechanged(Number min, Number max, VideoSource vs);
    }
}
