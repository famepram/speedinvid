package ekalaya.id.speedinvid.ui.timelapse;

import android.graphics.Bitmap;
import java.util.List;

import ekalaya.id.speedinvid.data.models.VideoSource;

/**
 * Created by Femmy on 8/26/2017.
 */

public class TimelapseContract {

    interface View {
        void videoInitialized(VideoSource videoSource, List<Bitmap> e);

        void videoModified(VideoSource vs);

    }

    interface Presenter {

        void initializinVidSrc(String abspath);
    }
}
