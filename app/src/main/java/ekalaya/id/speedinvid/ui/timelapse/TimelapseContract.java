package ekalaya.id.speedinvid.ui.timelapse;

import android.graphics.Bitmap;
import java.util.List;

import ekalaya.id.speedinvid.data.models.VideoSource;

/**
 * Created by Femmy on 8/26/2017.
 */

public class TimelapseContract {

    interface View {
        void frameGenerated(List<Bitmap> e);

        void setTimelineOverlay(int ml, int mr, int cw);

        void videoInitialized(VideoSource videoSource);

        void setTextTime(String start, String end);

        void videoModified(VideoSource vs);
    }

    interface Presenter {
        void generateFrames(String abspath);

        void adjustToSeekbar(Number min, Number max, VideoSource vs);

        void initializinVidSrc(String abspath);
    }
}
