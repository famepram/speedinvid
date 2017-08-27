package ekalaya.id.speedinvid.ui.finish;

import javax.inject.Inject;

import ekalaya.id.speedinvid.ui.base.BasePresenter;

/**
 * Created by Femmy on 8/27/2017.
 */

public class FinishPresenter extends BasePresenter<FinishContract.View> implements FinishContract.Presenter{

    @Inject
    public FinishPresenter(FinishContract.View view) {
        super(view);
    }
}
