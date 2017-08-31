package ekalaya.id.speedinvid.ui.timelapse.fragments;

import ekalaya.id.speedinvid.data.models.VideoSource;

/**
 * Created by WebDev on 31/08/2017.
 */

public class FragmentTimelapseSpeedContract {

    interface View {
        void seekbarValueChanged(VideoSource vs);
    }

    interface Presenter {
        void changeSpeedVideo(VideoSource vs,int i);
    }
}
