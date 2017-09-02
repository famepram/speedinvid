package ekalaya.id.speedinvid.ui.timelapse.fragments;

import android.util.Log;

import ekalaya.id.speedinvid.data.models.VideoSource;
import ekalaya.id.speedinvid.ui.base.BasePresenter;
import ekalaya.id.speedinvid.util.Const;

/**
 * Created by WebDev on 31/08/2017.
 */

public class FragmentTimelapseSpeedPresenter extends BasePresenter<FragmentTimelapseSpeedContract.View>
        implements FragmentTimelapseSpeedContract.Presenter {

    FragmentTimelapseSpeedContract.View view;

    public FragmentTimelapseSpeedPresenter(FragmentTimelapseSpeedContract.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public void changeSpeedVideo(VideoSource vs, int i) {
        double speed = 1;
        if(i < 50){
            speed = i < 5 ? 0.5 : ((double)(i+50) /  100);
            speed = Math.ceil(speed * 10) / 10 ;
        } else {
            speed = Math.ceil(((double)i - 50) / 10) ;
            speed = speed < 1 ? 1 : speed;
        }
        vs.setSpeed(speed);
        view.seekbarValueChanged(vs);
    }
}
