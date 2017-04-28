package org.night.tiny.night;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.night.tiny.night.databinding.ActivityMainBinding;
import org.night.tiny.night.night.Night;
import org.night.tiny.night.night.NightModeChangeListener;
import org.night.tiny.night.night.ViewBind;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NightModeChangeListener {

    private static final String S_SKIN_PATH = Environment.getExternalStorageDirectory() + File
            .separator;

    private ActivityMainBinding mBinding;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Night.getInstance().addListener(this);
        // 传文件名， 本应用的默认主题
        Night.getInstance().initNight(this, S_SKIN_PATH, "default", R.color.class);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ViewBind.setBackGround(mBinding.clLayout, R.color.bg);

        mBinding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Night.getInstance().setNight("pink");
            }
        });

/*
        Resources resources = ResourcesManager.getInstance().loadPlugin(this);
        int reid = ResourcesManager.getInstance().getResouceFromValueName(this, "color", "bg");
        mBinding.clLayout.setBackgroundColor(reid);*/

    }

    @Override
    public void onNightChange() {
        Night.getInstance().change(mBinding.clLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Night.getInstance().removeListener(this);
    }
}
