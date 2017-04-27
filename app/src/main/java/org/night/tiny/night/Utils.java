package org.night.tiny.night;

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
}
