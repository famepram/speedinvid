package ekalaya.id.speedinvid.ui.base;

/**
 * Created by Femmy on 8/26/2017.
 */

public class BasePresenter<T> {

    protected T view;

    public BasePresenter(T view){
        this.view = view;
    }
}
