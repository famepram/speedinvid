package ekalaya.id.speedinvid.ui.timelapse;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ekalaya.id.speedinvid.R;
import ekalaya.id.speedinvid.application.App;
import ekalaya.id.speedinvid.data.models.VideoSource;
import ekalaya.id.speedinvid.ui.finish.FinishActivity;
import ekalaya.id.speedinvid.ui.main.MainActivity;
import ekalaya.id.speedinvid.ui.timelapse.fragments.FragmentTimelapseQuality;
import ekalaya.id.speedinvid.ui.timelapse.fragments.FragmentTimelapseSpeed;
import ekalaya.id.speedinvid.ui.timelapse.fragments.FragmentTimelapseTrim;
import ekalaya.id.speedinvid.util.Const;
import ekalaya.id.speedinvid.util.Helper;
import ekalaya.id.speedinvid.util.VideoProcessor;

public class TimelapseActivity extends AppCompatActivity
                            implements TimelapseContract.View,
                                       MediaPlayer.OnPreparedListener,
                                       View.OnClickListener,VideoView.OnTouchListener,
                                        FragmentTimelapseTrim.OnFragmentInteractionListener,
                                        FragmentTimelapseSpeed.OnFragmentInteractionListener,
                                        FragmentTimelapseQuality.OnFragmentInteractionListener,
                                        VideoProcessor.VPCallback {

    @Inject
    TimelapsePresenter presenter;

    TimelapseComponent actvComponent;

    String videoPath;

    VideoView mVideoView;

    RelativeLayout rlPauseOverlay, loadingOverlay;

    Button btnProcess;

    private ProgressDialog progressDialog;

    TabLayout tabLayout;

    ViewPager viewPager;

    TimelapseTabAdapter tabAdapter;

    VideoSource videoSource;

    List<Bitmap> frames = new ArrayList<Bitmap>();

    TextView tvProg;

    private Runnable r;
    final Handler handler = new Handler();

    private VideoProcessor mVP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindow();
        setContentView(R.layout.activity_timelapse);

        actvComponent = DaggerTimelapseComponent.builder()
                .timelapseModule(new TimelapseModule(this))
                .appComponent(App.get(this).getComponent()).build();
        actvComponent.inject(this);
        initIntent();
        initUI();

    }

    public TimelapseComponent getActvComponent(){
        return actvComponent;
    }

    private void initTab(){
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("TRIM"));
        tabLayout.addTab(tabLayout.newTab().setText("SPEED"));
        tabLayout.addTab(tabLayout.newTab().setText("QUALITY"));

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabAdapter = new TimelapseTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),videoPath);


        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                return true;
            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

    }

    private void loadVP(){
        mVP = new VideoProcessor();
        mVP.setVideoProcessorCallback(this);
        mVP.loadFFMEPEG(getApplicationContext());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initWindow(){
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorGreyDark));
    }

    public VideoSource getVidSource(){
        return videoSource;
    }

    public List<Bitmap> getVidFrames(){
        return frames;
    }

    private void initUI(){
        mVideoView      = (VideoView) findViewById(R.id.vv_editor);

        btnProcess  = (Button) findViewById(R.id.btn_next);
        btnProcess.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);
        progressDialog.setCancelable(false);

        rlPauseOverlay = (RelativeLayout) findViewById(R.id.pause_overlay);
        rlPauseOverlay.setOnClickListener(this);
        loadingOverlay = (RelativeLayout) findViewById(R.id.loading_overlay);

        new CountDownTimer(1000, 1000) {
            public void onTick(long mills) {}

            @Override
            public void onFinish() {
                presenter.initializinVidSrc(videoPath);
            }
        }.start();


    }

    private void setVideoProgress(String time){
        if(tabAdapter != null){
            FragmentTimelapseTrim fTrim = (FragmentTimelapseTrim) tabAdapter.getItem(0);
            if(fTrim != null){
                tvProg.setText(time);
            }
        }

    }

    private void initIntent(){
        Bundle bundle = getIntent().getExtras();
//        videoPath   = bundle.getString(Const.INTENT_KEY_FILEPATH);
        videoPath   = "/storage/emulated/0/DCIM/Camera/V_20170826_234919_LL.mp4";

    }

    private void processTimeLapse(){
        mVP.setVidsource(videoSource);
        mVP.execute();
    }



    @Override
    public void videoInitialized(VideoSource vs, List<Bitmap> e) {
        videoSource = vs;
        mVideoView.setVideoURI(Uri.parse(videoSource.getPathsrc()));
        frames = e;
        initTab();
        loadVP();
    }

    @Override
    public void videoModified(VideoSource vs) {
        videoSource = vs;
        mVideoView.seekTo(vs.getStart());

    }

    @Override
    public void fragmentBuildingDone(TextView tv) {
        tvProg = tv;
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnTouchListener(this);
        loadingOverlay.setVisibility(View.GONE);

    }

    @Override
    public void FSVidSourceModified(VideoSource vs) {
        videoSource = vs;
    }

    @Override
    public void FQVidSourceModified(VideoSource vs) {
        videoSource = vs;
    }


    @Override
    public void onPause(){
        super.onPause();
        handler.removeCallbacks(r);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(r);
    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.setLooping(true);
        handler.postDelayed(r = new Runnable(){
            @Override
            public void run() {
                int cur_pos = mVideoView.getCurrentPosition();
                setVideoProgress(Helper.formatTime(cur_pos));
                if(Math.ceil(cur_pos/500) >= (Math.ceil(videoSource.getFinish()/500) )){
                    mVideoView.seekTo(videoSource.getStart());
                }
                handler.postDelayed(r,200);
            }
        },200);
        handler.removeCallbacks(r);
    }

    private void resumingVideo(){
        rlPauseOverlay.setVisibility(View.INVISIBLE);
        mVideoView.start();
        handler.postDelayed(r,200);
    }

    private void pausingVideo(){
        rlPauseOverlay.setVisibility(View.VISIBLE);
        mVideoView.pause();
        handler.removeCallbacks(r);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == rlPauseOverlay.getId()){
            resumingVideo();
        } else if(view.getId() == btnProcess.getId()){
            processTimeLapse();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        pausingVideo();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(TimelapseActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
        }
        return true;
    }

/*
Video Processor Callback
----------------------------------------------------------------------------------------------------
 */

    @Override
    public void unknownError(String e) {
        Log.d(Const.APP_TAG, "Video Processor Callback - unknownError ");
    }

    @Override
    public void ffmpegNotSupported() {
        Log.d(Const.APP_TAG, "Video Processor Callback - ffmpegNotSupported ");
    }

    @Override
    public void ffmpegSuccessLoaded() {
        Log.d(Const.APP_TAG, "Video Processor Callback - ffmpegSuccessLoaded ");
    }

    @Override
    public void ffmpegAlreadyRunning() {
        Log.d(Const.APP_TAG, "Video Processor Callback - ffmpegAlreadyRunning : ");
    }

    @Override
    public void onExecfailure(String s) {
        Log.d(Const.APP_TAG, "Video Processor Callback - onExecfailure : "+s);
    }

    @Override
    public void onExecSuccess(String s) {
        Log.d(Const.APP_TAG, "Video Processor Callback - onExecSuccess : "+s);
    }

    @Override
    public void onExecRunning(int percent, String msg) {
        progressDialog.setMessage("progress : " + msg);
        progressDialog.show();
        Log.d(Const.APP_TAG, "Video Processor Callback - Progress percent : "+percent);
    }

    @Override
    public void onExecFinished() {
        progressDialog.dismiss();
        String resultPath = mVP.getVideoFileResultPath();
        File filetoscan = new File(resultPath);
        Intent sintent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        sintent.setData(Uri.fromFile(filetoscan));
        getApplicationContext().sendBroadcast(sintent);
        Log.d(Const.APP_TAG, "Video Processor Callback - onExecFinished ");

        Intent i = new Intent(TimelapseActivity.this, FinishActivity.class);
        i.putExtra(Const.INTENT_KEY_FILE_RESULT_PATH, resultPath);
        startActivity(i);
        finish();
    }

    @Override
    public void onExecStart() {
        Log.d(Const.APP_TAG, "Video Processor Callback - onExecStart ");
    }



}
