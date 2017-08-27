package ekalaya.id.speedinvid.ui.splash;

import dagger.Component;
import ekalaya.id.speedinvid.annot.PerActivity;
import ekalaya.id.speedinvid.application.AppComponent;

/**
 * Created by Femmy on 8/26/2017.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules=SplashModule.class)
public interface SplashComponent {
    void inject(SplashActivity splashActivity);
}
