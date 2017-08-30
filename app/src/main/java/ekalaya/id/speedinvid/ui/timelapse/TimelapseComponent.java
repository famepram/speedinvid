package ekalaya.id.speedinvid.ui.timelapse;

import android.content.Context;

import javax.inject.Named;

import dagger.Component;
import ekalaya.id.speedinvid.annot.PerActivity;
import ekalaya.id.speedinvid.application.AppComponent;
import ekalaya.id.speedinvid.ui.timelapse.fragments.FragmentTimelapseTrim;

/**
 * Created by Femmy on 8/26/2017.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules=TimelapseModule.class)
public interface TimelapseComponent {
    void inject(TimelapseActivity timelapseActivity);

    void inject(FragmentTimelapseTrim fragment);

    @Named("AppContext")
    Context getContext();
}

