package org.night.tiny.night;

import android.text.TextUtils;

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
