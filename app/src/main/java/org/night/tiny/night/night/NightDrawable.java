package org.night.tiny.night.night;

import android.content.res.Resources;
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
 * 颜色切换的主要实现类
 * Created by tiny on 4/28/2017.
 */
class NightDrawable {

    void setViewDrawable(View view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_drawable))) {
            int drawable = ResourcesManager.getInstance().getResouceFromValueName(
                    "drawable", (String) view.getTag(R.id.night_drawable));
            view.setBackgroundResource(drawable);
        }
    }


    void setViewBackGround(View view) {
        String tag = (String) view.getTag(R.id.night_background);
        if (!TextUtils.isEmpty(tag)) {
            try {
                int color = ResourcesManager.getInstance().getColor(tag);
                view.setBackgroundColor(color);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    void setViewTextColor(View view) {
        String tag = (String) view.getTag(R.id.night_textcolor);
        if (!TextUtils.isEmpty(tag)) {
            try {
                int color = ResourcesManager.getInstance().getColor(tag);
                ((TextView) view).setTextColor(color);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    void setViewHintTextColor(View view) {
        String tag = (String) view.getTag(R.id.night_hinttextcolor);
        if (!TextUtils.isEmpty(tag)) {
            try {
                int color = ResourcesManager.getInstance().getColor(tag);
                ((TextView) view).setHintTextColor(color);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    void setViewDrawableLeft(View view) {
        if (!TextUtils.isEmpty((CharSequence) view.getTag(R.id.night_drawableleft))) {
            int resId = ResourcesManager.getInstance().getResouceFromValueName(
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
                    .getResouceFromValueName("drawable",
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
        if (view instanceof NightChange) {
            ((NightChange) view).onNightChange();
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
