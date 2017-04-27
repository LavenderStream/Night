package org.night.tiny.night;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by tiny on 2/20/2017.
 * <p>
 * 客户端夜间模式转换框架
 * <p>
 * 使用id进行注入时，首先保证在创建布局之前首先调用init方法，否则会抛异常
 * <p>
 * 使用string进行注入时没有这个限制
 */
@SuppressWarnings("All")
public class Night {
    private static final String TAG = "Night";

    private static Night ourInstance = new Night();
    // 存放fragment或者activity的set，用于通知存活页面进行更改
    private static Set<NightModeChangeListener> mListeners = new HashSet<>();
    // 存放r文件中的color drawable的相关信息
    private static final SparseArray<String> resourceMap = new SparseArray<>();

    // 当前是否时黑色模式须在程序加载的时候设置
    private boolean mIsNight = false;

    public static Night getInstance() {
        return ourInstance;
    }

    private Night() {
    }

    void drawable(View view, String valueName) {
        Utils.checkNull(valueName);
        view.setTag(R.id.night_drawable, valueName);
        setViewDrawable(view);
    }

    void drawable(View view, int valueId) {
        drawable(view, resourceMap.get(valueId));
    }

    void background(View view, String valueName) {
        Utils.checkNull(valueName);
        view.setTag(R.id.night_background, valueName);
        setViewBackGround(view);
    }

    void background(View view, int valueId) {
        background(view, resourceMap.get(valueId));
    }

    void textColor(TextView view, String valueName) {
        Utils.checkNull(valueName);
        view.setTag(R.id.night_textcolor, valueName);
        setViewTextColor(view);
    }

    void textColor(TextView view, int valueId) {
        textColor(view, resourceMap.get(valueId));
    }

    void hintTextColor(TextView view, String valueName) {
        Utils.checkNull(valueName);
        view.setTag(R.id.night_hinttextcolor, valueName);
        setViewHintTextColor(view);
    }

    void hintTextColor(TextView view, int valueId) {
        hintTextColor(view, resourceMap.get(valueId));
    }

    void drawableLeft(TextView view, String valueName) {
        Utils.checkNull(valueName);
        view.setTag(R.id.night_drawableleft, valueName);
        setViewDrawableLeft(view);
    }

    void drawableLeft(TextView view, int valueId) {
        drawableLeft(view, resourceMap.get(valueId));
    }

    void setImageViewWithGlide(ImageView view, int valueId) {
        setImageViewWithGlide(view, resourceMap.get(valueId));
    }

    void setImageViewWithGlide(ImageView view, String valueName) {
        Utils.checkNull(valueName);
        view.setTag(R.id.night_imageview, valueName);
        setImageViewWithGlide(view);
    }


    /**
     * 递归调用更改布局颜色属性
     *
     * @param rootView 布局根view
     */
    public void change(ViewGroup rootView) {
        changeView(rootView);
        for (int i = 0; i < rootView.getChildCount(); i++) {
            View view = rootView.getChildAt(i);
            changeView(view);
            if (view instanceof ViewGroup && !(view instanceof RecyclerView)) {
                change((ViewGroup) view);
            }
        }
    }

    /**
     * 更改布局资源
     *
     * @param view view
     */
    public void changeView(View view) {
        setViewBackGround(view);
        setViewDrawable(view);
        // 若不是textview的子类就不进行textview的相关设置
        if (view instanceof TextView) {
            setViewTextColor(view);
            setViewHintTextColor(view);
            setViewDrawableLeft(view);
        }
        if (view instanceof ImageView) {
            setImageViewWithGlide((ImageView) view);
        }
        // 修改实现night模式更改的自定义view
        if (view instanceof NightModeChangeListener) {
            ((NightModeChangeListener) view).onNightChange();
        }
    }

    void setViewDrawableLeft(View view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_drawableleft))) {
            int resId = SkinManager.getInstance().getResouceFromValueName(view.getContext(),
                    "drawable", (String) view.getTag(R.id.night_drawableleft));
            Drawable drawableLeft = view.getContext().getResources().getDrawable(resId);
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft
                    .getMinimumHeight());
            ((TextView) view).setCompoundDrawables(drawableLeft, null, null, null);
        }
    }

    void setViewHintTextColor(View view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_hinttextcolor))) {
            int colorId = SkinManager.getInstance().getResouceFromValueName(view.getContext(),
                    "color", (String) view.getTag(R.id.night_hinttextcolor));
            ((TextView) view).setHintTextColor(view.getResources().getColor(colorId));
        }
    }

    void setViewTextColor(View view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_textcolor))) {
            int colorId = SkinManager.getInstance().getResouceFromValueName(view.getContext(),
                    "color", (String) view.getTag(R.id.night_textcolor));
            ((TextView) view).setTextColor(view.getResources().getColor(colorId));
        }
    }

    void setViewDrawable(View view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_drawable))) {
            int drawable = SkinManager.getInstance().getResouceFromValueName(view.getContext(),
                    "drawable", (String) view.getTag(R.id.night_drawable));
            view.setBackgroundResource(drawable);
        }
    }

    void setViewBackGround(View view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_background))) {
            view.setBackgroundResource(SkinManager.getInstance().getResouceFromValueName(view.getContext(),
                    "color", (String) view.getTag(R.id.night_background)));
        }
    }

    void setImageViewWithGlide(ImageView view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_imageview))) {
            Glide.with(view.getContext()).load(SkinManager.getInstance()
                    .getResouceFromValueName(view.getContext(), "drawable",
                            (String) view.getTag(R.id.night_imageview)))
                    .dontAnimate()
                    .centerCrop()
                    .into(new GlideDrawableImageViewTarget(view));
        }
    }

    /**
     * 返回当前状态
     *
     * @return 夜间模式返回 true
     */
    public boolean isNight() {
        return mIsNight;
    }

    /**
     * 在程序初始化的时候调用，设定当前的夜间模式状态
     *
     * @param isNight 当前记录的模式
     * @param rclass  r.xx.class
     */
    public void initNight(boolean isNight, Class... rclass) {
        mIsNight = isNight;
        if (rclass.length == 0) {
            LogUtils.i("Night -> initNight -> Make sure , No id-inject in the project");
        }
        for (Class rclas : rclass) {
            injectR(rclas);
        }
    }

    /**
     * 从r文件注入字段
     *
     * @param clazz r.xx.class
     */
    private void injectR(Class clazz) {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            try {
                String valueString = String.valueOf(field.get(clazz));
                Integer value = Integer.parseInt(field.get(clazz).toString());
                resourceMap.put(field.getInt(clazz), field.getName());
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public void setNight(boolean isNight, String packageName) {
        SkinManager.getInstance().setSkinPackageName(packageName);
        setNight(isNight);
    }


    /**
     * 设置夜间模式，在主动改变模式的时候调用
     *
     * @param isNight
     * @deprecated
     */
    public void setNight(boolean isNight) {
        if (mIsNight == isNight) return;

        mIsNight = isNight;
        for (NightModeChangeListener mListener : mListeners) {
            mListener.onNightChange();
        }
    }

    /**
     * 增加一个监听，监听存活的界面
     *
     * @param l
     */
    public void addListener(NightModeChangeListener l) {
        mListeners.add(l);
    }

    /**
     * 清楚错有页面，须在程序完全退出的时候调用
     */
    public void clearListener() {
        mListeners.clear();
    }

    /**
     * 指定移除监听
     *
     * @param l
     */
    public void removeListener(NightModeChangeListener l) {
        mListeners.remove(l);
    }

    public SparseArray<String> getResourceMap() {
        return resourceMap;
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

                int colorId = SkinManager.getInstance().getResouceFromValueName(activity, "color", "bg");

                window.setStatusBarColor(activity.getResources().getColor(colorId));
                window.setNavigationBarColor(activity.getResources().getColor(colorId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}