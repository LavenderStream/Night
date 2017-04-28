package org.night.tiny.night.night;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

/**
 * 皮肤资源管理器
 * Created by tiny on 2017/4/27.
 */
@SuppressWarnings("All")
class ResourcesManager {
    private static final String TAG = "Night";
    private static final String PREFIX = "org.";
    private static final String S_SKIN_PATH = Environment.getExternalStorageDirectory() + File
            .separator;
    // 当前资源apk的包名
    private static final String S_DEFAULT_SKIN_NAME = "default";
    // 存放皮肤包的路径 以/结尾 初始化时设置一次
    private static String SKIN_PATH = S_SKIN_PATH;

    private NightError mError;

    // 当前皮肤包的名称
    private String mSkinName;
    private String mPackageName;
    // 当前资源的对象
    private Resources mResource;
    private Context mContext;
    // 当前是否时黑色模式须在程序加载的时候设置
    private boolean isNight = false;


    private static final ResourcesManager ourInstance = new ResourcesManager();

    public static ResourcesManager getInstance() {
        return ourInstance;
    }

    private ResourcesManager() {
    }


    public boolean isNight() {
        return isNight;
    }

    int getColor(String tag) throws Resources.NotFoundException {
        int resourceId = getResouceFromValueName("color", tag);
        return mResource.getColor(resourceId);
    }

    int getResouceFromValueName(String type, String valueName) {
        int resourceId = 0;

        if (mResource == null) {
            Log.e(TAG, "Night$ResourceNotFoundException -> " + mPackageName + " " + valueName);
            if (mError != null) {
                mError.error(mSkinName);
            }
        } else {
            String value = getValueWithNight(valueName);
            resourceId = mResource.getIdentifier(value, type, mPackageName);
            if (resourceId == 0) {
                Log.e(TAG, "Night$ResourceNotFoundException -> " + mPackageName + " " + value);

                if (mError != null) {
                    mError.error(mSkinName);
                }

                mSkinName = S_DEFAULT_SKIN_NAME;
                loadPlugin();
                resourceId = mResource.getIdentifier(value, type, mPackageName);
            }
        }

        return resourceId;
    }

    private String getValueWithNight(String valueName) {
        String value;
        if (isNight) {
            value = valueName + "_night";
        } else {
            value = valueName;
        }
        return value;
    }

    private void loadPlugin() {
        if (S_DEFAULT_SKIN_NAME.equals(mSkinName)) {
            mResource = mContext.getResources();
            mPackageName = mContext.getApplicationContext().getPackageName();

            return;
        }
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, SKIN_PATH + mSkinName);
            Resources superRes = mContext.getApplicationContext().getResources();
            mResource = new Resources(assetManager, superRes.getDisplayMetrics(), superRes
                    .getConfiguration());
            mPackageName = PREFIX + mSkinName;
        } catch (Exception e) {
            Log.e(TAG, "Night$ResourceNotFoundException -> " + e);
            mResource = null;
        }
    }

    public void setSkinPath(String skinPath) {
        this.SKIN_PATH = skinPath;
    }

    public void change(String skinName, boolean night) {
        setSkinName(skinName);
        setNight(night);
        loadPlugin();
    }

    public void setSkinName(String skinName) {
        this.mSkinName = skinName;
    }

    public void setContext(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public void setError(NightError error) {
        this.mError = error;
    }

    public Resources getResource() {
        return mResource;
    }

    public String getSkinName() {
        return mSkinName;
    }

}
