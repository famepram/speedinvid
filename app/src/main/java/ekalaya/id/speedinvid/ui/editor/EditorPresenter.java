package ekalaya.id.speedinvid.ui.editor;

import javax.inject.Inject;

import ekalaya.id.speedinvid.ui.base.BasePresenter;

/**
 * Created by Femmy on 8/26/2017.
 */

public class EditorPresenter extends BasePresenter<EditorContract.View> implements EditorContract.Presenter{

    @Inject
    public EditorPresenter(EditorContract.View view) {
        super(view);
    }
}
