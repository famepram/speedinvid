package ekalaya.id.speedinvid.ui.timelapse.fragments;

import ekalaya.id.speedinvid.data.models.VideoSource;

/**
 * Created by WebDev on 31/08/2017.
 */

public class FragmentTimelapseQualityContract {
    interface View {
        void vidsourceModified(VideoSource vs);
    }

    interface Presenter {
        void changeQualityResult(VideoSource vs, int qlty);

        void changeOrientationMode(VideoSource vs, Boolean orientation);
    }
}
