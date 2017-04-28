package org.night.tiny.night.night;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;

import org.night.tiny.night.R;

import java.lang.reflect.Field;
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

    private NightDrawable mNightDrawable;

    public static Night getInstance() {
        return ourInstance;
    }

    private Night() {
        mNightDrawable = new NightDrawable();
    }

    public void drawable(View view, int valueId) {
        drawable(view, resourceMap.get(valueId));
    }

    void drawable(View view, String valueName) {
        Utils.checkNull(valueName);
        view.setTag(R.id.night_drawable, valueName);
        mNightDrawable.setViewDrawable(view);
    }

    void background(View view, String valueName) {
        Utils.checkNull(valueName);
        view.setTag(R.id.night_background, valueName);
        mNightDrawable.setViewBackGround(view);
    }

    void background(View view, int valueId) {
        background(view, resourceMap.get(valueId));
    }

    void textColor(TextView view, int valueId) {
        textColor(view, resourceMap.get(valueId));
    }

    void textColor(TextView view, String valueName) {
        Utils.checkNull(valueName);
        view.setTag(R.id.night_textcolor, valueName);
        mNightDrawable.setViewTextColor(view);
    }

    void hintTextColor(TextView view, int valueId) {
        hintTextColor(view, resourceMap.get(valueId));
    }

    void hintTextColor(TextView view, String valueName) {
        Utils.checkNull(valueName);
        view.setTag(R.id.night_hinttextcolor, valueName);
        mNightDrawable.setViewHintTextColor(view);
    }

    void drawableLeft(TextView view, int valueId) {
        drawableLeft(view, resourceMap.get(valueId));
    }

    void drawableLeft(TextView view, String valueName) {
        Utils.checkNull(valueName);
        view.setTag(R.id.night_drawableleft, valueName);
        mNightDrawable.setViewDrawableLeft(view);
    }


    void setImageViewWithGlide(ImageView view, int valueId) {
        setImageViewWithGlide(view, resourceMap.get(valueId));
    }

    void setImageViewWithGlide(ImageView view, String valueName) {
        Utils.checkNull(valueName);
        view.setTag(R.id.night_imageview, valueName);
        mNightDrawable.setImageViewWithGlide(view);
    }

    /**
     * 递归调用更改布局颜色属性
     *
     * @param rootView 布局根view
     */
    public void change(ViewGroup rootView) {
        mNightDrawable.change(rootView);
    }

    /**
     * 更改布局资源
     *
     * @param view view
     */
    public void changeView(View view) {
        mNightDrawable.changeView(view);
    }

    /**
     * @param context  本应用的application上下文
     * @param skinPath 初始化皮肤插件路径
     * @param rclass   r.xx.class
     */
    public void initNight(Context context, String skinPath, String skinName, Class... rclass) {
        initNight(context, false, skinPath, skinName, rclass);
    }

    /**
     * 在程序初始化的时候调用，设定当前的夜间模式状态
     *
     * @param context  本应用的application上下文
     * @param isNight  当前记录的模式
     * @param skinPath 初始化皮肤插件路径
     * @param rclass   r.xx.class
     */
    public void initNight(Context context, boolean isNight, String skinPath, String skinName, Class... rclass) {
        ResourcesManager.getInstance().setSkinPath(skinPath);
        ResourcesManager.getInstance().setContext(context.getApplicationContext());
        ResourcesManager.getInstance().change(skinName, isNight);

        if (rclass.length == 0) {
            LogUtils.i("Night -> initNight -> Make sure , No id-inject in the project");
        }
        for (Class rclas : rclass) {
            injectR(rclas);
        }
    }

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

    /**
     * 当皮肤状态改变时调用
     *
     * @param skinName 皮肤插件名字
     */
    public void setNight(String skinName) {
        setNight(false, skinName);
    }

    /**
     * @param isNight  是否试黑夜状态
     * @param skinName 皮肤插件名字
     */
    public void setNight(boolean isNight, String skinName) {
        ResourcesManager.getInstance().change(skinName, isNight);

        for (NightModeChangeListener mListener : mListeners) {
            mListener.onNightChange();
        }
    }

    /**
     * 返回当前状态
     *
     * @return 夜间模式返回 true
     */
    public boolean isNight() {
        return ResourcesManager.getInstance().isNight();
    }

    /**
     * 得到当前皮肤名字
     *
     * @return 当前应用设置的皮肤名字
     */
    public String getSkinName() {
        return ResourcesManager.getInstance().getSkinName();
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

    SparseArray<String> getResourceMap() {
        return resourceMap;
    }
}