package ekalaya.id.speedinvid.ui.finish;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import javax.inject.Inject;

import ekalaya.id.speedinvid.R;
import ekalaya.id.speedinvid.application.App;
import ekalaya.id.speedinvid.ui.main.MainActivity;
import ekalaya.id.speedinvid.util.Const;


public class FinishActivity extends AppCompatActivity implements FinishContract.View {

    @Inject
    FinishPresenter presenter;

    FinishComponent actvComponent;

    String filepath;

    VideoView vv;

    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        actvComponent = DaggerFinishComponent.builder()
                .finishModule(new FinishModule(this))
                .appComponent(App.get(this).getComponent()).build();
        actvComponent.inject(this);
        initIntent();

        vv = (VideoView) findViewById(R.id.vv_finish);
        vv.setVideoURI(Uri.parse(filepath));
        vv.start();

        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(FinishActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void initIntent(){
        Bundle bundle = getIntent().getExtras();
        filepath   = bundle.getString(Const.INTENT_KEY_FILE_RESULT_PATH);
    }
}
