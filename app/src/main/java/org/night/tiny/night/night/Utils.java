package org.night.tiny.night.night;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by tiny on 2017/4/27.
 */

public class Utils {

    public static void checkNull(String valueName) {
        if (TextUtils.isEmpty(valueName)) {
            throw new RuntimeException("Night$ResourceMustNoNull" + valueName);
        }
    }

    /**
     * 设置状态栏沉浸
     * <p>
     * 在base activity中调用即可
     *
     * @param activity activity
     */
    public static void setWindowStatusBarColor(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                int colorId = ResourcesManager.getInstance().getResouceFromValueName(activity, "color", "bg");

                window.setStatusBarColor(activity.getResources().getColor(colorId));
                window.setNavigationBarColor(activity.getResources().getColor(colorId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
