package ekalaya.id.speedinvid.application;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Femmy on 8/26/2017.
 */
@Module
public class AppModule {

    private final App mApp;

    public AppModule(App mApp) {
        this.mApp = mApp;
    }

    @Provides @Singleton
    public App provideApp() {
        return mApp;
    }

    @Provides
    @Named("AppContext")
    public Context provideContext(){
        return mApp;
    }
}
