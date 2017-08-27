package ekalaya.id.speedinvid.ui.splash;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Femmy on 8/26/2017.
 */
@Module
public class SplashModule {

    private final SplashContract.View view;


    public SplashModule(SplashContract.View v) {
        view = v;
    }

    @Provides
    SplashContract.View provideView(){
        return view;
    }
}
