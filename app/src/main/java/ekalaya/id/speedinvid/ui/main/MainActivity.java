package ekalaya.id.speedinvid.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import ekalaya.id.speedinvid.R;
import ekalaya.id.speedinvid.application.App;
import ekalaya.id.speedinvid.ui.editor.EditorActivity;
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
        setContentView(R.layout.activity_main);

        actvComponent = DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .appComponent(App.get(this).getComponent()).build();
        actvComponent.inject(this);

        initUI();
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
        Toast.makeText(getApplicationContext(), "Video path : "+absPath,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        intent.putExtra(Const.INTENT_KEY_FILEPATH, absPath);
        startActivity(intent);
    }

    @Override
    public void videoInvalid() {
        Toast.makeText(getApplicationContext(), "Video Invalid",Toast.LENGTH_SHORT).show();
    }
}
