package org.night.tiny.night;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.night.tiny.night.databinding.ActivityMainBinding;
import org.night.tiny.night.night.Night;
import org.night.tiny.night.night.NightChange;
import org.night.tiny.night.night.NightError;

import java.io.File;

import static org.night.tiny.night.night.Night.DEFAULT_SKIN;

public class MainActivity extends AppCompatActivity implements NightChange, NightError {

    private static final String S_SKIN_PATH = Environment.getExternalStorageDirectory() + File
            .separator;

    private ActivityMainBinding mBinding;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 传文件名， 本应用的默认主题
        Night.getInstance().initNight(this, true, S_SKIN_PATH, DEFAULT_SKIN, R.color.class);
        super.onCreate(savedInstanceState);
        Night.getInstance().addListener(this);
        Night.getInstance().addError(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        mBinding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Night.getInstance().setNight(true, "pink");
            }
        });

    }

    @Override
    public void onNightChange() {
        Night.getInstance().change(mBinding.clLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Night.getInstance().removeListener(this);
        Night.getInstance().removeError(this);
    }

    @Override
    public void error(String skinName) {
        Toast.makeText(this, skinName + "error", Toast.LENGTH_SHORT).show();
    }
}
