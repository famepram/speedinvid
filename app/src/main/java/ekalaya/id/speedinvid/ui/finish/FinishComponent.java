package ekalaya.id.speedinvid.ui.finish;

import dagger.Component;
import ekalaya.id.speedinvid.annot.PerActivity;
import ekalaya.id.speedinvid.application.AppComponent;

/**
 * Created by Femmy on 8/27/2017.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules=FinishModule.class)
public interface FinishComponent {
    void inject(FinishActivity finishActivity);
}

