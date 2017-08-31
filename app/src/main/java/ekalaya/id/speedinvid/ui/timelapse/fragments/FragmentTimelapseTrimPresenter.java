package ekalaya.id.speedinvid.ui.timelapse.fragments;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ekalaya.id.speedinvid.data.models.VideoSource;
import ekalaya.id.speedinvid.ui.base.BasePresenter;
import ekalaya.id.speedinvid.util.Const;

/**
 * Created by WebDev on 30/08/2017.
 */

public class FragmentTimelapseTrimPresenter extends BasePresenter<FragmentTimelapseTrimContract.View>
        implements FragmentTimelapseTrimContract.Presenter {

    FragmentTimelapseTrimContract.View view;

    public FragmentTimelapseTrimPresenter(FragmentTimelapseTrimContract.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public void seekbarvaluechanged(Number min, Number max, VideoSource vs) {
        int start = Integer.parseInt(String.valueOf(min));
        int end   = Integer.parseInt(String.valueOf(max));
        vs.setStart(start);
        vs.setFinish(end);
        view.parentVideoSrcModified(vs);
    }
}
