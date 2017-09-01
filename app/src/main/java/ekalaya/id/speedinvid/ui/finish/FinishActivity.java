package ekalaya.id.speedinvid.ui.finish;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import javax.inject.Inject;

import ekalaya.id.speedinvid.R;
import ekalaya.id.speedinvid.application.App;
import ekalaya.id.speedinvid.ui.main.MainActivity;
import ekalaya.id.speedinvid.util.Const;
import ekalaya.id.speedinvid.util.Helper;


public class FinishActivity extends AppCompatActivity
                            implements FinishContract.View,
                                        View.OnClickListener,
                                        VideoView.OnTouchListener{

    @Inject
    FinishPresenter presenter;

    FinishComponent actvComponent;

    String filepath;

    VideoView vv;

    Button btnBack, btnOpenGallery;

    RelativeLayout rlPauseOverlay;

    TextView tvLocation, tvDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_finish);

        actvComponent = DaggerFinishComponent.builder()
                .finishModule(new FinishModule(this))
                .appComponent(App.get(this).getComponent()).build();
        actvComponent.inject(this);
        initUI();
        initIntent();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initWindow(){
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorGreyDark));
    }

    private void initUI(){
        vv = (VideoView) findViewById(R.id.vv_finish);

        btnBack = (Button) findViewById(R.id.btn_backhome);
        btnBack.setPaintFlags(btnBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnBack.setOnClickListener(this);

        btnOpenGallery = (Button) findViewById(R.id.btn_opengallery);
        btnOpenGallery.setOnClickListener(this);

        rlPauseOverlay = (RelativeLayout) findViewById(R.id.pause_overlay);
        rlPauseOverlay.setOnClickListener(this);

        tvLocation = (TextView) findViewById(R.id.val_loc);
        tvDuration = (TextView) findViewById(R.id.val_duration);
    }


    private void initIntent(){
        Bundle bundle = getIntent().getExtras();
        filepath   = bundle.getString(Const.INTENT_KEY_FILE_RESULT_PATH);
        presenter.initResult(filepath);
        vv.setOnTouchListener(this);
        vv.setVideoURI(Uri.parse(filepath));
    }

    private void resumingVideo(){
        rlPauseOverlay.setVisibility(View.INVISIBLE);
        vv.start();
    }

    private void pausingVideo(){
        rlPauseOverlay.setVisibility(View.VISIBLE);
        vv.pause();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == vv.getId()){
            pausingVideo();
        } else if(id == btnBack.getId()){
            Intent i = new Intent(FinishActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else if(id == btnOpenGallery.getId()){
            Intent intent = new Intent(Intent.ACTION_VIEW, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivity(intent);
        } else if(id == rlPauseOverlay.getId()){
            resumingVideo();
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        pausingVideo();
        return false;
    }

    @Override
    public void resultInitialized(String Duration) {
        tvLocation.setText(Helper.ellipsis(filepath,30));
        tvDuration.setText(Duration);
    }
}
