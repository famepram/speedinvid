package ekalaya.id.speedinvid.ui.main;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Femmy on 8/26/2017.
 */
@Module
public class MainModule {

    private final MainContract.View view;

    public MainModule(MainContract.View v) {
        view = v;
    }

    @Provides
    MainContract.View provideView(){
        return view;
    }
}
