package ekalaya.id.speedinvid.ui.timelapse;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
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
                                       OnRangeSeekbarChangeListener,
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

    RecyclerView mRecycleView;

    TimelineVideoAdapter mAdapter;

    CrystalRangeSeekbar mSeekbar;

    Spinner spinnerSpeed, spinnerQuality;

    RelativeLayout rlLeft, rlCenter, rlRight, rlPauseOverlay;

    LinearLayout.LayoutParams pl, pc, pr;

    TextView tvstart, tvend, tvprog;

    Button btnProcess;

    private ProgressDialog progressDialog;

    TabLayout tabLayout;

    ViewPager viewPager;

    TimelapseTabAdapter tabAdapter;

    VideoSource videoSource;

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
        initTab();
        //initUI();

//        loadVP();
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

    private void initUI(){
//        mVideoView      = (VideoView) findViewById(R.id.vv_editor);
//
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        mLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
//        mRecycleView    = (RecyclerView) findViewById(R.id.my_recycler_view);
//        mRecycleView.setLayoutManager(mLayoutManager);
//        mAdapter        = new TimelineVideoAdapter(null,getApplicationContext());
//        mRecycleView.setAdapter(mAdapter);
//
//        rlPauseOverlay = (RelativeLayout) findViewById(R.id.pause_overlay);
//        rlPauseOverlay.setOnClickListener(this);
//
//        rlLeft      = (RelativeLayout) findViewById(R.id.rl_left_space);
//        rlCenter    = (RelativeLayout) findViewById(R.id.rl_center_space);
//        rlRight     = (RelativeLayout) findViewById(R.id.rl_right_space);
//        mSeekbar    = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar);
//
//        btnProcess  = (Button) findViewById(R.id.btn_next);
//        btnProcess.setOnClickListener(this);
//
//        spinnerSpeed    = (Spinner) findViewById(R.id.spinner_speed);
//        ArrayAdapter<CharSequence> adapterSpeed = ArrayAdapter.createFromResource(this,
//                R.array.speed_array, android.R.layout.simple_spinner_item);
//        spinnerSpeed.setAdapter(adapterSpeed);
//
//        spinnerQuality  = (Spinner) findViewById(R.id.spinner_quality);
//        ArrayAdapter<CharSequence> adapterQty = ArrayAdapter.createFromResource(this,
//                R.array.quality_array, android.R.layout.simple_spinner_item);
//        spinnerQuality.setAdapter(adapterQty);
//
//        pl = (LinearLayout.LayoutParams) rlLeft.getLayoutParams();
//        pc = (LinearLayout.LayoutParams) rlCenter.getLayoutParams();
//        pr = (LinearLayout.LayoutParams) rlRight.getLayoutParams();
//
//        tvstart = (TextView) findViewById(R.id.tv_vidstart);
//        tvend   = (TextView) findViewById(R.id.tv_vidend);
//        tvprog  = (TextView) findViewById(R.id.tv_vidprogress);
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle(null);
//        progressDialog.setCancelable(false);
//
//        r = new Runnable(){
//            @Override
//            public void run() {
//                int cur_pos = mVideoView.getCurrentPosition();
//                tvprog.setText(Helper.formatTime(cur_pos));
//                if(Math.ceil(cur_pos/500) >= (Math.ceil(videoSource.getFinish()/500) )){
//                    mVideoView.seekTo(videoSource.getStart());
//                }
//                handler.postDelayed(r,200);
//            }
//        };
    }

    private void initIntent(){
        Bundle bundle = getIntent().getExtras();
//        videoPath   = bundle.getString(Const.INTENT_KEY_FILEPATH);
        videoPath   = "/storage/emulated/0/DCIM/Camera/V_20170826_234919_LL.mp4";
        //presenter.initializinVidSrc(videoPath);
    }

    private void processTimeLapse(){
        mVP.setVidsource(videoSource);
        mVP.execute();
    }

    @Override
    public void frameGenerated(List<Bitmap> e) {
        mAdapter.setFiles(e);
        mSeekbar.setMinValue(0);
        mSeekbar.setMaxValue(videoSource.getDuration());
        mSeekbar.setOnRangeSeekbarChangeListener(this);
        mVideoView.setOnTouchListener(this);
    }

    @Override
    public void setTimelineOverlay(int ml, int mr, int cw) {
        pl = pl == null ? (LinearLayout.LayoutParams) rlLeft.getLayoutParams() : pl;
        pc = pc == null ? (LinearLayout.LayoutParams) rlCenter.getLayoutParams() : pc;
        pr = pr == null ? (LinearLayout.LayoutParams) rlRight.getLayoutParams() : pr;

        pl.weight = ml;
        pc.weight = cw;
        pr.weight = mr;

        rlLeft.setLayoutParams(pl);
        rlCenter.setLayoutParams(pc);
        rlRight.setLayoutParams(pr);
    }

    @Override
    public void setTextTime(String start, String end) {
        tvprog.setText(start);
        tvstart.setText(start);
        tvend.setText(end);
    }

    @Override
    public void videoInitialized(VideoSource vs) {
        videoSource = vs;
        mVideoView.setVideoURI(Uri.parse(videoSource.getPathsrc()));
        mVideoView.setOnPreparedListener(this);
        presenter.generateFrames(videoPath);
    }

    @Override
    public void videoModified(VideoSource vs) {
        videoSource = vs;
        setTextTime(vs.getFormattedStart(),vs.getFormattedFinish());
        setTimelineOverlay(vs.getPercentStartCut(), vs.getPercentFinishCut(), vs.getPercentTrimmed());
        mVideoView.seekTo(vs.getStart());
    }

    @Override
    public void valueChanged(Number minValue, Number maxValue) {
        presenter.adjustToSeekbar(minValue, maxValue, videoSource);

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
        handler.postDelayed(r,200);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
