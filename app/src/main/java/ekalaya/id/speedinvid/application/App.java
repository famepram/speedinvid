package ekalaya.id.speedinvid.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Femmy on 8/26/2017.
 */

public class App extends Application {

    protected AppComponent appComponent;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getComponent().inject(this);
        //injectDependencies();
    }

    public AppComponent getComponent(){
        if(appComponent == null){
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }

}
