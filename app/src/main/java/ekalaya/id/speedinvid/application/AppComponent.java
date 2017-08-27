package ekalaya.id.speedinvid.application;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Femmy on 8/26/2017.
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(App app);

    @Named("AppContext")
    Context getContext();

}
