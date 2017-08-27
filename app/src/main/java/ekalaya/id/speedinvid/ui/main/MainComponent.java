package ekalaya.id.speedinvid.ui.main;

import dagger.Component;
import ekalaya.id.speedinvid.annot.PerActivity;
import ekalaya.id.speedinvid.application.AppComponent;

/**
 * Created by Femmy on 8/26/2017.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules=MainModule.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
