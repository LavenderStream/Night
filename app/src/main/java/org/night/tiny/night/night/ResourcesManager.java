package org.night.tiny.night.night;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;

import java.io.File;
import java.lang.reflect.Method;

/**
 * 皮肤资源管理器
 * Created by tiny on 2017/4/27.
 */
@SuppressWarnings("All")
class ResourcesManager {
    private static final String TAG = "SkinManager";
    private static final String PREFIX = "org.";
    private static final String S_SKIN_PATH = Environment.getExternalStorageDirectory() + File
            .separator;
    // 当前资源apk的包名
    private static final String S_DEFAULT_SKIN_NAME = "default";

    // 存放皮肤包的路径 以/结尾 初始化时设置一次
    private static String SKIN_PATH = S_SKIN_PATH;

    // 当前皮肤包的名称
    private String mCurrentSkinName;
    // 当前资源的对象
    private Resources mCurrentResource;
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

    public void setNight(boolean night) {
        isNight = night;
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
        String packageName;
        if (S_DEFAULT_SKIN_NAME.equals(mCurrentSkinName)) {
            mCurrentResource = context.getResources();
            packageName = context.getPackageName();
        } else {
            mCurrentResource = loadPlugin(context);
            packageName = PREFIX + mCurrentSkinName;
        }


        if (mCurrentResource == null) {
            Log.e(TAG, "Night$ResourceNotFoundException -> " + valueName);
        } else {
            String value;
            if (isNight()) {
                value = valueName + "_night";
            } else {
                value = valueName;
            }

            resourceId = mCurrentResource.getIdentifier(value, type, packageName);
            LogUtils.d("ResourcesManager -> getResouceFromValueName: " + resourceId);
            if (resourceId == 0) {
                Log.e(TAG, "Night$ResourceNotFoundException -> " + value);
            }
        }
        return resourceId;
    }

    public Resources loadPlugin(Context context) {
        LogUtils.d("ResourcesManager -> loadPlugin 2: " + context.getPackageName());
        LogUtils.d("ResourcesManager -> loadPlugin 3: " + PREFIX + mCurrentSkinName);
        try {
            LogUtils.d("ResourcesManager -> loadPlugin 111: " + SKIN_PATH + mCurrentSkinName);
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, SKIN_PATH + mCurrentSkinName);
            Resources superRes = context.getApplicationContext().getResources();
            return new Resources(assetManager, superRes.getDisplayMetrics(), superRes
                    .getConfiguration());
        } catch (Exception e) {
            LogUtils.e("SkinManager -> loadPlugin exception: " + e);
            return null;
        }
    }


    /**
     * 设置皮肤包路径
     *
     * @param skinPath
     */
    public void setSkinPath(String skinPath) {
        this.SKIN_PATH = skinPath;
    }

    /**
     * 设置当前皮肤名字
     */
    public void setSkinName(Context context, String skinName) {
        this.mCurrentSkinName = skinName;
    }

    /**
     * 得到现在的资源对象
     *
     * @return
     */
    public Resources getResource() {
        return mCurrentResource;
    }

    public String getSkinName() {
        return mCurrentSkinName;
    }

}
