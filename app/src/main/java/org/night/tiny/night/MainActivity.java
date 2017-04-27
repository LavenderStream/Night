package org.night.tiny.night;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.apkfuns.logutils.LogUtils;

import org.night.tiny.night.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NightModeChangeListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Night.getInstance().addListener(this);
        Night.setWindowStatusBarColor(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = Night.getInstance().isNight();
                Night.getInstance().setNight(!flag);
            }
        });
    }

    @Override
    public void onNightChange() {
        LogUtils.d("MainActivity -> onDestroy: ");
        Night.getInstance().change(mBinding.clLayout);
        Night.setWindowStatusBarColor(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Night.getInstance().removeListener(this);
    }
}
