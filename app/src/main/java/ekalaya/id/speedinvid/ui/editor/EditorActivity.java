package ekalaya.id.speedinvid.ui.editor;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import javax.inject.Inject;

import ekalaya.id.speedinvid.R;
import ekalaya.id.speedinvid.application.App;
import ekalaya.id.speedinvid.ui.timelapse.TimelapseActivity;
import ekalaya.id.speedinvid.util.Const;

public class EditorActivity extends AppCompatActivity implements EditorContract.View, View.OnClickListener {

    @Inject
    EditorPresenter presenter;

    EditorComponent actvComponent;

    String videoPath;

    Uri videoUri;

    VideoView mVideoView;

    RelativeLayout rlSlowmo, rlTimelapse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        actvComponent = DaggerEditorComponent.builder()
                .editorModule(new EditorModule(this))
                .appComponent(App.get(this).getComponent()).build();
        actvComponent.inject(this);
        initIntent();
        initUI();
    }

    private void initIntent(){
        Bundle bundle = getIntent().getExtras();
        videoPath   = bundle.getString(Const.INTENT_KEY_FILEPATH);
        videoUri    = Uri.parse(videoPath);
    }

    private void initUI(){
        mVideoView      = (VideoView) findViewById(R.id.vv_editor);
        mVideoView.setVideoURI(videoUri);
        //mVideoView.start();
        rlSlowmo        = (RelativeLayout) findViewById(R.id.rl_slowmo);
        rlTimelapse     = (RelativeLayout) findViewById(R.id.rl_timelapse);
        rlSlowmo.setOnClickListener(this);
        rlTimelapse.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.rl_slowmo){

        } else {
            Intent i = new Intent(EditorActivity.this, TimelapseActivity.class);
            Log.d(Const.APP_TAG, videoPath);
            i.putExtra(Const.INTENT_KEY_FILEPATH, videoPath);
            startActivity(i);
        }
        finish();
    }
}
