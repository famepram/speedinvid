package ekalaya.id.speedinvid.ui.finish;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Femmy on 8/27/2017.
 */

@Module
public class FinishModule {
    private final FinishContract.View view;

    public FinishModule(FinishContract.View v){
        view = v;
    }

    @Provides
    FinishContract.View provideView(){
        return view;
    }
}
