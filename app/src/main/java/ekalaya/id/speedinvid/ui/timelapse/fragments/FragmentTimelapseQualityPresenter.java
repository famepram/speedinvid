package ekalaya.id.speedinvid.ui.timelapse.fragments;

import ekalaya.id.speedinvid.data.models.VideoSource;
import ekalaya.id.speedinvid.ui.base.BasePresenter;

/**
 * Created by WebDev on 31/08/2017.
 */

public class FragmentTimelapseQualityPresenter extends BasePresenter<FragmentTimelapseQualityContract.View>
                                            implements  FragmentTimelapseQualityContract.Presenter{
    FragmentTimelapseQualityContract.View view;

    public FragmentTimelapseQualityPresenter(FragmentTimelapseQualityContract.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public void changeQualityResult(VideoSource vs, int qlty) {
        vs.setQuality(qlty);
        view.vidsourceModified(vs);
    }

    @Override
    public void changeOrientationMode(VideoSource vs, Boolean orientation) {
        vs.setKeepPotrait(orientation);
        view.vidsourceModified(vs);
    }

    @Override
    public void changeRemovingAudio(VideoSource vs, Boolean remAudio) {
        vs.setRemoveAudio(remAudio);
        view.vidsourceModified(vs);
    }
}
