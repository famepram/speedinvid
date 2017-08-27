package ekalaya.id.speedinvid.ui.splash;

import android.os.CountDownTimer;

import javax.inject.Inject;

import ekalaya.id.speedinvid.ui.base.BasePresenter;

/**
 * Created by Femmy on 8/26/2017.
 */

public class SplashPresenter extends BasePresenter<SplashContract.View> implements SplashContract.Presenter{

    @Inject
    public SplashPresenter(SplashContract.View view) {
        super(view);
    }

    @Override
    public void countingDown() {
        new CountDownTimer(2000, 1000) {

            public void onTick(long m) {}

            public void onFinish() {
                view.finsihCountDown();
            }

        }.start();
    }
}
