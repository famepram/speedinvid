package ekalaya.id.speedinvid.ui.finish;

/**
 * Created by Femmy on 8/27/2017.
 */

public class FinishContract {

    interface View {
        void resultInitialized(String Duration);
    }
    interface Presenter{
        void initResult(String uri);
    }
}
