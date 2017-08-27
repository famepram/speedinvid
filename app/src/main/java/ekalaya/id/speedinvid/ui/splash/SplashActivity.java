package ekalaya.id.speedinvid.ui.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.inject.Inject;

import ekalaya.id.speedinvid.R;
import ekalaya.id.speedinvid.application.App;
import ekalaya.id.speedinvid.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity implements SplashContract.View  {

    @Inject
    SplashPresenter presenter;

    SplashComponent actvComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        actvComponent = DaggerSplashComponent.builder()
                        .splashModule(new SplashModule(this))
                        .appComponent(App.get(this).getComponent()).build();
        actvComponent.inject(this);
        presenter.countingDown();
    }

    @Override
    public void finsihCountDown() {
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
