package ekalaya.id.speedinvid.ui.timelapse;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Femmy on 8/26/2017.
 */

@Module
public class TimelapseModule {
    private final TimelapseContract.View view;

    public TimelapseModule(TimelapseContract.View v){
        view = v;
    }

    @Provides
    TimelapseContract.View provideView(){
        return view;
    }
}
