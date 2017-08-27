package ekalaya.id.speedinvid.ui.editor;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Femmy on 8/26/2017.
 */
@Module
public class EditorModule {

    private final EditorContract.View view;

    public EditorModule(EditorContract.View v){
        view = v;
    }

    @Provides
    EditorContract.View provideView(){
        return view;
    }
}
