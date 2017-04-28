package org.night.tiny.night.night;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.night.tiny.night.R;

/**
 * Created by tiny on 4/28/2017.
 */

public class NightDrawable {

    void setViewDrawable(View view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_drawable))) {
            int drawable = ResourcesManager.getInstance().getResouceFromValueName(view.getContext(),
                    "drawable", (String) view.getTag(R.id.night_drawable));
            view.setBackgroundResource(drawable);
        }
    }


    void setViewBackGround(View view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_background))) {
            int resourceId = ResourcesManager.getInstance().getResouceFromValueName(view.getContext(),
                    "color", (String) view.getTag(R.id.night_background));
            view.setBackgroundColor(ResourcesManager.getInstance().getResource().getColor(resourceId));
        }
    }


    void setViewTextColor(View view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_textcolor))) {
            int colorId = ResourcesManager.getInstance().getResouceFromValueName(view.getContext(),
                    "color", (String) view.getTag(R.id.night_textcolor));
            ((TextView) view).setTextColor(view.getResources().getColor(colorId));
        }
    }

    void setViewHintTextColor(View view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_hinttextcolor))) {
            int colorId = ResourcesManager.getInstance().getResouceFromValueName(view.getContext(),
                    "color", (String) view.getTag(R.id.night_hinttextcolor));
            ((TextView) view).setHintTextColor(view.getResources().getColor(colorId));
        }
    }

    void setViewDrawableLeft(View view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_drawableleft))) {
            int resId = ResourcesManager.getInstance().getResouceFromValueName(view.getContext(),
                    "drawable", (String) view.getTag(R.id.night_drawableleft));
            Drawable drawableLeft = view.getContext().getResources().getDrawable(resId);
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft
                    .getMinimumHeight());
            ((TextView) view).setCompoundDrawables(drawableLeft, null, null, null);
        }
    }

    void setImageViewWithGlide(ImageView view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_imageview))) {
            Glide.with(view.getContext()).load(ResourcesManager.getInstance()
                    .getResouceFromValueName(view.getContext(), "drawable",
                            (String) view.getTag(R.id.night_imageview)))
                    .dontAnimate()
                    .centerCrop()
                    .into(new GlideDrawableImageViewTarget(view));
        }
    }


    /**
     * 更改布局资源
     *
     * @param view view
     */
    void changeView(View view) {
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

    /**
     * 递归调用更改布局颜色属性
     *
     * @param rootView 布局根view
     */
    void change(ViewGroup rootView) {
        changeView(rootView);
        for (int i = 0; i < rootView.getChildCount(); i++) {
            View view = rootView.getChildAt(i);
            changeView(view);
            if (view instanceof ViewGroup && !(view instanceof RecyclerView)) {
                change((ViewGroup) view);
            }
        }
    }

}
