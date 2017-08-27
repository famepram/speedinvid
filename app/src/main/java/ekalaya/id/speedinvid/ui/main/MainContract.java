package ekalaya.id.speedinvid.ui.main;

import android.content.Intent;

/**
 * Created by Femmy on 8/26/2017.
 */

public class MainContract {

    interface View {
        void videoValid(String absPath);

        void videoInvalid();
    }

    interface Presenter {
        void openFileBrowser();

        void receiveVideoFromIntent(Intent data);
    }
}
