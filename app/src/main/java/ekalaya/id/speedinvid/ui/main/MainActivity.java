package ekalaya.id.speedinvid.ui.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import ekalaya.id.speedinvid.R;
import ekalaya.id.speedinvid.application.App;
import ekalaya.id.speedinvid.ui.editor.EditorActivity;
import ekalaya.id.speedinvid.ui.timelapse.TimelapseActivity;
import ekalaya.id.speedinvid.util.Const;

public class MainActivity extends AppCompatActivity implements MainContract.View, View.OnClickListener{

    @Inject
    MainPresenter presenter;

    MainComponent actvComponent;

    Button btnBrowse;

    private static final int REQUEST_TAKE_GALLERY_VIDEO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_main);

        actvComponent = DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .appComponent(App.get(this).getComponent()).build();
        actvComponent.inject(this);

        initUI();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initWindow(){
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorGreyDark));
    }

    private void initUI(){
        btnBrowse = (Button) findViewById(R.id.btn_browse_video);
        btnBrowse.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        try {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            presenter.receiveVideoFromIntent(data);
        }
    }

    @Override
    public void videoValid(String absPath) {
        //Toast.makeText(getApplicationContext(), "Video path : "+absPath,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, TimelapseActivity.class);
        intent.putExtra(Const.INTENT_KEY_FILEPATH, absPath);
        startActivity(intent);
        finish();
    }

    @Override
    public void videoInvalid() {
        Toast.makeText(getApplicationContext(), "Video Invalid",Toast.LENGTH_SHORT).show();
    }
}
