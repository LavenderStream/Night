package org.night.tiny.night;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by tiny on 2017/4/27.
 */

public class SkinManager {
    private static final String TAG = "SkinManager";
    private static final String S_SKIN_PATH = Environment.getExternalStorageDirectory() + File
            .separator;
    // 存放皮肤apk的包名
    private String mSkinPackageName = "";


    private static final SkinManager ourInstance = new SkinManager();

    public static SkinManager getInstance() {
        return ourInstance;
    }

    private SkinManager() {
    }

    private Resources loadPlugin(Context context) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, S_SKIN_PATH + mSkinPackageName + ".apk");
            Resources superRes = context.getApplicationContext().getResources();
            return new Resources(assetManager, superRes.getDisplayMetrics(), superRes
                    .getConfiguration());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从资源的名字拿到id
     *
     * @param context   上下文
     * @param type      资源类型 color|drawable
     * @param valueName 资源的名字
     * @return 资源id
     */
    int getResouceFromValueName(Context context, String type, String valueName) {
        int resourceId = -1;
        Resources resources = loadPlugin(context);
        if (resources == null) {
            Log.e(TAG, "Night$ResourceNotFoundException -> " + valueName);
        } else {
            String value;
            if (Night.getInstance().isNight()) {
                value = valueName + "_night";
            } else {
                value = valueName;
            }

            resourceId = resources.getIdentifier(value, type, context.getPackageName());
            if (resourceId == 0) {
                Log.e(TAG, "Night$ResourceNotFoundException -> " + valueName);
            }
        }


        return resourceId;
    }

    private int getResouceFromValueName(Context context, String type, int valueId) {
        return getResouceFromValueName(context, type, Night.getInstance().getResourceMap().get(valueId));
    }

    public String getSkinPackageName() {
        return mSkinPackageName;
    }

    public void setSkinPackageName(String mSkinPackageName) {
        this.mSkinPackageName = mSkinPackageName;
    }
}
