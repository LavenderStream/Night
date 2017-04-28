package org.night.tiny.night.night;

import android.databinding.BindingAdapter;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by tiny on 2017/4/27.
 */
@SuppressWarnings("All")
public class ViewBind {

    /**
     * 将一个drawble放到view 背景
     *
     * @param view      view
     * @param valueName 资源string
     */
    @Deprecated
    @BindingAdapter("drawable")
    public static void setDrawable(View view, String valueName) {
        Night.getInstance().drawable(view, valueName);
    }

    /**
     * 设置textview 字体颜色
     *
     * @param view
     * @param valueName
     */
    @Deprecated
    @BindingAdapter("textcolor")
    public static void setTextColor(@NonNull TextView view, @NonNull String valueName) {
        Night.getInstance().textColor(view, valueName);
    }

    /**
     * 将一个drawble放到view 背景
     * <p>
     * 须在整个应用view加载之前执行init方法
     *
     * @param view    view
     * @param valueId 资源id
     */
    @BindingAdapter("drawable")
    public static void setDrawable(View view, int valueId) {
        Night.getInstance().drawable(view, valueId);
    }

    /**
     * 设置背景颜色
     *
     * @param view      view
     * @param valueName 资源string
     */
    @Deprecated
    @BindingAdapter("background")
    public static void setBackGround(@NonNull View view, @NonNull String valueName) {
        Night.getInstance().background(view, valueName);
    }

    /**
     * 设置textview 字体颜色
     * <p>
     * 须在整个应用view加载之前执行init方法
     *
     * @param view    view
     * @param valueId 资源id
     */
    @BindingAdapter("textcolor")
    public static void setTextColor(@NonNull TextView view, int valueId) {
        Night.getInstance().textColor(view, valueId);
    }

    /**
     * 设置textview hint 文字颜色
     *
     * @param view      view
     * @param valueName 资源string
     */
    @Deprecated
    @BindingAdapter("hinttextcolor")
    public static void setHintTextColor(@NonNull TextView view, @NonNull String valueName) {
        Night.getInstance().hintTextColor(view, valueName);
    }

    /**
     * 设置view背景
     * <p>
     * 须在整个应用view加载之前执行init方法
     *
     * @param view    view
     * @param valueId 资源id
     */
    @BindingAdapter("background")
    public static void setBackGround(@NonNull View view, int valueId) {
        Night.getInstance().background(view, valueId);
    }

    /**
     * 设置textview左边图标
     *
     * @param view      view
     * @param valueName 资源string
     */
    @Deprecated
    @BindingAdapter("drawableleft")
    public static void setDrawableLeft(@NonNull TextView view, @NonNull String valueName) {
        Night.getInstance().drawableLeft(view, valueName);
    }

    /**
     * 设置textview左边图标
     * <p>
     * 须在整个应用view加载之前执行init方法
     *
     * @param view    view
     * @param valueId 资源id
     */
    @BindingAdapter("drawableleft")
    public static void setDrawableLeft(@NonNull TextView view, int valueId) {
        Night.getInstance().drawableLeft(view, valueId);
    }

    /**
     * 设置textview hint 文字颜色
     * <p>
     * 须在整个应用view加载之前执行init方法
     *
     * @param view    view
     * @param valueId 资源id
     */
    @BindingAdapter("hinttextcolor")
    public static void setHintTextColor(@NonNull TextView view, int valueId) {
        Night.getInstance().hintTextColor(view, valueId);
    }

    @BindingAdapter("setimageview")
    public static void setImageWithGlide(@NonNull ImageView imageView, @IdRes int valueId) {
        Night.getInstance().setImageViewWithGlide(imageView, valueId);
    }

    @BindingAdapter("setimageview")
    public static void setImageWithGlide(@NonNull ImageView imageView, String valueName) {
        Night.getInstance().setImageViewWithGlide(imageView, valueName);
    }
}
