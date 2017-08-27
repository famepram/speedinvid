package ekalaya.id.speedinvid.ui.editor;

import dagger.Component;
import ekalaya.id.speedinvid.annot.PerActivity;
import ekalaya.id.speedinvid.application.AppComponent;

/**
 * Created by Femmy on 8/26/2017.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules=EditorModule.class)
public interface EditorComponent {
    void inject(EditorActivity editorActivity);
}
