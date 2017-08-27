package ekalaya.id.speedinvid.application;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Femmy on 8/26/2017.
 */
@Module
public class AppModule {

    private final Application mApp;

    public AppModule(Application mApp) {
        this.mApp = mApp;
    }

    @Provides
    @Named("AppContext")
    public Context provideContext(){
        return mApp;
    }
}
